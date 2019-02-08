# McCoder项目指南

## 试玩McCoder

- 在这里我们将带大家体会使用`McCoder`创建一个mod并添加物品的过程。

- 为工程添加`McCoder`及其依赖。(1+4)(4个依赖在libs文件夹中)

- 运行mc，至`run`中出现`copy succeed`字样后结束运行。

- 在资源文件夹新建文件`mccoder.properties`，写下如下内容：

```PROPERTIES
modid=testmod
mainClass=TestMod
mainPackage=jihuayu.testmod
```

- 在资源文件夹新建文件夹`mccoder`。

- 创建一个类`TestMod`作为主类，并为其打上`TMod`注解，如下：

```JAVA
@TMod(modid = "testmod")
public class ExampleMod{}
```

- 创建一个物品，并为其打上`RegItem`注解，如下：  

```JAVA
@RegItem(value = "ooo",unLocalName = "kkk")
public class ItemA extends Item {
    public ItemA(){
        this.setCreativeTab(CreativeTabs.FOOD);
    }
}
```

- 打开游戏，发现mod已经成功加载，物品已经成功注册。

- 如果没有成功请检查IDE是否开启`annotation processing`，下图为IDEA的打开位置：  
![](./pics/005.png)


- 如果编译失败或者没有达到效果，请进行重新全部编译(`Rebuild`)。

- 如果有其他问题可以给我发`issues`，或者加我的qq`8042833`

- `files`文件夹中有每天的程序归档;`libs`文件夹中运行所需要的库,请全部添加依赖。

## 使用自动本地化功能

- 在资源文件夹新建文件`mccoder.properties`，写下如下内容：

```PROPERTIES
modid=testmod
```

- 进入游戏，这时候本地化的词条已经生成在了`lang/en_us.lang`文件中


- 单独使用此功能请下载`withmixin`版本，或者加入依赖`mixin-0.7.11-SNAPSHOT.jar`，其他依赖可以不用。

- 此功能不需要开启`annotation processing`，如果没有添加全所有依赖，请不要开启`annotation processing`。

- **可以被自动生成的词条需要满足非本地化名字中带有`modid:`,请务必注意.**