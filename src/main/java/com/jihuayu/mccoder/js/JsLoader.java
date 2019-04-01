package com.jihuayu.mccoder.js;

import com.jihuayu.mccoder.utils.FileUtil;

import javax.script.ScriptException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class JsLoader {


    public static void loadScript() {
        List<File> list = FileUtil.findScript(".js");
        list.forEach(i->{
            try {
                JsEngine.engine.eval(new FileReader(i));
            } catch (ScriptException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });
    }
    public static void main(String[]args){
        loadScript();
    }
}
