package zql.CallRope.point.log.config;

import zql.CallRope.point.log.LoggerContext;
import zql.CallRope.point.log.appender.Appender;

import java.net.URL;
import java.util.Map;

public class Tag {
    public static final String APPENDER_TAG = "appender";

    // logger标签，name属性的作用是作用范围
    public static final String LOGGER_TAG = "logger";

    public static final String CLASS_ATTR = "class";

    public static final String NAME_ATTR = "name";

    public static final String VALUE_ATTR = "value";

    public static final String LEVEL_ATTR = "level";

    public static final String FILTER_ATTR = "filter";

    public static final String LAYOUT_TAG = "layout";

    public static final String ENCODING_TAG = "encoding";

    public static final String PARAM_TAG = "param";

    public static final String ROOT_TAG = "root";

    public static final String APPENDER_REF_TAG = "appender-ref";

    public static final String APPENDER_REF_ATTR = "ref";

}
