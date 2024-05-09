package zql.CallRope.core.es;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class DateUtil {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    /**查询显示，Elasticsearch中的时间都是以2019-12-09T12:12:12的格式存储的，
     所以在查询的时候需要转换日期字符串格式，这同样只影响到数据查询匹配，数据回显并不受影响*/
    public static String getDateStr(Date date){
        Date dateTmp = date==null?new Date():date;
        return sdf.format(dateTmp).replace(" ","T");
    }
    public static String getSub8DateStr(Date date){
        Date dateTmp = (date == null?new Date():date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR,-8);
        dateTmp = calendar.getTime();
        return getDateStr(dateTmp);
    }
}
