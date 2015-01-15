package TheTimeless.game;

import TheTimeless.gui.VTextRender;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.tiled.TiledMap;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;



/**
 * Loads different resources
 */
public class Loader {
    Graphics g =new Graphics();
    private VTextRender TextRender=new VTextRender(20,"Sans");
            Map<String, Animation> Animations = new HashMap<String, Animation>();
    Map<String, Audio> Sounds = new HashMap<String, Audio>();
    ArrayList<String> strings=new ArrayList<String>();
    Map<String, Image> Smallpictures= new HashMap<String, Image>();
    public Loader() {
                setSprites();
        new Thread() {
            @Override
            public void start() {
                setSounds();
            }
        }.start();
        new Thread() {
            @Override
            public void start() {
getStrings();
            }
        }.start();
        new Thread() {
            @Override
            public void start() {
                getPicts();
            }
        }.start();

    }

    /**
     * Creates animations with files from folders and adds into Animations list
     */
    private void setSprites() {
        try {

            Map<String, Animation> anims = new HashMap<String, Animation>();
            String files[] = new File("data/sprites").list();//get folders' names
            for(String adr:files) {
             anims.put(adr, new Animation(getImages("data/sprites/" + adr), 200,false));//create animation and put into list
             Animations=anims;
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
         for(String adr:files) {
             if(adr.contains(".ogg")) {
                 Sounds.put(adr.substring(0, adr.indexOf(".ogg")),// get every ogg sound and write into list under name of file
                  AudioLoader.getAudio("OGG", new FileInputStream(new File("data/sounds/" + adr))));
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
        ArrayList<Image> imgs=new ArrayList<Image>();
        for (String str:files) {
            if(str.contains(".png"))
            imgs.add(new Image(adr+"/"+str));
        }
        Image images[]=new Image[imgs.size()];
        return imgs.toArray(images);
    }
    /**
     * Gets all strings for the level
     */
    protected void getStrings(){
        try {
            Scanner s = new Scanner(new File("data/strings/strings"));
            ArrayList<String> strList = new ArrayList<String>();
            while(s.hasNextLine()){
                String c = s.nextLine();
                strList.add(c);
            }
            strings=strList;
        } catch (FileNotFoundException ex) {
          ex.printStackTrace();
        }
    }
private void getPicts(){
    try {
        Map<String, Image> picts = new HashMap<String, Image>();
        String pictures[] = new File("data/strings/smallpictures").list();
        for (String str:pictures){
            if(str.contains(".png")) {
                picts.put(str.substring(0, str.indexOf(".png")),
                        new Image("data/strings/smallpictures/" + str));
            }
        }
        Smallpictures = picts;
    }catch(Exception e){
        e.printStackTrace();
    }
}

    public Animation getSprite(String index)
    {
        return Animations.get(index);
    }

    public void playSound(String index,int gain,int pitch,boolean repeat)
    {
         Sounds.get(index).playAsSoundEffect(gain,pitch,repeat);
    }
    public void playSound(String index,int gain,int pitch,boolean repeat,float x, float y, float z)
    {
        Sounds.get(index).playAsSoundEffect(gain,pitch,repeat,x,y,z);
    }
    public Audio getSound(String index) {return Sounds.get(index);}
    public String getString(int index)  {return strings.get(index);}


    /**
     * Draw string under given number
     */
    public void  renderString(int index) {
        try {
            g.setColor(new Color(0, 0, 0, 60));
            g.fillRect(2, Display.getHeight() - 80, Display.getWidth(), 80);//draw a background for message yb`
            String speaker = strings.get(index).substring(0, strings.get(index).indexOf(":"));//get speaker
            String value = strings.get(index).substring(strings.get(index).indexOf(":") + 1,
                    strings.get(index).length());//get what he said
            if (speaker.equals("Me")) {
                Smallpictures.get("Me").draw(5,Display.getHeight() - 79);
                TextRender.drawString(value, 75, Display.getHeight() - 40, Color.white);

            } else if (!speaker.equals("Me"))//draw on the left or right
            {
                Smallpictures.get(speaker).draw(Display.getWidth() - 5-64,Display.getHeight() - 79);
                TextRender.drawString(value,
                        Display.getWidth() - 75 - TextRender.getWidth(value),
                        Display.getHeight() - 40, Color.white);
                //draw text
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
            ArrayList<Entity> crts=new ArrayList<Entity>();
            for (int xAxis = 0; xAxis < map.getWidth(); xAxis++) {
                for (int yAxis = 0; yAxis < map.getHeight(); yAxis++) {
                    int tileID = map.getTileId(xAxis, yAxis, 3);//get id of block
                    String value = map.getTileProperty(tileID,"type","");//check the type of block with ID
                    Entity objs=null;
                    if (!("".equals(value))) {
                        if(value.equals("Table"))
                        {
                            Constructor c = Class.forName("TheTimeless.game.Table").getConstructors()[0];
                            objs = (Table)c.newInstance((xAxis -Display.getWidth() / 2 / 64) * 64,
                                    (yAxis - Display.getHeight() / 2 / 64) * 64,map.getTileProperty(tileID,"text",""));
                        }else {
                            Constructor c = Class.forName("TheTimeless.game." + value).getConstructors()[0];
                           objs = (Entity) c.newInstance((xAxis - Display.getWidth() / 2 / 64) * 64,
                                    (yAxis - Display.getHeight() / 2 / 64) * 64);//get the coords of th object
                        }
                        if(!(objs instanceof Creature))
                            crts.add(objs);
                        else throw new Exception("Wrong type:an object must be an TheTimeless.game.Entity, check your map");
                    }//if block has a type, create an object with the value of "type"
                }
            }
            return  crts;
        }catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
    }
    /**
     * Gets all mobs from a TiledMap.
     */
    public ArrayList<Creature> getCreatures(TiledMap map) {
        try {
            ArrayList<Creature> crts=new ArrayList<Creature>();
            for (int xAxis = 0; xAxis < map.getWidth(); xAxis++) {
                for (int yAxis = 0; yAxis < map.getHeight(); yAxis++) {
                    int tileID = map.getTileId(xAxis, yAxis, 2);//get id of block
                    String value = map.getTileProperty(tileID,"type","");//check the type of block with ID
                    if (!("".equals(value))) {
                        Constructor c=Class.forName("TheTimeless.game."+value).getConstructors()[0];
                        Creature objs=(Creature)c.newInstance((xAxis-Display.getWidth()/2/64)*64,
                                (yAxis-Display.getWidth()/2/64)*64);//get the coords of th object
                        crts.add(objs);
                    }//if block has a type, create an creature with the value of "type"
                }
            }
            return  crts;
        }catch(Exception ex){
            System.out.print("Wrong type:an enemy must be an TheTimeless.game.Creature, check your map");
            ex.printStackTrace();
            return null;
        }
    }

}