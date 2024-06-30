package zql.CallRope.point.log.config;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import zql.CallRope.point.log.Filter.Filter;
import zql.CallRope.point.log.LifeCycle;
import zql.CallRope.point.log.Logc;
import zql.CallRope.point.log.LoggerContext;
import zql.CallRope.point.log.appender.Appender;
import zql.CallRope.point.log.appender.AppenderAttachableImpl;
import zql.CallRope.point.log.appender.AppenderBase;
import zql.CallRope.point.log.appender.Level;
import zql.CallRope.point.log.layout.Layout;
import zql.CallRope.point.log.util.ReflectionUtils;
import zql.CallRope.point.log.util.StringUtils;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static zql.CallRope.point.log.config.Tag.*;

public class XMLConfigurator implements Configurator {
    private URL url;

    private LoggerContext loggerContext;

    private Map<String,Appender> appenderCache = new HashMap<>();
    public XMLConfigurator(URL url, LoggerContext loggerContext) {
        this.url = url;
        this.loggerContext = loggerContext;
    }

    @Override
    public void doConfigure() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = factory.newDocumentBuilder();
            Document document = documentBuilder.parse(url.openStream());
            parse(document.getDocumentElement());
        } catch (Exception ignore) {
            ignore.printStackTrace();
        }
    }

    /**
     * document即配置文件
     */
    public void parse(Element document) {
        parseLoggers(document);
        parseRoot(document);
    }

    // 解析logger标签
    public void parseLoggers(Element document) {
        NodeList loggerNodeList = document.getElementsByTagName(LOGGER_TAG);
        for (int i = 0; i < loggerNodeList.getLength(); i++) {
        }

    }

    public void parseRoot(Element document) {

    }

    public Logc parseChildrenOfLoggerElement(Element element)throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        Logc logger = new Logc();
        String name = element.getAttribute(NAME_ATTR);
        logger.setName(name);

        String level = element.getAttribute(LEVEL_ATTR);
        if(!StringUtils.isEmpty(level)){
            logger.setLevel(Level.parse(level));
        }

        AppenderAttachableImpl aai = new AppenderAttachableImpl();
        logger.setAai(aai);
        // 遍历logger标签下的appender-ref标签
        NodeList appenderRefNodeList = element.getElementsByTagName(APPENDER_REF_TAG);
        int refLength =appenderRefNodeList.getLength();
        for (int i = 0; i < refLength; i++) {
            Element appenderRefNode = (Element) appenderRefNodeList.item(i);
            // 读取appender-ref标签的ref属性
            String appenderName = appenderRefNode.getAttribute(APPENDER_REF_ATTR);
            Appender appender = findAppenderByName(element.getOwnerDocument(), appenderName);
            aai.addAppender(appender);
        }
        return logger;
    }
    protected Appender findAppenderByName(Document document, String appenderName)throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        Appender appender = appenderCache.get(appenderName);
        if(appender != null){
            return appender;
        }

        NodeList appenderNodeList = document.getElementsByTagName(APPENDER_TAG);
        for (int i = 0; i < appenderNodeList.getLength(); i++) {
            Element appenderEle = (Element) appenderNodeList.item(i);
            String itemAppenderName = appenderEle.getAttribute(NAME_ATTR);
            if(appenderName.equals(itemAppenderName)){
                String itemAppenderClassName = appenderEle.getAttribute(CLASS_ATTR);
                appender = (Appender) Class.forName(itemAppenderClassName).newInstance();
                appender.setName(itemAppenderName);

                // 输出器输出方式
                Element layoutEle = getFirstElementByTagName(appenderEle, LAYOUT_TAG);
                Layout layout = parseLayout(layoutEle);
                ((AppenderBase)appender).setLayout(layout);
                startComponent(layout);

                // 输出器编码方式
                Element encodingELe = getFirstElementByTagName(appenderEle, ENCODING_TAG);
                if(encodingELe!=null){
                    String encoding = parseEncoding(encodingELe);
                    ((AppenderBase)appender).setEncoding(encoding);
                }

                // 获取过滤器列表
                NodeList filterNodeList = appenderEle.getElementsByTagName(FILTER_ATTR);
                if(filterNodeList.getLength()>0){
                    List<Filter> filterList = parseFilter(filterNodeList);
                    ((AppenderBase)appender).setFilterList(filterList);
                    for (Filter filter : filterList) {
                        startComponent(filter);
                    }
                }

                startComponent(appender);
                return appender;
            }
        }
        return null;
    }

    /**
     * 获取appender下的第一个tagname标签
     * @param ele
     * @param tagName
     * @return
     */
    private Element getFirstElementByTagName(Element ele,String tagName){
        NodeList elements = ele.getElementsByTagName(tagName);
        if(elements.getLength()>0){
            return (Element) elements.item(0);
        }
        return null;
    }

    // 读取layout标签
    protected Layout parseLayout(Element layoutEle) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        String className = layoutEle.getAttribute(CLASS_ATTR);
        Object instance 	= Class.forName(className).newInstance();
        Layout layout   	= (Layout)instance;

        NodeList params 	= layoutEle.getChildNodes();
        final int length 	= params.getLength();

        for (int loop = 0; loop < length; loop++) {
            Node currentNode = params.item(loop);
            if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
                Element currentElement = (Element) currentNode;
                String tagName = currentElement.getTagName();
                if(tagName.equals(PARAM_TAG)) {
                    String name = currentElement.getAttribute(NAME_ATTR);
                    String value = currentElement.getAttribute(VALUE_ATTR);
                    ReflectionUtils.setFiled(layout,name,value);
                }
            }
        }
        return layout;
    }

    private String parseEncoding(Element encodingEle){
        return encodingEle.getNodeValue();
    }

    protected List<Filter> parseFilter(NodeList filterNodeList) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        List<Filter> filterList = new ArrayList<>(filterNodeList.getLength());

        for (int j = 0; j < filterNodeList.getLength(); j++) {
            Element filterEle = (Element) filterNodeList.item(0);
            String filterClassName = filterEle.getAttribute(CLASS_ATTR);
            Filter filter = (Filter) Class.forName(filterClassName).newInstance();
            NodeList paramNodeList = filterEle.getElementsByTagName(PARAM_TAG);
            for (int k = 0; k < paramNodeList.getLength(); k++) {
                Element paramEle = (Element) paramNodeList.item(k);
                ReflectionUtils.setFiled(filter,paramEle.getAttribute(NAME_ATTR),paramEle.getAttribute(VALUE_ATTR));
            }
            filterList.add(filter);
        }
        return filterList;
    }

    private void startComponent(LifeCycle component){
        component.start();
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

}
