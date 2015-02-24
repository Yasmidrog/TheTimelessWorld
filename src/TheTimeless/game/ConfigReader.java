package TheTimeless.game;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by yasmidrog on 1/10/15.
 */
public class ConfigReader {
    static  Map<String,String> configs=new HashMap<String, String>();
     public  ConfigReader()
    {
        getConfigs();
    }
    static  protected  void  getConfigs() {
        try {
            Scanner s = new Scanner(new File("configs/main.conf"));
            while (s.hasNextLine()) {
                String c = s.nextLine();
                String[] params;
                if (c.contains(" ")) {
                    params = c.split(" ");
                    configs.put(params[0], params[1]);
                }
            }

        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }
    static  public String getConf(String conf){
        if(configs.containsKey(conf))
        return configs.get(conf);
        else return "null";
    }
    static  public void setConfig(String key,String conf){
        if(configs.containsKey(key))
        {
            configs.remove(key);
        }
        configs.put(key, conf);
        String text= "";
        File file = new File("configs/main.conf");
        for(Map.Entry<String, String> entry : configs.entrySet()) {
            text+=entry.getKey()+" "+entry.getValue()+"\n";
        }
        try {
                file.delete();
                file.createNewFile();
            try (PrintWriter out = new PrintWriter(file.getAbsoluteFile())) {
                //Записываем текст у файл
                out.print(text);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
