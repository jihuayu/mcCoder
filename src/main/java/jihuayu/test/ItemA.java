package jihuayu.test;

import jihuayu.mccoder.annotation.RegItem;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

@RegItem(value = "kkk",unLocalName = "ooo")
public class ItemA extends Item {
    public ItemA(){
        this.setCreativeTab(CreativeTabs.FOOD);
    }
}
