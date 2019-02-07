package jihuayu.mccoder.builder;

import jihuayu.mccoder.Env;

public class AllBuilder {
    public static void build(){
        Env.note("begin build");
        MainClassBuilder.build();
        ClientProxyBuilder.build();
        CommonProxyBuilder.build();
        ItemLoaderBuilder.build();
    }
}
