package zql.CallRope.point.log;

import zql.CallRope.point.log.config.Configurator;
import zql.CallRope.point.log.config.XMLConfigurator;

import java.net.URL;

public class ContextInitializer {
    final public static String AUTOCONFIG_FILE = "logc.xml";

    private static final LoggerContext DEFAULT_LOGGER_CONTEXT = new LoggerContext();

    public static void autoconfig() {
        URL url = getConfigURL();
        if(url == null){
            System.err.println("config[logc.xml or logc.yml] file not found!");
            return ;
        }
        String urlString = url.toString();
        Configurator configurator = null;

        if(urlString.endsWith("xml")){
            configurator = new XMLConfigurator(url,DEFAULT_LOGGER_CONTEXT);
        }
        configurator.doConfigure();
    }

    private static URL getConfigURL(){
        URL url = null;
        ClassLoader classLoader = ContextInitializer.class.getClassLoader();
        url = classLoader.getResource(AUTOCONFIG_FILE);
        if(url != null){
            return url;
        }
        return null;
    }

    public static LoggerContext getDefautLoggerContext(){
        return DEFAULT_LOGGER_CONTEXT;
    }

}
