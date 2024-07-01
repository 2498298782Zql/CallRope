package zql.CallRope.spi.metadata;

import com.sun.tools.javac.tree.JCTree;
import zql.CallRope.spi.metadata.model.ProcessContext;

public class LObject extends LCommon{

    /**
     * 表达式
     */
    private JCTree.JCExpression expression;

    /**
     * 代码块
     */
    private JCTree.JCStatement statement;

    public LObject(ProcessContext processContext) {
        super(processContext);
    }

    public JCTree.JCExpression expression() {
        return expression;
    }

    public LObject expression(JCTree.JCExpression expression) {
        this.expression = expression;
        return this;
    }

    public JCTree.JCStatement statement() {
        return statement;
    }

    public LObject statement(JCTree.JCStatement statement) {
        this.statement = statement;
        return this;
    }
}
