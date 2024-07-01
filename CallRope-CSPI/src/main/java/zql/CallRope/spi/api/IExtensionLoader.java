package zql.CallRope.spi.api;

import java.util.List;

public interface IExtensionLoader<T> {
    T getExtension(String alias);


    List<T> getAllExtension();

    T createInstace(String name);

}
