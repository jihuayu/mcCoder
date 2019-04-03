package com.jihuayu.mccoder.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class CItem {
    private String id = null;
    private String group = null;

    public Item build(){
        Item.Properties properties = new Item.Properties();
        if(group!=null&&group.equals("food"))
            properties.group(ItemGroup.FOOD);
        Item item = new Item(properties);
        if(id==null)return null;
        item.setRegistryName(id);
        return item;
    }
}
