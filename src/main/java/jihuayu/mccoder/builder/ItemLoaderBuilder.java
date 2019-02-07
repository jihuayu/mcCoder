package jihuayu.mccoder.builder;

import jihuayu.mccoder.Env;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;

import javax.tools.JavaFileObject;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ItemLoaderBuilder {
    public static void build(){
        Env.note("begin build ItemLoaderBuilder");

        if(Env.libEnv==null||Env.mainPackage==null|| Env.filer==null){
            Env.error("ItemLoaderBuilder null");
            return;
        }
        Template vt = Env.libEnv.getTemplate("mccoder/itemLoader.vm");
        VelocityContext vc = new VelocityContext();
        vc.put("mainPackage",Env.mainPackage);
        List items = new ArrayList<String>();
        List itemsDecl = new ArrayList<String>();
        for(String i :Env.items.keySet()){
            items.add(Env.items.get(i));
        }
        for(String i :Env.itemsDecl.keySet()){
            itemsDecl.add(Env.itemsDecl.get(i));
        }
        List impor =  new ArrayList<String>();
        vc.put("import",impor);
        vc.put("items",items);
        vc.put("itemsDecl",itemsDecl);
        try {
            JavaFileObject jfo = Env.filer.createSourceFile(Env.mainPackage+"."+"loader.ItemLoader");
            BufferedWriter bw = new BufferedWriter(jfo.openWriter());
            vt.merge(vc, bw);
            bw.close();
            Env.note("build ItemLoaderBuilder");
        } catch (IOException e) {
            Env.error("ItemLoaderBuilder");
            Env.error(e.getMessage());
        }
    }
}
