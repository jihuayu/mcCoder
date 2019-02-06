package jihuayu.mccoder.processor;

import com.sun.tools.javac.api.JavacTrees;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Names;
import jihuayu.mccoder.annotation.CommonProxy;
import jihuayu.mccoder.annotation.RegItem;
import org.lwjgl.Sys;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.lang.reflect.Modifier;
import java.util.Set;

@SupportedAnnotationTypes("jihuayu.mccoder.annotation.RegItem")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class RegItemProcessor extends AbstractProcessor {
    private Messager messager;
    private JavacTrees trees;
    private TreeMaker treeMaker;
    private Names names;
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
        if (roundEnv.processingOver()) return true;
        Set<? extends Element> set = roundEnv.getElementsAnnotatedWith(RegItem.class);
        for(Element i :set){
            JCTree tree = trees.getTree(i);
            if(tree instanceof JCTree.JCClassDecl){
                JCTree.JCNewClass newClass =  treeMaker.NewClass(null,List.nil(),memberAccess(i.asType().toString()),List.nil(),null);
                JCTree.JCVariableDecl jcVariableDecl = treeMaker.VarDef(treeMaker.Modifiers(Modifier.PUBLIC+Modifier.STATIC+Modifier.FINAL), getNameFromString(((JCTree.JCClassDecl) tree).name.toString()), treeMaker.TypeApply(memberAccess("net.minecraft.item.Item"), List.nil()), newClass);
                TModProcessor.items.defs = TModProcessor.items.defs.append(jcVariableDecl);
                ////////////////////////////
                JCTree.JCExpression jcExpression = treeMaker.Apply(List.nil(),memberAccess("event.getRegistry"),List.nil());
                jcExpression = treeMaker.Select(jcExpression,getNameFromString("register"));
                JCTree.JCMethodInvocation methodInvocation = treeMaker.Apply(List.of(memberAccess("net.minecraft.item.Item")),jcExpression,List.of(memberAccess(TModProcessor.mainPath+".Items."+((JCTree.JCClassDecl) tree).name.toString())));
                TModProcessor.regItem.body.stats = TModProcessor.regItem.body.stats.prepend(treeMaker.Exec(methodInvocation));
//                for(JCTree j :((JCTree.JCClassDecl) tree).defs){
//                    if(j instanceof JCTree.JCMethodDecl){
//                        if(((JCTree.JCMethodDecl) j).name.toString().equals("a")) {
//                            messager.printMessage(Diagnostic.Kind.ERROR,((JCTree.JCMethodDecl) j).restype.toString());
//                            for(JCTree.JCStatement k :((JCTree.JCMethodDecl) j).body.stats){
//                            if(k instanceof JCTree.JCReturn){
//                                messager.printMessage(Diagnostic.Kind.ERROR,((JCTree.JCReturn) k).toString());
//                                if(((JCTree.JCReturn) k).expr instanceof JCTree.JCLiteral)
//                                    messager.printMessage(Diagnostic.Kind.ERROR,((JCTree.JCReturn) k).toString());
//                            }
//                        }
//                        }

//                    }
//                }
            }
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

//    TypeTag.VOID
}
