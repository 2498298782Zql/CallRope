package zql.CallRope.spi.annotation;


import java.lang.annotation.*;

/**
 * 作为一个SPI标识(必须要加)
 * 只允许在类上打标签
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface SPI {
}
