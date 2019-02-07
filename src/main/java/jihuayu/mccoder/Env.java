package jihuayu.mccoder;

import com.sun.tools.javac.api.JavacTrees;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Names;
import org.apache.velocity.app.VelocityEngine;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.tools.Diagnostic;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Env {
    public static final boolean ISDEBUG = true;

    public static Messager messager;
    public static JavacTrees trees;
    public static TreeMaker treeMaker;
    public static Names names;
    public static Filer filer;

    public static Properties props;

    public static String modid;
    public static String mainClass;
    public static String mainPackage;

    public static String userPath;
    public static String resPath;
    public static VelocityEngine modEnv;
    public static VelocityEngine libEnv;

    public static Map<String,String>items = new HashMap<>();
    public static Map<String,String>itemsDecl = new HashMap<>();

    public static void error(String str){
        messager.printMessage(Diagnostic.Kind.ERROR,str);
    }
    public static void note(String str){
        if(ISDEBUG)
            messager.printMessage(Diagnostic.Kind.NOTE,str);
    }
}
