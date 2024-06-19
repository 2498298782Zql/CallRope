package zql.CallRope.demo.reflection;


import java.lang.reflect.Field;

public class demo {
    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        Person person = new student();
        person.say();
        Class<?> clazz = person.getClass();
        Field height = clazz.getDeclaredField("height");
        height.setAccessible(true);
        height.set(person, 10);
        System.out.println(person);
    }
}
