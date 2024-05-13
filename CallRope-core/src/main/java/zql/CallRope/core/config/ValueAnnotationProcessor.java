package zql.CallRope.core.config;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.DeclaredType;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;


/**
 *  使用方法：javac -processor ValueAnnotationProcessor 文件夹路径/*.java
 *  暂不启用
 */
@SupportedAnnotationTypes("Value")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class ValueAnnotationProcessor extends AbstractProcessor {

    private static final Properties properties = new Properties();

    static {
        try (InputStream inputStream = ValueAnnotationProcessor.class.getClassLoader().getResourceAsStream("rope-core.properties")) {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(Value.class)) {
            if (element.getKind() == ElementKind.METHOD) {
                ExecutableElement methodElement = (ExecutableElement) element;
                Value valueAnnotation = methodElement.getAnnotation(Value.class);
                String key = valueAnnotation.value();
                String value = properties.getProperty(key);
                if (value != null) {
                    try {
                        setValue(methodElement, value);
                    } catch (Exception e) {
                        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, e.getMessage(), element);
                    }
                } else {
                    processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Property not found for key: " + key, element);
                }
            }
        }
        return true;
    }

    private void setValue(ExecutableElement methodElement, String value) throws Exception {
        TypeElement classElement = (TypeElement) methodElement.getEnclosingElement();
        String fieldName = methodElement.getSimpleName().toString().replace("get", "");
        String setterName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        for (Element enclosedElement : classElement.getEnclosedElements()) {
            if (enclosedElement.getKind() == ElementKind.METHOD && enclosedElement.getSimpleName().toString().equals(setterName)) {
                ExecutableElement setterMethodElement = (ExecutableElement) enclosedElement;
                if (setterMethodElement.getParameters().size() == 1) {
                    DeclaredType parameterType = (DeclaredType) setterMethodElement.getParameters().get(0).asType();
                    if (parameterType.toString().equals(String.class.getName())) {
                        processingEnv.getElementUtils().getElementValuesWithDefaults((AnnotationMirror) methodElement);
                        return;
                    }
                }
            }
        }
        throw new Exception("Setter method not found for property: " + fieldName);
    }
}
