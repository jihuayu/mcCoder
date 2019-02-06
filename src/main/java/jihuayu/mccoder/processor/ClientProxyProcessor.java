package jihuayu.mccoder.processor;

import com.sun.tools.javac.api.JavacTrees;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.code.TypeTag;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Names;
import jihuayu.mccoder.annotation.ClientProxy;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.lang.reflect.Modifier;
import java.util.Set;

@SupportedAnnotationTypes("jihuayu.mccoder.annotation.ClientProxy")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class ClientProxyProcessor extends AbstractProcessor {
    private Messager messager;
    private JavacTrees trees;
    private TreeMaker treeMaker;
    private Names names;
    public static JCTree.JCClassDecl client;
    public static String clientPath;
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
        Set<? extends Element> set = roundEnv.getElementsAnnotatedWith(ClientProxy.class);
        if (set.size() > 1) {
            messager.printMessage(Diagnostic.Kind.ERROR, "find more than one @ClientProxy!");
            return false;
        }
        for (Element i : set) {
            String tempName =  i.asType().toString();
            JCTree.JCClassDecl temp = (JCTree.JCClassDecl) trees.getTree(i);
            for (JCTree.JCAnnotation j : temp.getModifiers().annotations) {
                if (j.type.toString().equals(ClientProxy.class.getName())) {
                    client = temp;
                    clientPath = tempName;
                    if(CommonProxyProcessor.commonPath!=null){
                        client.extending = memberAccess(CommonProxyProcessor.commonPath);
                    }
                }
            }
        }
        if(client !=null && !client.defs.equals(List.nil())) {
            JCTree.JCBlock preBody = treeMaker.Block(0,List.nil());
            JCTree.JCBlock initBody = treeMaker.Block(0,List.nil());;
            JCTree.JCBlock postBody = treeMaker.Block(0,List.nil());
            List<JCTree> list = List.nil();
            for(JCTree i : client.defs){
                if(i instanceof JCTree.JCMethodDecl){
                    if(((JCTree.JCMethodDecl) i).params.equals(List.nil())){
                        if(list.equals(List.nil())) {
                            list = List.of(i);
                        } else {
                            list = list.append(i);
                        }
                    }
                    for(JCTree.JCVariableDecl j :((JCTree.JCMethodDecl) i).params){
                        switch (j.vartype.toString()) {
                            case "FMLPreInitializationEvent":
                                preBody = ((JCTree.JCMethodDecl) i).body;

                                break;
                            case "FMLInitializationEvent":
                                initBody = ((JCTree.JCMethodDecl) i).body;

                                break;
                            case "FMLPostInitializationEvent":
                                postBody = ((JCTree.JCMethodDecl) i).body;
                                break;
                            default:
                                if (list.equals(List.nil())) {
                                    list = List.of(i);
                                } else {
                                    list = list.append(i);
                                }
                                break;
                        }
                    }
                }
                else{
                    if(list.equals(List.nil())) {
                        list = List.of(i);
                    } else {
                        list = list.append(i);
                    }
                }
            }
            ////////////////////////////////////////////////////////////
            List<JCTree.JCVariableDecl> l = List.of(treeMaker.VarDef(treeMaker.Modifiers(0x200000000L),getNameFromString("event"),memberAccess("net.minecraftforge.fml.common.event.FMLPreInitializationEvent"),null));
            preBody.stats = preBody.stats.append(treeMaker.Return(null));
            List<JCTree.JCExpression> typeArgs = List.of(memberAccess("net.minecraftforge.fml.common.event.FMLPreInitializationEvent"));
            List<JCTree.JCExpression> args = List.of(memberAccess("event"));
            preBody.stats =  preBody.stats.prepend(treeMaker.Exec(treeMaker.Apply(List.nil(),memberAccess("super.preInit"),args)));
            JCTree.JCMethodDecl methodDecl = treeMaker.MethodDef(treeMaker.Modifiers(Modifier.PUBLIC),getNameFromString("preInit"),treeMaker.Type(new Type.JCVoidType()),List.nil(),l,List.nil(),preBody,null);
            list = list.prepend(methodDecl);
            ////////////////////////////////////////////////////////////
            l = List.of(treeMaker.VarDef(treeMaker.Modifiers(0x200000000L),getNameFromString("event"),memberAccess("net.minecraftforge.fml.common.event.FMLInitializationEvent"),null));
            initBody.stats = initBody.stats.append(treeMaker.Return(null));
             typeArgs = List.of(memberAccess("net.minecraftforge.fml.common.event.FMLInitializationEvent"));
            args = List.of(memberAccess("event"));
            initBody.stats =  initBody.stats.prepend(treeMaker.Exec(treeMaker.Apply(List.nil(),memberAccess("super.init"),args)));
            methodDecl = treeMaker.MethodDef(treeMaker.Modifiers(Modifier.PUBLIC),getNameFromString("init"),treeMaker.Type(new Type.JCVoidType()),List.nil(),l,List.nil(),initBody,null);
            list = list.prepend(methodDecl);
            ////////////////////////////////////////////////////////////
            l = List.of(treeMaker.VarDef(treeMaker.Modifiers(0x200000000L),getNameFromString("event"),memberAccess("net.minecraftforge.fml.common.event.FMLPostInitializationEvent"),null));
            postBody.stats = postBody.stats.append(treeMaker.Return(null));
            typeArgs = List.of(memberAccess("net.minecraftforge.fml.common.event.FMLPostInitializationEvent"));
            args = List.of(memberAccess("event"));
            postBody.stats =  postBody.stats.prepend(treeMaker.Exec(treeMaker.Apply(List.nil(),memberAccess("super.postInit"),args)));
            methodDecl = treeMaker.MethodDef(treeMaker.Modifiers(Modifier.PUBLIC),getNameFromString("postInit"),treeMaker.Type(new Type.JCVoidType()),List.nil(),l,List.nil(),postBody,null);
            list = list.prepend(methodDecl);
            ////////////////////////////////////////////////////////////
            client.defs = list;
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        JCTree.JCClassDecl main = TModProcessor.main;
        if(main!=null){
            List<JCTree.JCExpression> list = List.of(treeMaker.Assign(treeMaker.Ident(getNameFromString("clientSide")),treeMaker.Literal(ClientProxyProcessor.clientPath)));
            list = list.append(treeMaker.Assign(treeMaker.Ident(getNameFromString("serverSide")),treeMaker.Literal(CommonProxyProcessor.commonPath)));
            JCTree.JCAnnotation anno = treeMaker.Annotation(memberAccess("net.minecraftforge.fml.common.SidedProxy"),list);
            JCTree.JCVariableDecl var = treeMaker.VarDef(treeMaker.Modifiers(Modifier.PUBLIC+Modifier.STATIC,List.of(anno)),getNameFromString("proxy"),memberAccess(CommonProxyProcessor.commonPath),null);
            main.defs = main.defs.append(var);
            /////////////////////////////////////////////////////////////
            List<JCTree.JCVariableDecl> l = List.of(treeMaker.VarDef(treeMaker.Modifiers(0x200000000L),getNameFromString("event"),memberAccess("net.minecraftforge.fml.common.event.FMLPreInitializationEvent"),null));
            List<JCTree.JCStatement> list1 =  List.of(treeMaker.Return(null));
            List<JCTree.JCExpression> typeArgs = List.of(memberAccess("net.minecraftforge.fml.common.event.FMLPreInitializationEvent"));
            List<JCTree.JCExpression> args = List.of(memberAccess("event"));
            list1 =  list1.prepend(treeMaker.Exec(treeMaker.Apply(List.nil(),memberAccess("this.proxy.preInit"),args)));
            JCTree.JCMethodDecl methodDecl = treeMaker.MethodDef(treeMaker.Modifiers(Modifier.PUBLIC),getNameFromString("preInit"),treeMaker.Type(new Type.JCVoidType()),List.nil(),l,List.nil(),treeMaker.Block(0,list1),null);
            main.defs = main.defs.append(methodDecl);
            ///////////////////////////////////////////////////////////////
            l = List.of(treeMaker.VarDef(treeMaker.Modifiers(0x200000000L),getNameFromString("event"),memberAccess("net.minecraftforge.fml.common.event.FMLInitializationEvent"),null));
            list1 = List.of(treeMaker.Return(null));
            typeArgs = List.of(memberAccess("net.minecraftforge.fml.common.event.FMLInitializationEvent"));
            args = List.of(memberAccess("event"));
            list1 =  list1.prepend(treeMaker.Exec(treeMaker.Apply(List.nil(),memberAccess("this.proxy.init"),args)));
            methodDecl = treeMaker.MethodDef(treeMaker.Modifiers(Modifier.PUBLIC),getNameFromString("init"),treeMaker.Type(new Type.JCVoidType()),List.nil(),l,List.nil(),treeMaker.Block(0,list1),null);
            main.defs = main.defs.append(methodDecl);
            ///////////////////////////////////////////////////////////////
            l = List.of(treeMaker.VarDef(treeMaker.Modifiers(0x200000000L),getNameFromString("event"),memberAccess("net.minecraftforge.fml.common.event.FMLPostInitializationEvent"),null));
            list1 = List.of(treeMaker.Return(null));
            typeArgs = List.of(memberAccess("net.minecraftforge.fml.common.event.FMLPostInitializationEvent"));
            args = List.of(memberAccess("event"));
            list1 =  list1.prepend(treeMaker.Exec(treeMaker.Apply(List.nil(),memberAccess("this.proxy.postInit"),args)));
            methodDecl = treeMaker.MethodDef(treeMaker.Modifiers(Modifier.PUBLIC),getNameFromString("postInit"),treeMaker.Type(new Type.JCVoidType()),List.nil(),l,List.nil(),treeMaker.Block(0,list1),null);
            main.defs = main.defs.append(methodDecl);
        }
        return true;
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private Name getNameFromString(String s) { return names.fromString(s); }

    private JCTree.JCExpression memberAccess(String components) {
        String[] componentArray = components.split("\\.");
        JCTree.JCExpression expr = treeMaker.Ident(getNameFromString(componentArray[0]));
        for (int i = 1; i < componentArray.length; i++) {
            expr = treeMaker.Select(expr, getNameFromString(componentArray[i]));
        }
        return expr;
    }

}
