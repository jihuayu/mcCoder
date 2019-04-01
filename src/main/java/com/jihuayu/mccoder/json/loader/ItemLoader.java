package com.jihuayu.mccoder.json.loader;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jihuayu.mccoder.utils.FileUtil;
import com.jihuayu.mccoder.utils.ItemUtil;
import net.minecraft.item.Item;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ItemLoader {
    public static List<Item> getItems(){
        List<File> list = FileUtil.findScript(".item.json");
        List<Item> items = new ArrayList<>();
        list.forEach(i->{
            if(i!=null)
                items.add(getItemFromJson(i));
        });
        return items;
    }
    public static Item getItemFromJson(File file){
        String content= null;
        try {
            content = FileUtils.readFileToString(file,"UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        JsonObject item = new JsonParser().parse(content).getAsJsonObject();
        return ItemUtil.createItemFromJson(item);
    }
}
