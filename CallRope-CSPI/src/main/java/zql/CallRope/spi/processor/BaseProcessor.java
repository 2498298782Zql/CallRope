package zql.CallRope.spi.processor;

import com.sun.tools.javac.api.JavacTrees;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Names;
import zql.CallRope.spi.metadata.model.ProcessContext;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;

public abstract class BaseProcessor extends AbstractProcessor {

    /**
     * Messager主要是用来在编译期打log用的
     */
    protected Messager messager;

    /**
     * JavacTrees提供了待处理的抽象语法树
     */
    protected JavacTrees trees;

    /**
     * TreeMaker封装了创建AST节点的一些方法
     */
    protected TreeMaker treeMaker;

    /**
     * Names提供了创建标识符的方法
     */
    protected Names names;

    /**
     * 执行上下文
     */
    protected ProcessContext processContext;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.messager = processingEnv.getMessager();
        this.trees = JavacTrees.instance(processingEnv);
        Context context = ((JavacProcessingEnvironment) processingEnv).getContext();
        this.treeMaker = TreeMaker.instance(context);
        this.names = Names.instance(context);

        this.processContext = ProcessContext.newInstance().messager(messager)
                .names(names).treeMaker(treeMaker).trees(trees);
    }

}
