package jihuayu.mccoder;

import com.sun.tools.javac.api.JavacTrees;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Names;
import org.apache.velocity.app.VelocityEngine;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.tools.Diagnostic;
import java.util.*;

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
    public static String resPath;

    public static String userPath;

    public static VelocityEngine modEnv;
    public static VelocityEngine libEnv;

    public static Map<String,String>allVar = new HashMap<>();
    public static Map<String,String>allImport = new HashMap<>();
    public static List<String>importList = new ArrayList<>();

    public static Map<String,String>items = new HashMap<>();
    public static Map<String,String>itemsDecl = new HashMap<>();

    public static Map<String,String>I18n = new HashMap<>();

    public static void error(String str){
        messager.printMessage(Diagnostic.Kind.ERROR,str);
    }
    public static void note(String str){
        if(ISDEBUG)
            messager.printMessage(Diagnostic.Kind.NOTE,str);
    }
    public static String findVar(String name){
        for(String i :allVar.keySet()){
            if(i.contains(name)){
                return i;
            }
        }
        return "";
    }
    public static String findVarAce(JCTree.JCFieldAccess i,String className){
        String str = i.toString();
        if(str.contains("this.")){
            return str;
        }
        String[] path = str.split(".");
        String result = findVar(className+path[0]);
        if(!result.equals("")){
            return "this."+str;
        }
        result = findVar(path[0]+path[1]);
        if(!result.equals("")){
            return result.split(path[0])[0]+str;
        }
        return "";
    }
    public static String getVar(JCTree.JCExpression expression){
        StringBuilder path = new StringBuilder();
        return  expression.toString();
//        if(expression instanceof JCTree.JCLiteral){
//            return ((JCTree.JCLiteral) expression).toString();
//        }
//        else if(expression instanceof JCTree.JCFieldAccess){
//            path.insert(0,((JCTree.JCFieldAccess) expression).name.toString());
//            path.insert(0,".");
//            JCTree.JCExpression i = ((JCTree.JCFieldAccess) expression).selected;
//            while(i instanceof JCTree.JCFieldAccess){
//                path.insert(0,((JCTree.JCFieldAccess) i).name.toString());
//                path.insert(0,".");
//                i = ((JCTree.JCFieldAccess) i).selected;
//
//            }
//            if(i instanceof JCTree.JCIdent){
//                path.insert(0,((JCTree.JCIdent) i).name.toString());
//            }
//        }
//        note(path.toString());
//        return path.toString();
//    }
//    public static JCTree.JCExpression memberAccess(String components) {
//        String[] componentArray = components.split("\\.");
//        JCTree.JCExpression expr = treeMaker.Ident(getNameFromString(componentArray[0]));
//        for (int i = 1; i < componentArray.length; i++) {
//            expr = treeMaker.Select(expr, getNameFromString(componentArray[i]));
//        }
//        return expr;
    }
    public static Name getNameFromString(String s) { return names.fromString(s); }
}
