package jihuayu.mccoder.mixin;

import com.google.common.collect.Maps;
import jihuayu.Init;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.util.text.translation.LanguageMap;
import org.lwjgl.Sys;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

@Mixin(I18n.class)
public class I18nOld_mixin {
    @Shadow(remap=false)
    private static final LanguageMap fallbackTranslator = new LanguageMap();
    @Shadow(remap=false)
    private static final LanguageMap localizedName = LanguageMap.getInstance();

    @Overwrite(remap=false)
    public static String translateToFallback(String key) {
        if(key.contains(Init.modid+":")) {
            if (!fallbackTranslator.languageList.containsKey(key)) {
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
                    fallbackTranslator.languageList.put(key, key);
                } catch (IOException e) {
                    e.printStackTrace();
                    return fallbackTranslator.translateKey(key);
                }
            }
        }
        return fallbackTranslator.translateKey(key);
    }

    @Overwrite(remap=false)
    public static String translateToLocal(String key) {
        if(key.contains(Init.modid+":")) {
            if (!localizedName.languageList.containsKey(key)) {
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
                    localizedName.languageList.put(key, key);
                } catch (IOException e) {
                    e.printStackTrace();
                    return localizedName.translateKey(key);
                }
            }
        }
        return localizedName.translateKey(key);
    }

}
