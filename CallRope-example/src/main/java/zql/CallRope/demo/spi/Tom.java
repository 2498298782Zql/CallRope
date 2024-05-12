package zql.CallRope.demo.spi;

public class Tom implements SayHi{
    @Override
    public void say() {
        System.out.println("hello, i am tom");
    }
}
