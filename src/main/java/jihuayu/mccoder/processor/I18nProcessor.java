package jihuayu.mccoder.processor;

import com.sun.tools.javac.tree.JCTree;
import jihuayu.mccoder.Env;
import jihuayu.mccoder.annotation.I18n;
import jihuayu.mccoder.annotation.TMod;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.Set;

@SupportedAnnotationTypes("jihuayu.mccoder.annotation.I18n_new")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class I18nProcessor extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (roundEnv.processingOver()) {
            return true;
        }
        Set<? extends Element> set = roundEnv.getElementsAnnotatedWith(I18n.class);

        for (Element i : set) {
            String ago = null;
            String now = null;
            JCTree tree = Env.trees.getTree(i);
            Env.note(tree.getClass().getName());
            if (tree instanceof JCTree.JCVariableDecl) {
                Env.note("i18n");
                for (JCTree.JCAnnotation expression : ((JCTree.JCVariableDecl) tree).mods.annotations) {
                    if (((JCTree.JCAnnotation) expression).annotationType.toString().equals("I18n_new")) {
                        for (JCTree.JCExpression expression1 : ((JCTree.JCAnnotation) expression).args) {
                            if (expression1 instanceof JCTree.JCAssign) {
                                if (((JCTree.JCAssign) expression1).rhs instanceof JCTree.JCLiteral) {
                                    now = ((JCTree.JCLiteral) ((JCTree.JCAssign) expression1).rhs).value.toString();
                                }
                            }
                        }
                    }
                }
                if (((JCTree.JCVariableDecl) tree).init instanceof JCTree.JCLiteral) {
                    Env.note("init");
                    ago = ((JCTree.JCLiteral) ((JCTree.JCVariableDecl) tree).init).value.toString();
                }
            } else if (tree instanceof JCTree.JCClassDecl) {
                for (JCTree.JCExpression expression : ((JCTree.JCClassDecl) tree).mods.annotations) {
                    if (expression instanceof JCTree.JCAnnotation) {
                        if (((JCTree.JCAnnotation) expression).annotationType.toString().equals("I18n_new")) {
                            for (JCTree.JCExpression expression1 : ((JCTree.JCAnnotation) expression).args)
                                if (expression1 instanceof JCTree.JCAssign) {
                                    if (((JCTree.JCAssign) expression1).rhs instanceof JCTree.JCLiteral) {
                                        now = ((JCTree.JCLiteral) ((JCTree.JCAssign) expression1).rhs).value.toString();
                                    }
                                }
                        } else if (((JCTree.JCAnnotation) expression).annotationType.toString().equals("RegItem")) {
                            for (JCTree.JCExpression expression1 : ((JCTree.JCAnnotation) expression).args)
                                if (expression1 instanceof JCTree.JCAssign) {
                                    if (((JCTree.JCAssign) expression1).lhs.toString().equals("value")) {
                                        if (((JCTree.JCAssign) expression1).rhs instanceof JCTree.JCLiteral) {
                                            ago = ((JCTree.JCLiteral) ((JCTree.JCAssign) expression1).rhs).value.toString();
                                        }
                                    }
                                }
                        }
                    }
                }

            }
            if (ago != null && now != null)
                Env.I18n.put(ago, now);
        }
        return true;
    }
}
//TODO：传入名字不为字符串
//TODO:RegBlock
