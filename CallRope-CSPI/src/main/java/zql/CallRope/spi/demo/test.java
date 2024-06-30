package zql.CallRope.spi.demo;

import zql.CallRope.spi.SpiBs;
import zql.CallRope.spi.api.impl.ExtensionLoader;

public class test {
    public static void main(String[] args) {
        ExtensionLoader<Say> loader = (ExtensionLoader<Say>) SpiBs.load(Say.class);
        Say jenny = loader.getExtension("JennySay");
        Say tom = loader.getExtension("tomSay");
        jenny.say();
        tom.say();
    }
}
