package jihuayu.mccoder.builder;

import jihuayu.mccoder.Env;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class I18nBuilder {
    public static void build() {
        Env.note("begin build I18nBuilder");

        if (Env.resPath == null || Env.filer == null) {
            Env.error("I18nBuilder null");
            return;
        }
        try {
            String path = Env.resPath + "/assets";
            if (!new File(path).exists())
                new File(path).mkdir();
            path += "/";
            path += Env.modid;
            if (!new File(path).exists())
                new File(path).mkdir();
            path += "/lang";
            if (!new File(path).exists())
                new File(path).mkdir();
            FileWriter writer = new FileWriter(new File(Env.resPath + "/assets/" + Env.modid + "/lang/en_us.lang"));
            String temp;
            for (String i : Env.I18n.keySet()) {

                temp = i + "=" + Env.I18n.get(i) + "\n";
                Env.note(temp);

                writer.append(temp);
                writer.close();
            }
        } catch (IOException e) {
            Env.error("I18nBuilder");
            Env.error(e.getMessage());

        }

    }
}
