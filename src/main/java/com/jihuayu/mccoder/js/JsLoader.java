package com.jihuayu.mccoder.js;

import com.jihuayu.mccoder.McCoder;
import com.jihuayu.mccoder.utils.FileUtil;

import javax.script.ScriptException;
import javax.swing.plaf.PanelUI;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class JsLoader {

    public static void a(String a){
        McCoder.LOGGER.warn(a);
    }

    public static void loadScript() {
        List<File> list = FileUtil.findScript(".js");
        list.forEach(i->{
            try {

                JsEngine.engine.eval(new FileReader(i));
                McCoder.LOGGER.info(i.getName());
            } catch (ScriptException e) {
                McCoder.LOGGER.error(e.getMessage());
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                McCoder.LOGGER.error(e.getMessage());
                e.printStackTrace();
            }
        });
    }
//    public static void main(String[]args){
//        loadScript();
//    }
}
