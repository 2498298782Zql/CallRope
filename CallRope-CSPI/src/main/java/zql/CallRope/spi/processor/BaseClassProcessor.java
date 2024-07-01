package zql.CallRope.spi.processor;

import com.sun.tools.javac.code.Symbol;
import zql.CallRope.spi.metadata.LClass;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public abstract class BaseClassProcessor extends BaseProcessor {

    /**
     * 获取抽象的注解类型
     *
     * @return 注解类型
     * @since 0.0.2
     */
    protected abstract Class<? extends Annotation> getAnnotationClass();


    /**
     * 获取对应的 class 信息列表
     * @param roundEnv 环境信息
     * @param clazz    注解类型
     * @return 列表
     * @since 0.0.2
     */
    protected List<LClass> getClassList(final RoundEnvironment roundEnv,
                                        final Class<? extends Annotation> clazz) {
        List<LClass> classList = new ArrayList<>();
        Set<? extends Element> serialSet = roundEnv.getElementsAnnotatedWith(clazz);

        // 对于每一个类可以分开，使用线程进行处理。
        for (Element element : serialSet) {
            if (element instanceof Symbol.ClassSymbol) {
                LClass lClass = new LClass(processContext, (Symbol.ClassSymbol) element);
                classList.add(lClass);
            }
        }

        return classList;
    }
}
