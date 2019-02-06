package jihuayu.mccoder;

import com.sun.tools.javac.tree.JCTree;

public class Tuning {
    public static Tuning tuning;
    public JCTree.JCExpression modid;//modid的引用
    public JCTree.JCExpression modClass;//主类的引用
    public JCTree.JCClassDecl main;//主类对象
    public JCTree.JCClassDecl common;//common对象
    public String commonPath;
    public JCTree.JCClassDecl client;//client对象
    public String clientPath;
}
