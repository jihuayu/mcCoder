import jihuayu.mccoder.annotation.TMod;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.common.Mod;

//@Mod(modid = "mccoder_init_mod",name = "mccoder init mod")
@TMod(modid = "test")
public class InitMod {
    public InitMod(){
        I18n.format("ooo");
    }

}
