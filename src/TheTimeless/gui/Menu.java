package TheTimeless.gui;

import TheTimeless.game.*;
import org.ietf.jgss.GSSManager;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.GameContainer;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by yasmidrog on 1/15/15.
 */
public class Menu {
    public  guiContainer gui,params,saves;
    private GameContainer app;
    private VTextRender mainFont;
    private WizardGame Game;
    private Menu thism=this;
    private SavesList list;
    public boolean Shown=false;
    private int s_height=Display.getDesktopDisplayMode().getHeight();
    public  Menu(WizardGame game,GameContainer cntr){
       mainFont = Fonts.mainFont;
        app=cntr;
        Game=game;
        setGui();

    }
    public void render()
    {
        if(Shown) {
            gui.render();
            params.render();
            if(list!=null)
            list.render();
        }
    }
    private void setGui(){
        gui =new guiContainer(app);
        gui.setShown(true);
        params =new guiContainer(app);
        params.setShown(false);
        saves =new guiContainer(app);
        saves.setShown(false);
        params.setSound(World.ResLoader.getSound("click"));
        gui.setSound(World.ResLoader.getSound("click"));
        gui.add(new guiButton(app,"Resume", mainFont,4,
                s_height/2-  mainFont.getHeight()/2-300+30){
            @Override
            public void onClicked(){
               Shown=false;
                app.setPaused(false);
                Loader.playSound("click", 1, 1, false);
            }
        });

        gui.add(new guiButton(app,"Load", mainFont,4,
                s_height/2-  mainFont.getHeight()/2-150+30){
            @Override
            public void onClicked(){
                try {
                   gui.setShown(false);
                    list=new SavesList(Game,thism);
                    list.setShown(true);
                    Loader.playSound("click", 1, 1, false);
                }catch(Exception e){
e.printStackTrace();
                }
            }
        });
        gui.add(new guiButton(app,"Save", mainFont,4,
                s_height/2-  mainFont.getHeight()/2+30){
            @Override
            public void onClicked(){
                try {

                    Shown=false;
                    app.setPaused(false);
                    Date d = new Date();
                    SimpleDateFormat format1 = new SimpleDateFormat("dd_MM_yyyy_hhmmss");
                    Game.save("data/saves/world_lev." + Game.Level + "_" + format1.format(d) + ".ttws");
                    Loader.playSound("click", 1, 1, false);
                    list=new SavesList(Game,thism);
                    list.setShown(false);
                }catch(Exception e){
e.printStackTrace();
                }
            }
        });

        gui.add(new guiButton(app,"Parameters",  mainFont,4,
                s_height/2-  mainFont.getHeight()/2+150+30){
            @Override
            public void onClicked(){
                gui.setShown(false);
                params.setShown(true);
                Loader.playSound("click", 1, 1, false);
            }
        });


        gui.add(new guiButton(app,"Exit", mainFont,4,
                s_height/2-  mainFont.getHeight()/2+300+30){
            @Override
            public void onClicked(){
                ConfigReader.setConfig("width", String.valueOf(Display.getWidth()));
                ConfigReader.setConfig("height", String.valueOf(Display.getHeight()));
                app.exit();
                Loader.playSound("click", 1, 1, false);
            }
        });
        params.add(new guiCheckBox(app,"FPS", mainFont,4,
                s_height/2-  mainFont.getHeight()/2-150+30,app.isShowingFPS()){
            @Override
            public void onClicked (){
                if (active) {
                    active=false;
                    ConfigReader.setConfig("fps", "false");
                    WizardGame.setParams(app);
                }else {
                    active = true;
                    ConfigReader.setConfig("fps", "true");
                    WizardGame.setParams(app);
                }
                Loader.playSound("click", 1, 1, false);
            }
        });
        params.add(new guiButton(app,"Return", mainFont,4,
                s_height/2-  mainFont.getHeight()/2- mainFont.getHeight()/2-10+30){
            @Override
            public void onClicked(){
                params.setShown(false);
                gui.setShown(true);
                Loader.playSound("click", 1, 1, false);
            }
        });
    }
    public void setShown(boolean show){
        Shown=show;
    }
    public boolean isShown(){
        return Shown;
    }
}
