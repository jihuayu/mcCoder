package jihuayu.testmod;

import jihuayu.mccoder.annotation.RegItem;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

@RegItem("jjx")
public class p extends Item {
    public int a =1;
    public void a(){
        if(a==1)
            return;
        a=2;
        return ;
    }
}
