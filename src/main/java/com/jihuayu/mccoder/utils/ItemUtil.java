package com.jihuayu.mccoder.utils;

import com.google.gson.JsonObject;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class ItemUtil {
    public static Item createItemFromJson(JsonObject object){
        Item.Properties properties = new Item.Properties();
        properties.group(ItemGroup.FOOD);
        Item item = new Item(properties);
        String id = object.get("id").getAsString();
        if(id!=null)
            item.setRegistryName(id);
        return item;
    }
}
