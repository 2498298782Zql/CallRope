package zql.CallRope.spi.api;

public interface IExtensionLoader<T> {
    T getExtension(String alias);

    T createInstace(String name);
}
