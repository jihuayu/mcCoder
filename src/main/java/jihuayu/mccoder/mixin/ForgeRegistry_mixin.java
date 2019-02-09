package jihuayu.mccoder.mixin;

import jihuayu.Init;
import net.minecraft.item.Item;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Mixin(ForgeRegistry.class)
public class ForgeRegistry_mixin<V extends IForgeRegistryEntry<V>> {
    @Overwrite(remap = false)
    public void register(V value) {
        if (value instanceof Item ) {
            System.out.println(value.getRegistryName().getResourceDomain() + "rrrrrrrrrrrrrrrrrrrrrrrr");
            try {
                String path = Init.path + "/assets";
                if (!new File(path).exists())
                    new File(path).mkdir();
                path += "/";
                path += value.getRegistryName().getResourceDomain();
                if (!new File(path).exists())
                    new File(path).mkdir();
                path += "/models";
                if (!new File(path).exists())
                    new File(path).mkdir();
                path += "/item";
                if (!new File(path).exists())
                    new File(path).mkdir();
                path = path + "/" + value.getRegistryName().getResourcePath() + ".json";
                System.out.println(path);
                File file = new File(path);
                if (!file.exists()) {
                    FileWriter writer = new FileWriter(file);
                    writer.append("{\n")
                            .append("\"parent\": \"minecraft:"+ "item"+"/generated\",\n")
                            .append("\"textures\":{\n")
                            .append("\"layer0\": \"")
                            .append(value.getRegistryName().getResourceDomain())
                            .append(":item/")
                            .append(value.getRegistryName().getResourcePath())
                            .append("\"\n")
                            .append("}\n}")
                            .close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        add(-1, value);
    }

    @Shadow(remap = false)
    int add(int id, V value) {
        return 0;
    }
}
