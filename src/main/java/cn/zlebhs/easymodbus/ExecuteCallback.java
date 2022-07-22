package cn.zlebhs.easymodbus;

/**
 * @ClassName: ExecuteCallback
 * @Description: 异步回调
 * @date: 22/7/21 16:21
 * @author hs
 * @version 1.0
 * @since JDK 1.8
 */
public interface ExecuteCallback<T> {

    void success(T response);

    void error(Exception e);

}
