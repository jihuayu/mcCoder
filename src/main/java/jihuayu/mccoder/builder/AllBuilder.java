package jihuayu.mccoder.builder;

public class AllBuilder {
    public static void build(){
        MainClassBuilder.build();
        ClientProxyBuilder.build();
        CommonProxyBuilder.build();
        ItemLoaderBuilder.build();
    }
}
