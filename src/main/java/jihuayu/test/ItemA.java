package jihuayu.test;

import jihuayu.mccoder.annotation.RegItem;
import jihuayu.mccoder.annotation.TMod;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

@RegItem(value = ItemA.o,unLocalName = "test:ooo1")
@RegItem(value = "Kpk1",unLocalName = "test:ooo2")
@RegItem(value = "Kpk2",unLocalName = "test:ooo3")
@TMod(modid = "test")
public class ItemA extends Item {
    public ItemA(){
        this.setCreativeTab(CreativeTabs.FOOD);
    }
    public static final String o ="kkkkp";
    @Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {
        I18n.hasKey("test:pppihhj");
        entityLiving.sendMessage(new TextComponentTranslation("test:pppp"));
        return super.onBlockDestroyed(stack, worldIn, state, pos, entityLiving);
    }
}
