package jihuayu.mccoder.processor;


import com.sun.source.tree.CompilationUnitTree;
import com.sun.tools.javac.api.JavacTrees;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.tree.TreeTranslator;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Names;
import jihuayu.mccoder.Env;
import jihuayu.mccoder.annotation.TMod;
import jihuayu.mccoder.builder.AllBuilder;
import org.apache.velocity.app.VelocityEngine;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.Set;

@SupportedAnnotationTypes("jihuayu.mccoder.annotation.TMod")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class TModProcessor extends AbstractProcessor {
    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        Env.messager = processingEnv.getMessager();
        Env.trees = JavacTrees.instance(processingEnv);
        Context context = ((JavacProcessingEnvironment) processingEnv).getContext();
        Env.treeMaker = TreeMaker.instance(context);
        Env.names = Names.instance(context);
        Env.filer = processingEnv.getFiler();
    }

    @Override
    public synchronized boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (roundEnv.processingOver()) {
            Env.note(String.valueOf(Math.random()));
            AllBuilder.build();
            return true;
        }
        Set<? extends Element> set = roundEnv.getRootElements();
        for (Element i : set) {
            CompilationUnitTree tree = Env.trees.getPath(i).getCompilationUnit();
            if (tree instanceof JCTree.JCCompilationUnit) {
                if(!Env.importList.contains(((JCTree.JCCompilationUnit) tree).packge.fullname.toString()+".*"))
                    Env.importList.add(((JCTree.JCCompilationUnit) tree).packge.fullname.toString()+".*");
            }
        }
        set = roundEnv.getElementsAnnotatedWith(TMod.class);
        for (Element i : set) {
            if (Env.trees.getTree(i) instanceof JCTree.JCClassDecl) {
                List<JCTree.JCAnnotation> anno = ((JCTree.JCClassDecl) Env.trees.getTree(i)).mods.annotations;
                for (JCTree.JCAnnotation j : anno) {
                    if (j.annotationType.toString().equals("TMod")) {
                        for (JCTree.JCExpression k : j.args) {
                            if (k instanceof JCTree.JCAssign) {
                                if (!Env.getVar(((JCTree.JCAssign) k).rhs).equals("")) {
                                    Env.modid = Env.getVar(((JCTree.JCAssign) k).rhs).replace("\"", "");//TODO:fixed
                                }
                            }
                        }
                    }
                }
            }
        }
        ///////////////////////////////////////////////////////////////////
        try {
            if (Env.modid == null) return true;
            Properties props = new Properties();
            Env.note("file:///" + System.getProperty("user.home").replace("\\", "/") + "/.mccoder/" + Env.modid + "/mccoder.properties");
            URL url = new URL("file:///" + System.getProperty("user.home").replace("\\", "/") + "/.mccoder/" + Env.modid + "/mccoder.properties");
            props.load(url.openStream());
            Env.props = props;
            Env.userPath = System.getProperty("user.home").replace("\\", "/") + "/.mccoder/" + Env.modid + "/";
            String mainClass = props.getProperty("mainClass");
            if (mainClass != null) {
                Env.mainClass = mainClass;
                Env.note(mainClass);
            }
            String mainPackage = props.getProperty("mainPackage");
            if (mainPackage != null) {
                Env.mainPackage = mainPackage;
                Env.note(mainPackage);
            }
            String resPath = props.getProperty("resPath");
            if (resPath != null) {
                Env.resPath = resPath;
                Env.note(resPath);
            }
            ///////////////////////////////////////////////////////////////////
            props = new Properties();
            url = this.getClass().getClassLoader().getResource("velocity.properties");
            if (url == null) return true;
            props.load(url.openStream());
            props.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
            Env.libEnv = new VelocityEngine(props);
            Env.libEnv.init();
            ///////////////////////////////////////////////////////////////////
            props.load(url.openStream());
            props.setProperty(VelocityEngine.FILE_RESOURCE_LOADER_PATH, System.getProperty("user.home") + "/.mccoder/" + Env.modid);
            Env.modEnv = new VelocityEngine(props);
            Env.modEnv.init();
        } catch (IOException e) {
            Env.error("worry");
            for (StackTraceElement i : e.getStackTrace()) {
                Env.error(i.toString());
            }
        }
        return true;
    }

    public static void findAllVar(JCTree.JCClassDecl classDecl, String pathOld) {
        StringBuilder path = new StringBuilder(pathOld);
        for (JCTree i : classDecl.defs) {
            if (i instanceof JCTree.JCClassDecl) {
                path.append(".")
                        .append(((JCTree.JCClassDecl) i).name.toString());
                findAllVar((JCTree.JCClassDecl) i, pathOld);
            } else if (i instanceof JCTree.JCVariableDecl) {
                Env.allVar.put(path.toString() + (".") + (((JCTree.JCVariableDecl) i).name.toString()), i.toString());
            }
        }

    }
}


//            tree.accept(new TreeTranslator() {
//                @Override
//                public void visitClassDef(JCTree.JCClassDecl jcClassDecl) {
//                    Env.note(jcClassDecl.toString()+"llllllllllllll");
//                    super.visitClassDef(jcClassDecl);
//                }
//            });