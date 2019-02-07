package jihuayu.test;

import jihuayu.mccoder.annotation.RegItem;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

@RegItem(value = "kkk1",unLocalName = "ooo1")
@RegItem(value = "kkk",unLocalName = "ooo")
@RegItem(value = "kkk3",unLocalName = "ooo2")
public class ItemA extends Item {
    public ItemA(){
        this.setCreativeTab(CreativeTabs.FOOD);
    }
}
