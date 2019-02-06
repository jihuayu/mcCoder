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
import jihuayu.mccoder.annotation.TMod;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.lang.reflect.Modifier;
import java.util.Set;

@SupportedAnnotationTypes("jihuayu.mccoder.annotation.TMod")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class TModProcessor extends AbstractProcessor {
    private Messager messager;
    private JavacTrees trees;
    private TreeMaker treeMaker;
    private Names names;
    public static JCTree.JCExpression modid;
    public static JCTree.JCExpression modClass;
    public static JCTree.JCClassDecl main;
    public static JCTree.JCMethodDecl regItem;
    public static JCTree.JCMethodDecl regBlock;
    public static JCTree.JCClassDecl items;
    public static JCTree.JCClassDecl blocks;
    public static String mainPath;
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
        Set<? extends Element> set = roundEnv.getElementsAnnotatedWith(TMod.class);
        if(set.size()>1){
            messager.printMessage(Diagnostic.Kind.ERROR,"find more than one @TMod!");
            return false;
        }
        /////////////////////////////////////////////////////////////
        for(Element i : set){
            mainPath = i.asType().toString();
            messager.printMessage(Diagnostic.Kind.NOTE,i.asType().toString());
            modClass = memberAccess(i.asType().toString());
            main = (JCTree.JCClassDecl) trees.getTree(i);//获取主类
            ////////////////////////////////////////////////////
            for(JCTree.JCAnnotation j : main.getModifiers().annotations){
                if(j.type.toString().equals(TMod.class.getName())){
                    for(JCTree.JCExpression k : j.args){
                        if(k instanceof JCTree.JCAssign) {
                            if(((JCTree.JCAssign) k).lhs.toString().equals("modid")) {
                                modid = ((JCTree.JCAssign) k).rhs;
                            }
                        }
                    }
                    ///////////////////////////////////////////////////
                    if(modid!=null&&main.mods.annotations!=null){
                        JCTree.JCAssign jcAssign = treeMaker.Assign(treeMaker.Ident(getNameFromString("modid")),modid);
                        JCTree.JCAnnotation jcAnnotation = treeMaker.Annotation(memberAccess("net.minecraftforge.fml.common.Mod"),List.of(jcAssign));
                        if(jcAnnotation!=null) {
                            main.getModifiers().annotations = main.getModifiers().getAnnotations().append(jcAnnotation);
                        }
                        ////////////////////////////////////////////////
                        jcAssign = treeMaker.Assign(treeMaker.Ident(getNameFromString("value")),modid);
                        jcAnnotation = treeMaker.Annotation(memberAccess("net.minecraftforge.fml.common.Mod.Instance"),List.of(jcAssign));
                        JCTree.JCVariableDecl jcVariableDecl = treeMaker.VarDef(treeMaker.Modifiers(Modifier.PUBLIC+Modifier.STATIC,List.of(jcAnnotation)),getNameFromString("instance"),modClass,null);
                        main.defs = main.defs.append(jcVariableDecl);
                    }
                }
            }
            ///////////////////////////////////////////////////////////////////////
            JCTree.JCAnnotation annotation = treeMaker.Annotation(memberAccess("net.minecraftforge.fml.common.Mod.EventBusSubscriber"),List.nil());
            JCTree.JCClassDecl classDecl = treeMaker.ClassDef(treeMaker.Modifiers(Modifier.PUBLIC+Modifier.STATIC,List.of(annotation)),getNameFromString("ObjectRegistryHandler"),List.nil(),null,List.nil(),List.nil());
            //////////////////////////////////////////////////////////////////////////
            JCTree.JCTypeApply type = treeMaker.TypeApply(memberAccess("net.minecraftforge.event.RegistryEvent.Register"),List.of(memberAccess("net.minecraft.item.Item")));
            List<JCTree.JCVariableDecl> list = List.of(treeMaker.VarDef(treeMaker.Modifiers(0x200000000L),getNameFromString("event"),type,null));
            List<JCTree.JCStatement> list1 =  List.of(treeMaker.Return(null));
            annotation = treeMaker.Annotation(memberAccess("net.minecraftforge.fml.common.eventhandler.SubscribeEvent"),List.nil());
            JCTree.JCMethodDecl methodDecl = treeMaker.MethodDef(treeMaker.Modifiers(Modifier.PUBLIC+Modifier.STATIC,List.of(annotation)),getNameFromString("addItems"),treeMaker.Type(new Type.JCVoidType()),List.nil(),list,List.nil(),treeMaker.Block(0,list1),null);
            classDecl.defs = List.of(methodDecl);
            regItem = methodDecl;
            //////////////////////////////////////////////////////////////////////////
            type = treeMaker.TypeApply(memberAccess("net.minecraftforge.event.RegistryEvent.Register"),List.of(memberAccess("net.minecraft.block.Block")));
            list = List.of(treeMaker.VarDef(treeMaker.Modifiers(0x200000000L),getNameFromString("event"),type,null));
            list1 =  List.of(treeMaker.Return(null));
            annotation = treeMaker.Annotation(memberAccess("net.minecraftforge.fml.common.eventhandler.SubscribeEvent"),List.nil());
            methodDecl = treeMaker.MethodDef(treeMaker.Modifiers(Modifier.PUBLIC+Modifier.STATIC,List.of(annotation)),getNameFromString("addBlocks"),treeMaker.Type(new Type.JCVoidType()),List.nil(),list,List.nil(),treeMaker.Block(0,list1),null);
            classDecl.defs = classDecl.defs.append(methodDecl);
            regBlock = methodDecl;
            main.defs = main.defs.append(classDecl);
            /////////////////////////////////////////////////////////////////////////////
            classDecl = treeMaker.ClassDef(treeMaker.Modifiers(Modifier.PUBLIC+Modifier.STATIC),getNameFromString("Items"),List.nil(),null,List.nil(),List.nil());
            main.defs = main.defs.append(classDecl);
            items = classDecl;
            /////////////////////////////////////
            classDecl = treeMaker.ClassDef(treeMaker.Modifiers(Modifier.PUBLIC+Modifier.STATIC),getNameFromString("Blocks"),List.nil(),null,List.nil(),List.nil());
            main.defs = main.defs.append(classDecl);
            blocks = classDecl;

        }
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
}