package zql.CallRope.demo;

public class test01 {
    public static void main(String[] args) throws Exception {
        System.out.println(hello("zql",18));
    }

    private static String hello(String name, int age) throws Exception {
        Thread.sleep(1000);
        boolean b = age >= 18;
        return "hello" + name + "，是否成年：" + b;
    }
}
