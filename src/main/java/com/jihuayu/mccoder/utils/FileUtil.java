package com.jihuayu.mccoder.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {
    private static final String JS_DIR = "./mccoder";
    public static List<File> findScript(String end){
        List<File> list = new ArrayList<>();
        File file = new File(JS_DIR);
        if(!file.exists()){
            file.mkdir();
        }
        if(file.isDirectory()){
            for(File i : file.listFiles()){
                if(!i.isDirectory()&&i.getName().endsWith(end)){
                    list.add(i);
                }
            }
        }
        return list;
    }
}
