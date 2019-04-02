package com.jihuayu.mccoder.event;

import com.google.gson.JsonObject;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class LoadEvent<T extends IForgeRegistryEntry<T>> {
    private T obj;
    private JsonObject json;

    public LoadEvent(T obj, JsonObject json) {
        this.obj = obj;
        this.json = json;
    }

    public T getLoad(){
        return obj;
    }

    public JsonObject getJson() {
        return json;
    }
}
