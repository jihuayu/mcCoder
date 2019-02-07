import org.lwjgl.Sys;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;


public class Init {
    public static String mainPath;
    public static void main(String[] args){

        try {
            File file = new File(System.getProperty("user.home").replace("\\","/")+"/.mccoder");
            if(!file.exists()) {
                file.mkdir();
            }
            Properties props = new Properties();
            file = new File(System.getProperty("user.dir"));
            String path = find(file)+"/main/resources";
            mainPath = path;
            path = path.replace("\\","/");
            URL url = new URL("file:///" + path+"/mccoder.properties");
            props.load(url.openStream());
            String modid = props.getProperty("modid");
            if(modid!=null){
                copyDir(path+"/mccoder",System.getProperty("user.home").replace("\\","/")+"/.mccoder/"+modid);
                copyFile(path+"/mccoder.properties",System.getProperty("user.home").replace("\\","/")+"/.mccoder/"+modid+"/mccoder.properties");
            }
            System.out.println("copy succeed");
        } catch (IOException e) {
            System.out.println("please set the lib in the project path.");
        }


    }
    private static String find(File file){
        if(file==null)return "";
        if(!file.isDirectory())
            find(file.getParentFile());
        else{
            File[] files = file.listFiles();
            for(File i : files){
                if(i.getName().equals("src"))
                    return i.getAbsolutePath().replace("\\","/");
            }
        }
        return find(file.getParentFile());
    }
    public static void copyDir(String sourcePath, String newPath) throws IOException {
        File file = new File(sourcePath);
        String[] filePath = file.list();
        if (!(new File(newPath)).exists()) {
            (new File(newPath)).mkdir();
        }

        for (int i = 0; i < filePath.length; i++) {
            if ((new File(sourcePath + "/" + filePath[i])).isDirectory()) {
                copyDir(sourcePath  + "/"  + filePath[i], newPath  + "/" + filePath[i]);
            }
            if (new File(sourcePath  + "/" + filePath[i]).isFile()) {
                copyFile(sourcePath + "/" + filePath[i], newPath + "/" + filePath[i]);
            }

        }
    }
    public static void copyFile(String oldPath, String newPath) throws IOException {
        File oldFile = new File(oldPath);
        File file = new File(newPath);
        FileInputStream in = new FileInputStream(oldFile);
        FileOutputStream out = new FileOutputStream(file);;
        byte[] buffer=new byte[2097152];
        int readByte = 0;
        while((readByte = in.read(buffer)) != -1){
            out.write(buffer, 0, readByte);
        }
        if(oldFile.getName().equals("mccoder.properties")){
            byte[] buff = ("\nresPath="+mainPath).getBytes();
            out.write(buff,0,buff.length);
        }
        in.close();
        out.close();
    }
}
