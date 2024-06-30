package zql.CallRope.spi.demo;


import zql.CallRope.spi.annotation.SPIAuto;

@SPIAuto("tomSay")
public class Tom implements Say{
    @Override
    public void say() {
        System.out.println("Hello, Tom");
    }
}
