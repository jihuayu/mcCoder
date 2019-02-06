package jihuayu.testmod;

import jihuayu.mccoder.annotation.RegItem;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

@RegItem("jjx")
public class p extends Item {
    public p(){
        this.setRegistryName("ll");
        this.setUnlocalizedName("ok");
        this.setCreativeTab(CreativeTabs.FOOD);
    }
}
