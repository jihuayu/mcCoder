package jihuayu.mccoder.processor;

import com.sun.tools.javac.tree.JCTree;
import jihuayu.mccoder.Env;
import jihuayu.mccoder.annotation.RegItem;
import jihuayu.mccoder.annotation.TMod;
import jihuayu.mccoder.builder.AllBuilder;
import scala.xml.dtd.ELEMENTS;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.Set;

@SupportedAnnotationTypes("jihuayu.mccoder.annotation.RegItem")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class RegItemProcessor extends AbstractProcessor {
    @Override
    public synchronized boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (roundEnv.processingOver()) return true;
        Set<? extends Element> set = roundEnv.getElementsAnnotatedWith(RegItem.class);

        for (Element i : set) {
            Env.messager.printMessage(Diagnostic.Kind.NOTE,i.asType().toString());
            String path = i.asType().toString();
            StringBuilder decl = new StringBuilder("public static Item ");
            String regName = null;
            Env.messager.printMessage(Diagnostic.Kind.NOTE, path);
            JCTree tree = Env.trees.getTree(i);
            if (tree instanceof JCTree.JCClassDecl) {
                for (JCTree.JCAnnotation annotation : ((JCTree.JCClassDecl) tree).mods.annotations) {
                    if (annotation.annotationType.toString().equals("RegItem")) {
                        for (JCTree.JCExpression assign : annotation.args) {
                            if (assign instanceof JCTree.JCAssign) {
                                if (((JCTree.JCAssign) assign).rhs instanceof JCTree.JCLiteral) {
                                    if (((JCTree.JCAssign) assign).lhs.toString().equals("value")) {
                                        regName = ((JCTree.JCLiteral) ((JCTree.JCAssign) assign).rhs).value.toString();
                                        decl.append(regName.toUpperCase())
                                                .append(" = new ")
                                                .append(path)
                                                .append("().setRegistryName(\"")
                                                .append(regName)
                                                .append("\")");
                                    } else if (((JCTree.JCAssign) assign).lhs.toString().equals("unLocalName")) {
                                        decl.append(".setUnlocalizedName(\"")
                                                .append(((JCTree.JCLiteral) ((JCTree.JCAssign) assign).rhs).value.toString())
                                                .append("\")");
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if(regName!=null){
                Env.messager.printMessage(Diagnostic.Kind.NOTE,"ent");
                Env.itemsDecl.put(regName,decl.toString());
                Env.items.put(regName,regName.toUpperCase());
            }
        }
        AllBuilder.build();
        return true;
    }
}
