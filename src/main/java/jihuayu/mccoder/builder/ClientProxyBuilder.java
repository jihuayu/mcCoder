package jihuayu.mccoder.builder;

import jihuayu.mccoder.Env;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;

import javax.tools.JavaFileObject;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClientProxyBuilder {
    public static void build(){
        Env.note("begin build ClientProxyBuilder");
        if(Env.libEnv==null||Env.mainPackage==null|| Env.filer==null){
            Env.error("ClientProxyBuilder null");
            return;
        }
        Template vt = Env.libEnv.getTemplate("mccoder/client.vm");
        VelocityContext vc = new VelocityContext();
        vc.put("import",Env.importList);
        vc.put("mainPackage",Env.mainPackage);
        vc.put("preInit","");
        vc.put("init","");
        vc.put("postInit","");
        try {
            JavaFileObject jfo = Env.filer.createSourceFile(Env.mainPackage+"."+"proxy.ClientProxy");
            BufferedWriter bw = new BufferedWriter(jfo.openWriter());
            vt.merge(vc, bw);
            bw.close();
            Env.note("build ClientProxyBuilder");

        } catch (IOException e) {
            Env.error("ClientProxyBuilder");
            Env.error(e.getMessage());

        }
    }
}
