package jihuayu.mccoder.mixin;

import jihuayu.Init;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.Locale;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Mixin(I18n.class)
public class I18n_mixin {
    @Shadow(remap=false)
    private static Locale i18nLocale;

    @Overwrite(remap=false)
    public static String format(String translateKey, Object... parameters) {
        if (!i18nLocale.hasKey(translateKey)) {
            System.out.println(translateKey + "000000000000000000");
            try {
                String path = Init.path + "/assets";
                if (!new File(path).exists())
                    new File(path).mkdir();
                path += "/";
                path += Init.modid;
                if (!new File(path).exists())
                    new File(path).mkdir();
                path += "/lang";
                if (!new File(path).exists())
                    new File(path).mkdir();
                FileWriter writer = new FileWriter(new File(path + "/en_us.lang"), true);
                String str = translateKey + "=" + translateKey + "\n";
                writer.append(str);
                writer.close();
                i18nLocale.properties.put(translateKey, translateKey);
            } catch (IOException e) {
                e.printStackTrace();
                return i18nLocale.formatMessage(translateKey, parameters);
            }
        }
        return i18nLocale.formatMessage(translateKey, parameters);
    }

    @Overwrite(remap=false)
    public static boolean hasKey(String key) {
        if (!i18nLocale.hasKey(key)) {
            System.out.println(key + "000000000000000000");
            try {
                String path = Init.path + "/assets";
                if (!new File(path).exists())
                    new File(path).mkdir();
                path += "/";
                path += Init.modid;
                if (!new File(path).exists())
                    new File(path).mkdir();
                path += "/lang";
                if (!new File(path).exists())
                    new File(path).mkdir();
                FileWriter writer = new FileWriter(new File(path + "/en_us.lang"), true);
                String str = key + "=" + key + "\n";
                writer.append(str);
                writer.close();
                i18nLocale.properties.put(key, key);

            } catch (IOException e) {
                e.printStackTrace();
                return i18nLocale.hasKey(key);
            }
        }
        else{
            if(key.equals(i18nLocale.properties.get(key))){
                return false;
            }
        }
        return i18nLocale.hasKey(key);
    }
}
