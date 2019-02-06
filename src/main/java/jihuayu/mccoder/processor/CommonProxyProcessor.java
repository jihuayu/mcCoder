package jihuayu.mccoder.processor;

import com.sun.tools.javac.api.JavacTrees;
import com.sun.tools.javac.code.TypeTag;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Names;
import com.sun.tools.javac.code.Type;
import jihuayu.mccoder.annotation.CommonProxy;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.lang.reflect.Modifier;
import java.util.Set;

@SupportedAnnotationTypes("jihuayu.mccoder.annotation.CommonProxy")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class CommonProxyProcessor extends AbstractProcessor {
    private Messager messager;
    private JavacTrees trees;
    private TreeMaker treeMaker;
    private Names names;
    public static JCTree.JCClassDecl common;
    public static String commonPath;
    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.messager = processingEnv.getMessager();
        this.trees = JavacTrees.instance(processingEnv);
        Context context = ((JavacProcessingEnvironment) processingEnv).getContext();
        this.treeMaker = TreeMaker.instance(context);
        this.names = Names.instance(context);
    }

    @Override
    public synchronized boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if(roundEnv.processingOver())return true;
        Set<? extends Element> set = roundEnv.getElementsAnnotatedWith(CommonProxy.class);
        if (set.size() > 1) {
            messager.printMessage(Diagnostic.Kind.ERROR, "find more than one @CommonProxy!");
            return false;
        }
        for (Element i : set) {
            String tempName =  i.asType().toString();
            JCTree.JCClassDecl temp = (JCTree.JCClassDecl) trees.getTree(i);
            for (JCTree.JCAnnotation j : temp.getModifiers().annotations) {
                if (j.type.toString().equals(CommonProxy.class.getName())) {
                    common = temp;
                    commonPath = tempName;
                }
            }
        }
        if(common!=null && !common.defs.equals(List.nil())) {
            JCTree.JCBlock preBody = treeMaker.Block(0,List.nil());
            JCTree.JCBlock initBody = treeMaker.Block(0,List.nil());;
            JCTree.JCBlock postBody = treeMaker.Block(0,List.nil());
            List<JCTree> list = List.nil();
            for(JCTree i : common.defs){
                if(i instanceof JCTree.JCMethodDecl){
                    if(((JCTree.JCMethodDecl) i).params.equals(List.nil())){
                        if(list.equals(List.nil()))
                            list = List.of(i);
                        else
                            list = list.append(i);
                    }
                    for(JCTree.JCVariableDecl j :((JCTree.JCMethodDecl) i).params){

                        if(j.vartype.toString().equals("FMLPreInitializationEvent")){
                            preBody = ((JCTree.JCMethodDecl) i).body;

                        }
                        else if(j.vartype.toString().equals("FMLInitializationEvent")){
                            initBody = ((JCTree.JCMethodDecl) i).body;

                        }
                        else if(j.vartype.toString().equals("FMLPostInitializationEvent")){
                            postBody = ((JCTree.JCMethodDecl) i).body;
                        }
                        else{
                            if(list.equals(List.nil()))
                                list = List.of(i);
                            else
                                list = list.append(i);
                        }
                    }
                }
                else{
                    if(list.equals(List.nil()))
                        list = List.of(i);
                    else
                        list = list.append(i);

                }
            }
            List<JCTree.JCVariableDecl> l = List.of(treeMaker.VarDef(treeMaker.Modifiers(0x200000000L),getNameFromString("event"),memberAccess("net.minecraftforge.fml.common.event.FMLPreInitializationEvent"),null));
            preBody.stats = preBody.stats.append(treeMaker.Return(null));
            JCTree.JCMethodDecl methodDecl = treeMaker.MethodDef(treeMaker.Modifiers(Modifier.PUBLIC),getNameFromString("preInit"),treeMaker.Type(new Type.JCVoidType()),List.nil(),l,List.nil(),preBody,null);
            list = list.prepend(methodDecl);

             l = List.of(treeMaker.VarDef(treeMaker.Modifiers(0x200000000L),getNameFromString("event"),memberAccess("net.minecraftforge.fml.common.event.FMLInitializationEvent"),null));
             initBody.stats = initBody.stats.append(treeMaker.Return(null));
             methodDecl = treeMaker.MethodDef(treeMaker.Modifiers(Modifier.PUBLIC),getNameFromString("init"),treeMaker.Type(new Type.JCVoidType()),List.nil(),l,List.nil(),initBody,null);
             list = list.prepend(methodDecl);

            l = List.of(treeMaker.VarDef(treeMaker.Modifiers(0x200000000L),getNameFromString("event"),memberAccess("net.minecraftforge.fml.common.event.FMLPostInitializationEvent"),null));
            postBody.stats = postBody.stats.append(treeMaker.Return(null));
            methodDecl = treeMaker.MethodDef(treeMaker.Modifiers(Modifier.PUBLIC),getNameFromString("postInit"),treeMaker.Type(new Type.JCVoidType()),List.nil(),l,List.nil(),postBody,null);
            list = list.prepend(methodDecl);
            common.defs = list;
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        return true;
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private JCTree.JCVariableDecl makeVarDef(JCTree.JCModifiers modifiers, String name, JCTree.JCExpression vartype, JCTree.JCExpression init) {
        return treeMaker.VarDef(
                modifiers,
                getNameFromString(name), //名字
                vartype, //类型
                init //初始化语句
        );
    }

    private Name getNameFromString(String s) { return names.fromString(s); }

    private JCTree.JCExpression memberAccess(String components) {
        String[] componentArray = components.split("\\.");
        JCTree.JCExpression expr = treeMaker.Ident(getNameFromString(componentArray[0]));
        for (int i = 1; i < componentArray.length; i++) {
            expr = treeMaker.Select(expr, getNameFromString(componentArray[i]));
        }
        return expr;
    }

    private JCTree.JCExpression memberAccess(Class s) {
        return memberAccess(s.getName());
    }
//    JCTree.JCVariableDecl var = makeVarDef(treeMaker.Modifiers(0), "xiao", memberAccess("java.lang.String"), treeMaker.Literal("methodName"));
//    client.defs = client.defs.append(var);
}
