package zql.CallRope.spi.annotation;


import java.lang.annotation.*;

/**
 * 只允许在类上打标签
 */
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.TYPE})
@Documented
public @interface SPIAuto {

    String value() default "";

    String dir() default "META-INF/services/";
}
