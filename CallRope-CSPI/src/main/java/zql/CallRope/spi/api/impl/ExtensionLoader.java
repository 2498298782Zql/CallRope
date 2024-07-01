package zql.CallRope.spi.api.impl;

import zql.CallRope.spi.annotation.SPI;
import zql.CallRope.spi.api.IExtensionLoader;
import zql.CallRope.spi.constant.SpiConst;
import zql.CallRope.spi.exception.CommonException;
import zql.CallRope.spi.exception.SpiException;
import zql.CallRope.spi.util.ArgsUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public final class ExtensionLoader<T> implements IExtensionLoader<T> {
    /**
     * 接口定义
     */
    private final Class<T> spiClass;

    /**
     * 缓存的实现类实例
     */
    private final Map<String, T> cacheInstances = new ConcurrentHashMap<>();
    /**
     * 类加载器
     */
    private final ClassLoader classLoader;


    private final Map<String,String> classAliasMap = new ConcurrentHashMap<>();

    public ExtensionLoader(Class<T> clazz) {
        this(clazz, Thread.currentThread().getContextClassLoader());
    }

    public ExtensionLoader(Class<T> clazz, ClassLoader classLoader) {
        spiClassCheck(clazz);
        ArgsUtil.notNull(classLoader, "classloader");
        this.classLoader = classLoader;
        this.spiClass = clazz;
        this.initSpiConfig();
    }

    @Override
    public List<T> getAllExtension() {
        Collection<String> classNames = classAliasMap.keySet();
        List<T> instances = new ArrayList<>();
        for(String key : classNames){
            T extension = getExtension(key);
            instances.add(extension);
        }
        return instances;
    }

    @Override
    public T getExtension(String alias) {
        ArgsUtil.notNull(alias, "alias");
        T instance = cacheInstances.get(alias);
        if(instance != null){
            return instance;
        }
        synchronized (cacheInstances){
            instance = cacheInstances.get(alias);
            if(instance == null){
                instance = createInstace(alias);
                cacheInstances.put(alias, instance);
            }
        }
        return instance;
    }

    @Override
    public T createInstace(String alias) {
        String className = classAliasMap.get(alias);
        if(className == null || "".equals(className)){
            throw new SpiException("SPI config not found for spi:"+spiClass.getName() + " with alias " + className);
        }
        try{
            Class clazz = Class.forName(className);
            return (T) clazz.newInstance();
        } catch (IllegalAccessException | InstantiationException | ClassNotFoundException e) {
            throw new SpiException(e);
        }
    }


    public void spiClassCheck(final Class<T> spiClass){
        ArgsUtil.notNull(spiClass, "spiClass");
        if(!spiClass.isInterface()){
            throw new SpiException("Spi class isn't interface" + spiClass);
        }

        if(!spiClass.isAnnotationPresent(SPI.class)){
            throw new SpiException("Spi class is must be anonotated with @SPI," + spiClass);
        }
    }

    /**
     * 读取接口对应文件，将其键值对放入map而不进行全部实例化
     */
    public void initSpiConfig(){
        // 读取对应接口的文件 example: interface: AsyncThreadAspect    fullname : /META_INF/services/zql/CallRope/core/aspect/AsyncThreadAspect
        String fullName = SpiConst.JDK_DIR + this.spiClass.getName();
        try {
            Enumeration<URL> urlEnumeration = this.classLoader.getResources(fullName);
            if(!urlEnumeration.hasMoreElements()){
                throw new SpiException("SPI config file for class not found:" + spiClass.getName());
            }

            URL url = urlEnumeration.nextElement();
            List<String> allLines = readAllLines(url);
            if(allLines.size() == 0){
                throw new SpiException("SPI config file for class is empty: " + spiClass.getName());
            }

            // key = value
            for(String line : allLines){
                String[] node = line.split(SpiConst.CONFIG_SPLITTER);
                classAliasMap.put(node[0], node[1]);
            }
        } catch (IOException e) {
            throw new SpiException("SPI config file is unknown exception", e);
        }

    }

    public List<String> readAllLines(URL url){
        ArgsUtil.notNull(url, "url");
        List<String> result = new ArrayList<>();
        try(InputStream is = url.openStream()){
            BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            String line;
            while((line = br.readLine())!= null){
                result.add(line);
            }
        }catch (IOException e){
            throw new CommonException("spi config 读取异常:", e);
        }
        return result;
    }
}
