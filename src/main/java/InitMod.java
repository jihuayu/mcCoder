import jihuayu.mccoder.annotation.TMod;
import net.minecraftforge.fml.common.Mod;

//@Mod(modid = "mccoder_init_mod")
@TMod(modid = "test")
public class InitMod {
    static {
        Init.main(null);
    }
}
