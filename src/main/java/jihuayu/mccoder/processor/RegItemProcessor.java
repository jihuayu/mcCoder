package jihuayu.mccoder.processor;

import com.sun.tools.javac.tree.JCTree;
import jihuayu.mccoder.Env;
import jihuayu.mccoder.annotation.RegItem;
import jihuayu.mccoder.annotation.RegItems;
import jihuayu.mccoder.builder.AllBuilder;;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.Set;

@SupportedAnnotationTypes({"jihuayu.mccoder.annotation.RegItem", "jihuayu.mccoder.annotation.RegItems"})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class RegItemProcessor extends AbstractProcessor {
    private String path;
    private StringBuilder decl;
    private String regName;

    @Override
    public synchronized boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (roundEnv.processingOver()) {
            return true;
        }
        Set<? extends Element> set = roundEnv.getElementsAnnotatedWith(RegItem.class);
        Set<? extends Element> set2 = roundEnv.getElementsAnnotatedWith(RegItems.class);

        for (Element i : set) {
            regs(i);
        }

        for (Element i : set2) {
            regs(i);
        }

        return true;
    }

    private void regs(Element i) {
        Env.note(i.asType().toString());
        JCTree tree = Env.trees.getTree(i);
        if (tree instanceof JCTree.JCClassDecl) {
            for (JCTree.JCAnnotation annotation : ((JCTree.JCClassDecl) tree).mods.annotations) {
                Env.note(annotation.annotationType.toString());
                if (annotation.annotationType.toString().equals("RegItem")) {
                    path = ((JCTree.JCClassDecl) tree).name.toString();
                    decl = new StringBuilder("public static Item ");
                    regName = null;
                    reg(annotation);
                }

            }
        }
    }

    public void reg(JCTree.JCAnnotation annotation) {
        for (JCTree.JCExpression assign : annotation.args) {
            if (assign instanceof JCTree.JCAssign) {
                if (((JCTree.JCAssign) assign).lhs.toString().equals("value")) {
                    if(!Env.getVar(((JCTree.JCAssign) assign).rhs).equals("")) {
                        regName = Env.getVar(((JCTree.JCAssign) assign).rhs);//TODO:fixed
                        decl.append(regName.toUpperCase().replace("\"","").replaceAll("\\W","_"))
                                .append(" = new ")
                                .append(path)
                                .append("().setRegistryName(")
                                .append(regName)
                                .append(")");

                    }
                }
                else if (((JCTree.JCAssign) assign).lhs.toString().equals("unLocalName")) {
                    if(!Env.getVar(((JCTree.JCAssign) assign).rhs).equals("")) {
                        String temp = Env.getVar(((JCTree.JCAssign) assign).rhs);//TODO:fixed
                        decl.append(".setUnlocalizedName(")
                                .append(temp)
                                .append(")");
                    }
                }
            }
        }
        if (regName != null) {
            Env.note("ent");
            Env.itemsDecl.put(regName, decl.toString());
            Env.items.put(regName, regName.toUpperCase().replace("\"","").replaceAll("\\W","_"));
        } else {
            Env.error("regName null");
        }
    }
}
//TODO：传入名字不为字符串
//TODO：物品修改
//TODO：自动非本地化