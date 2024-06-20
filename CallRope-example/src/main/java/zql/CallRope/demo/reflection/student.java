package zql.CallRope.demo.reflection;

public class student implements Person {
    int age;
    int height;
    @Override
    public void say() {
        System.out.println("hello world");
    }



    @Override
    public String toString() {
        return "student{" +
                "age=" + age +
                ", height=" + height +
                '}';
    }
}
