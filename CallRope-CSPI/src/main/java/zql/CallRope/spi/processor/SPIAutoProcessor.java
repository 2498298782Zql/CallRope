package zql.CallRope.spi.processor;


import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Type;
import javafx.util.Pair;
import zql.CallRope.spi.annotation.SPI;
import zql.CallRope.spi.annotation.SPIAuto;
import zql.CallRope.spi.exception.SpiException;
import zql.CallRope.spi.metadata.LClass;
import zql.CallRope.spi.util.StreamUtil;
import zql.CallRope.spi.util.StringUtils;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.util.*;

@SupportedAnnotationTypes("zql.CallRope.spi.annotation.SPIAuto")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class SPIAutoProcessor extends BaseClassProcessor{

    public SPIAutoProcessor() {
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        System.out.println("SPIAutoProcessor is processing......");
        List<LClass> classList = getClassList(roundEnv, getAnnotationClass());
        Map<String, Set<String>> spiInterfaceMap = new HashMap<>();

        for(LClass spiClassImpl :classList){
            String spiInterfaceName = getSpiInterfaceName(spiClassImpl);
            String spiImplFullName = spiClassImpl.classSymbol().fullname.toString();
            if(StringUtils.isEmpty(spiInterfaceName)){
                throw new SpiException("@SPI interface not found for class: "
                        + spiImplFullName);
            }
            Pair<String, String> aliasAndDir = getAliasAndDir(spiClassImpl);
            String newLine =aliasAndDir.getKey() +  "=" + spiImplFullName;
            String spiFilePath = aliasAndDir.getValue() + spiInterfaceName;

            Set<String> lineSet = spiInterfaceMap.get(spiFilePath);

            if(lineSet == null){
                lineSet = new HashSet<>();
            }

            lineSet.add(newLine);
            spiInterfaceMap.put(spiFilePath, lineSet);
        }
        generateSpiFile(spiInterfaceMap);
        return true;
    }

    @Override
    protected Class<? extends Annotation> getAnnotationClass() {
        return SPIAuto.class;
    }


    /**
     * 传入一个具有SPIAuto注解的类,封装其SPIAuto的属性
     * @param lClass
     * @return
     */
    private Pair<String,String> getAliasAndDir(LClass lClass){
        SPIAuto spiAuto = lClass.classSymbol().getAnnotation(SPIAuto.class);

        String fullClassName = lClass.classSymbol().fullname.toString();
        String simpleClassName = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);

        String alias = spiAuto.value();
        if(StringUtils.isEmpty(alias)){
            // 如果没有配置spiAuto的value属性，则自动使用类名作为key
            alias = StringUtils.firstToLowerCase(simpleClassName);
        }
        return new Pair<>(alias, spiAuto.dir());
    }


    /**
     * 获取传入的类中第一个标注了SPI注解的接口的全类名
     * @param lClass
     * @return
     */
    private String getSpiInterfaceName(final LClass lClass) {
        com.sun.tools.javac.util.List<Type> typeList =  lClass.classSymbol().getInterfaces();
        if(null == typeList || typeList.isEmpty()) {
            return "";
        }
        // 获取注解对应的值
        SPIAuto auto = lClass.classSymbol().getAnnotation(SPIAuto.class);
        for(Type type : typeList) {
            Symbol.ClassSymbol tsym = (Symbol.ClassSymbol) type.tsym;
            //TOOD: 后期这里添加一下拓展。
            if(tsym.getAnnotation(SPI.class) != null) {
                return tsym.fullname.toString();
            }
        }
        return "";
    }


    /**
     * 创建新的文件
     * key: 文件路径
     * value: 对应的内容信息
     * 核心逻辑: 检查文件是否存在并读取已有内容，如果存在则追加新内容，如果不存在则创建新文件并写入内容
     */
    private void generateSpiFile(Map<String, Set<String>> spiClassMap) {
        Filer filer = processingEnv.getFiler();

        for(Map.Entry<String, Set<String>> entry : spiClassMap.entrySet()) {
            String fullFilePath = entry.getKey();
            Set<String> newLines = entry.getValue();
            try {
                FileObject existingFile = filer.getResource(StandardLocation.CLASS_OUTPUT, "",fullFilePath);
                System.out.println("Looking for existing resource file at " + existingFile.toUri());
                Set<String> oldLines = new HashSet<String>(StreamUtil.readAllLines(existingFile.openInputStream()));
                System.out.println("Looking for existing resource file set " + oldLines);

                // 写入
                newLines.addAll(oldLines);
                StreamUtil.write(newLines, existingFile.openOutputStream());
                return;
            } catch (IOException e) {
                System.out.println("Resources file not exists.");
            }

            try {
                FileObject newFile = filer.createResource(StandardLocation.CLASS_OUTPUT, "",
                        fullFilePath);
                try(OutputStream outputStream = newFile.openOutputStream();) {
                    StreamUtil.write(newLines, outputStream);
                    System.out.println("Write into file "+newFile.toUri());
                } catch (IOException e) {
                    throw new SpiException(e);
                }
            } catch (IOException e) {
                throw new SpiException(e);
            }
        }
    }

}
