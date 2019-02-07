package jihuayu.mccoder.builder;

import jihuayu.mccoder.Env;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;

import javax.tools.JavaFileObject;
import java.io.BufferedWriter;
import java.io.IOException;

public class ClientProxyBuilder {
    public static void build(){
        if(Env.libEnv==null||Env.mainClass==null||Env.modid==null||Env.mainPackage==null|| Env.filer==null)return;
        Template vt = Env.libEnv.getTemplate("mccoder/client.vm");
        VelocityContext vc = new VelocityContext();
        vc.put("mainPackage",Env.mainPackage);
        vc.put("import","");
        vc.put("preInit","");
        vc.put("init","");
        vc.put("postInit","");
        try {
            JavaFileObject jfo = Env.filer.createSourceFile(Env.mainPackage+"."+"proxy.ClientProxy");
            BufferedWriter bw = new BufferedWriter(jfo.openWriter());
            vt.merge(vc, bw);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
