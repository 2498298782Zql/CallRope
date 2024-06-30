package zql.CallRope.point.log.util;

import zql.CallRope.point.log.exception.ConfigException;

import java.lang.reflect.Field;

public class ReflectionUtils {

    public static void setFiled(Object instance,String fieldName,String fieldValue){
        Class<?> clazz = instance.getClass();
        try{
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(instance,fieldValue);
        }catch (ReflectiveOperationException e){
            throw new ConfigException(e);
        }
    }

    public static Object newInstance(Class<?> clazz){
        try {
            return clazz.newInstance();
        } catch (InstantiationException e) {
            throw new ConfigException(e);
        } catch (IllegalAccessException e) {
            throw new ConfigException(e);
        }
    }
}
