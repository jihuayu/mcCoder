package jihuayu.mccoder.builder;

import jihuayu.mccoder.Env;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;

import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainClassBuilder {
    public static void build(){
        Env.note("begin build MainClassBuilder");
        if(Env.libEnv==null||Env.mainClass==null||Env.modid==null||Env.mainPackage==null|| Env.filer==null){
            Env.error("MainClassBuilder null");
            return;
        }
        Template vt = Env.libEnv.getTemplate("mccoder/main.vm");
        VelocityContext vc = new VelocityContext();
        vc.put("modid","\""+Env.modid+"\"");
        vc.put("mainClass",Env.mainClass);
        vc.put("mainPackage",Env.mainPackage);
        vc.put("import",Env.importList);
        try {
            JavaFileObject jfo = Env.filer.createSourceFile(Env.mainPackage+"."+Env.mainClass);
            BufferedWriter bw = new BufferedWriter(jfo.openWriter());
            vt.merge(vc, bw);
            bw.close();
            Env.note("build MainClassBuilder");

        } catch (IOException e) {
            Env.error("MainClassBuilder");
            Env.error(e.getMessage());
        }
    }
}
