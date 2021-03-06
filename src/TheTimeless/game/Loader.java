package TheTimeless.game;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.*;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.tiled.TiledMap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


/**
 * Loads different resources
 */
public class Loader {
    static String Locale="en_EN";
    Graphics g = new Graphics();
    Map<String, Animation> Animations = new HashMap<String, Animation>();
    static Map<String, Sound> Sounds = new HashMap<String, Sound>();
    static ArrayList<String> DialogStrings = new ArrayList<String>();
    static ArrayList<String> enDialogStrings = new ArrayList<String>();
    static Map<String, String> Strings = new HashMap<String, String>();
    static Map<String, String> enStrings = new HashMap<String, String>();
     Map<String, Image> Smallpictures = new HashMap<String, Image>();
    public  Loader() {
        setSprites();
        new Thread() {
            @Override
            public void start() {
                setSounds();
            }
        }.start();
        getPicts();
    }

    /**
     * Creates animations with files from folders and adds into Animations list
     */
    private void setSprites() {
        try {

            Map<String, Animation> anims = new HashMap<String, Animation>();
            String files[] = new File("data/sprites").list();//get folders' names
            for (String adr : files) {
                anims.put(adr, new Animation(getImages("data/sprites/" + adr), 200, false));//create animation and put into list
                Animations = anims;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Adds every audio from folder into list
     */
    private void setSounds() {
        try {
            String files[] = new File("data/sounds").list();
            for (String adr : files) {
                if (adr.contains(".ogg")) {
                    Sounds.put(adr.substring(0, adr.indexOf(".ogg")),// get every ogg sound and write into list under name of file
                                new Sound("data/sounds/" + adr));
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Gets an array of images to create an animation
     */
    protected org.newdawn.slick.Image[] getImages(String adr) throws SlickException {
        String files[] = new File(adr).list();
        ArrayList<Image> imgs = new ArrayList<Image>();
        for (String str : files) {
            if (str.contains(".png"))
                imgs.add(new Image(adr + "/" + str));
        }
        Image images[] = new Image[imgs.size()];
        return imgs.toArray(images);
    }

    /**
     * Gets all strings fir the current locale and for english locale
     */
    static public  void setStrings() {
        try {
            Scanner s = new Scanner(new File("data/dialogs/"+Locale+"/strings"),"UTF-8");
            ArrayList<String> strList = new ArrayList<String>();

            while (s.hasNextLine()) {
               String  c = s.nextLine();
               strList.add(c);
            }
            DialogStrings = strList;
            //get dialogs in the given locale
            s = new Scanner(new File("data/strings/"+Locale+"/strings"),"UTF-8");
             Map<String, String> strings = new HashMap<String, String>();
           String c ="";
            while (s.hasNextLine()) {
              c+= s.nextLine()+"\n";
            }
            for(String str:c.split(";\n"))
            {
                String sr[]=str.split(":");
                String Short =sr[0];
                String Full = sr[1];
                strings.put(Short, Full);
            }
            Strings=strings;
            if(enDialogStrings.isEmpty()||enStrings.isEmpty())
            setEnglish();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    private static void setEnglish() {
        try{
        Scanner s = new Scanner(new File("data/strings/en_EN/strings"),"UTF-8");
            Map<String, String> strings = new HashMap<String, String>();
            String c ="";
            while (s.hasNextLine()) {
                c+= s.nextLine()+"\n";
            }
            for(String str:c.split(";\n"))
            {
                String sr[]=str.split(":");
                String Short =sr[0];
                String Full = sr[1];
                strings.put(Short, Full);
            }
        enStrings = strings;
        s = new Scanner(new File("data/dialogs/en_EN/strings"),"UTF-8");
        ArrayList<String> strList = new ArrayList<String>();
        while (s.hasNextLine()) {
            String st = s.nextLine();
            strList.add(st);
        }
        enDialogStrings = strList;
    }catch(Exception e){e.printStackTrace();}
    }
     public static String getString(String desc){
         if(Strings.containsKey(desc))
         return Strings.get(desc);
         else return enStrings.get(desc);
     }
     private void getPicts() {
        try {
            Map<String, Image> picts = new HashMap<String, Image>();
            String pictures[] = new File("data/dialogs/smallpictures").list();
            for (String str : pictures) {
                if (str.contains(".png")) {
                    picts.put(str.substring(0, str.indexOf(".png")),
                            new Image("data/dialogs/smallpictures/" + str));
                }
            }
            Smallpictures = picts;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Animation getSprite(String index) {
        return Animations.get(index);
    }
    /*
    public static void playMusic(String index, int gain, int pitch, boolean repeat) {
        Sounds.get(index).playAsMusic(gain,pitch,repeat);
    }
    */
    public static void playSound(String index, int volume, int pitch, boolean repeat) {
        if(!repeat)
        Sounds.get(index).play(pitch,volume);
        else  Sounds.get(index).loop(pitch, volume);
    }

    public static  void playSound(String index, int volume, int pitch, float x, float y, float z) {
        Sounds.get(index).playAt(volume,pitch,x,y,z);
    }

    public Sound getSound(String index) {
        return Sounds.get(index);
    }

    public String getDialogString(int index) {
        return DialogStrings.get(index);
    }


    /**
     * Draw string under given number
     */
    public void renderString(int index) {
        try {
            int swidth=Display.getDesktopDisplayMode().getWidth();
            int sheight=Display.getDesktopDisplayMode().getHeight();
            g.setColor(new Color(0, 0, 0, 60));
            g.fillRect(2, sheight - 140, swidth, 140);//draw a background for message yb`
            String speaker = DialogStrings.get(index).substring(0, DialogStrings.get(index).indexOf(":"));//get speaker
            String value = DialogStrings.get(index).substring(DialogStrings.get(index).indexOf(":") + 1,
                    DialogStrings.get(index).length());//get what he said
            if (speaker.equals("Me")) {
                Smallpictures.get("Me").draw(10, sheight - 130);
                Fonts.TextRender.drawString(value, 80, sheight - 115, Color.white);

            } else if (!speaker.equals("Me"))//draw on the left or right
            {
                Smallpictures.get(speaker).draw(swidth - 5 - 125, sheight- 130);
                Fonts.TextRender.drawString(value,
                        swidth -5-130 - Fonts.TextRender.getWidth(value),
                        sheight - 115, Color.white);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets all static objects from a TiledMap.
     */
    public ArrayList<Entity> getObjects(TiledMap map) {
        try {

            ArrayList<Entity> crts = new ArrayList<Entity>();
            for (int xAxis = 0; xAxis < map.getWidth(); xAxis++) {
                for (int yAxis = 0; yAxis < map.getHeight(); yAxis++) {
                    int tileID = map.getTileId(xAxis, yAxis, 3);//get id of block
                    String value = map.getTileProperty(tileID, "type", "");//check the type of block with ID
                    Entity objs = null;
                    if (!("".equals(value))) {
                        Constructor c = Class.forName("TheTimeless.game." + value).getConstructors()[0];
                        objs = (Entity) c.newInstance(((xAxis - (Display.getDesktopDisplayMode().getWidth() / 2 / 64)) * 64) + 27,
                                ((yAxis - (Display.getDesktopDisplayMode().getHeight() / 2 / 64)) * 64) +27);//get the coords of th object
                        if (!(objs instanceof Creature)) {
                            crts.add(objs);
                        }
                        else
                            throw new Exception("Wrong type:an object must be an TheTimeless.game.Entity, check your map");
                    }//if block has a type, create an object with the value of "type"
                }
            }
            crts.addAll(getScriptsAndTables(map));
            return crts;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Gets all mobs from a TiledMap.
     */
    public ArrayList<Creature> getCreatures(TiledMap map) {
        try {
            ArrayList<Creature> crts = new ArrayList<Creature>();
            for (int xAxis = 0; xAxis < map.getWidth(); xAxis++) {
                for (int yAxis = 0; yAxis < map.getHeight(); yAxis++) {
                    int tileID = map.getTileId(xAxis, yAxis, 2);//get id of block
                    String value = map.getTileProperty(tileID, "type", "");//check the type of block with ID
                    if (!("".equals(value))) {
                        Constructor c = Class.forName("TheTimeless.game." + value).getConstructors()[0];
                        Creature objs = (Creature) c.newInstance((xAxis -  Display.getDesktopDisplayMode().getWidth() / 2 / 64) * 64+64,
                                (yAxis - Display.getDesktopDisplayMode().getHeight() / 2 / 64) * 64+64);//get the coords of th object
                        crts.add(objs);
                    }//if block has a type, create an creature with the value of "type"
                }
            }

            return crts;
        } catch (Exception ex) {
            System.out.print("Wrong type:an enemy must be an TheTimeless.game.Creature, check your map");
            ex.printStackTrace();
            return null;
        }

    }
private ArrayList<Entity> getScriptsAndTables(TiledMap map){
    ArrayList<Entity> crts = new ArrayList<Entity>();
    int count=map.getObjectCount(0);

    try {
        for (int i = 0; i < count; i++) {
            if(map.getObjectType(0, i).equals("Table")) {
                Constructor c = Class.forName("TheTimeless.game.Table").getConstructors()[0];
                crts.add((Entity) c.newInstance((map.getObjectX(0, i) - Display.getDesktopDisplayMode().getWidth() / 2 - 32),
                        ((map.getObjectY(0, i) - Display.getDesktopDisplayMode().getHeight() / 2) - 32),
                        map.getObjectProperty(0, i, "text", "hello.txt")));
            }
            if(map.getObjectType(0, i).equals("Lava")){
                crts.add( new Lava(map.getObjectX(0, i) - Display.getDesktopDisplayMode().getWidth() / 2 +27,
                        ((map.getObjectY(0, i) - Display.getDesktopDisplayMode().getHeight() / 2) +27),
                        map.getObjectWidth(0, i), map.getObjectHeight(0, i)));
            }
            if(map.getObjectType(0, i).equals("ScriptActivator")){
                crts.add(new ScriptActivator(
                        (map.getObjectX(0, i) - Display.getDesktopDisplayMode().getWidth() / 2) +27,
                        (map.getObjectY(0, i) - Display.getDesktopDisplayMode().getHeight() / 2) +27,
                        map.getObjectWidth(0, i), map.getObjectHeight(0, i),
                        map.getObjectProperty(0,i,"script","test")));
            }
        }
    }catch(Exception e){
        e.printStackTrace();
    }
    return crts;
    }
}