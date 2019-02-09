package jihuayu.mccoder;


import jihuayu.Init;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.Mixins;

import javax.annotation.Nullable;
import java.util.Map;

@IFMLLoadingPlugin.MCVersion(ForgeVersion.mcVersion)
@IFMLLoadingPlugin.SortingIndex(-7500)
//@IFMLLoadingPlugin.TransformerExclusions("io.jihuayu.goingages")
public final class LoadingPlugin implements IFMLLoadingPlugin {

    public LoadingPlugin() {
        Init.main(null);
        MixinBootstrap.init();
        Mixins.addConfiguration("mixins.mccoder.json");
}

    @Override public String[] getASMTransformerClass() {  return new String[0]; }
    @Override public String getModContainerClass() { return null; }
    @Nullable
    @Override public String getSetupClass() { return null; }
    @Override public void injectData(Map<String, Object> data) {}
    @Override public String getAccessTransformerClass() { return null; }
}