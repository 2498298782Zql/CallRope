package zql.CallRope.point.log.appender;

import java.io.IOException;
import java.io.OutputStream;
/**
 * 不推荐使用，输出到控制台
 */
public class ConsoleAppender extends AppenderBase {
    private OutputStream out = System.out;
    private OutputStream out_err = System.err;
    @Override
    protected void doAppend(String body) {
        try {
            out.write(body.getBytes(encoding));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
