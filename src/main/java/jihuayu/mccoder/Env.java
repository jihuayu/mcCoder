package jihuayu.mccoder;

import com.sun.tools.javac.api.JavacTrees;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Names;
import org.apache.velocity.app.VelocityEngine;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import java.util.Properties;

public class Env {
    public static Messager messager;
    public static JavacTrees trees;
    public static TreeMaker treeMaker;
    public static Names names;
    public static Filer filer;
    public static String userPath;
    public static String resPath;
    public static Properties props;
    public static String modid;
    public static String mainClass;
    public static String mainPackage;

    public static VelocityEngine modEnv;
    public static VelocityEngine libEnv;
}
