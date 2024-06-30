package zql.CallRope.core.demo;

import zql.CallRope.spi.annotation.SPIAuto;

@SPIAuto("JennySay")
public class Jenny implements Say {
    @Override
    public void say() {
        System.out.println("hello, Jenny");
    }
}
