package TheTimeless.gui;

import TheTimeless.game.*;
import org.ietf.jgss.GSSManager;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.*;

import java.awt.*;
import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by yasmidrog on 1/15/15.
 */
public class Menu {
    public  guiContainer gui,params,saves;
    private GameContainer app;
    private guiButton Help;
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
        try {
            if (Shown) {
                if(Game.loaded&&app.getInput().isKeyPressed(Input.KEY_ESCAPE)) {
                    Shown = false;
                    app.setPaused(false);
                    Loader.playSound("click", 1, 1, false);
                }
                gui.render();
                params.render();
                if (list != null)
                    list.render();
                Help.render();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void setGui(){
        gui =new guiContainer(app);
        gui.setShown(true);
        params =new guiContainer(app);
        params.setShown(false);
        saves =new guiContainer(app);
        saves.setShown(false);
        Help=new guiButton(app,Loader.getString("Helptab"),
                mainFont,
                Display.getDesktopDisplayMode().getWidth()/2-200,
                Display.getDesktopDisplayMode().getHeight()/12){
            @Override
            public void render(){
                try {
                    if (shown) {
                        graphics.setColor(new org.newdawn.slick.Color(0, 0, 0, 230));
                        graphics.fillRect(getX() - 10, getY() - 10, getWidth() + 20, getHeight() + 20);
                        super.render();
                    }
                }catch (Exception e){e.printStackTrace();}
            }
        };
        Help.setShown(false);
        params.setSound(World.ResLoader.getSound("click"));
        gui.setSound(World.ResLoader.getSound("click"));
        gui.add(new guiButton(app,Loader.getString("Return"), mainFont,4,
                s_height/2-  mainFont.getHeight()/2-350){
            @Override
            public void onClicked(){
                if(Game.loaded) {
                    Shown = false;
                    app.setPaused(false);
                    Loader.playSound("click", 1, 1, false);
                }
            }
            @Override
            public void render(){
                if (Game.loaded) {
                    render.drawString( string, (int) rect.getX(), (int) rect.getY(), org.newdawn.slick.Color.white);
                }else{
                    render.drawString( string, (int) rect.getX(), (int) rect.getY(), org.newdawn.slick.Color.gray);
                }
            }
        });
        gui.add(new guiButton(app,Loader.getString("New"), mainFont,4,
                s_height/2-  mainFont.getHeight()/2-270){
            @Override
            public void onClicked(){
                try {
                    Shown = false;
                    Game.loaded=true;
                    app.setPaused(false);
                    Loader.playSound("click", 1, 1, false);
                    Game.newGame(app);
                    System.gc();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        Help.setLocation((int)Help.getX(),(int)(gui.ve.get(gui.ve.size()-1).getY()+40));
        gui.add(new guiButton(app,Loader.getString("Load"), mainFont,4,
                s_height/2-  mainFont.getHeight()/2-190){
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
        gui.add(new guiButton(app,Loader.getString("Save"), mainFont,4,
                s_height/2-  mainFont.getHeight()/2-110){
            @Override
            public void onClicked(){
                try {
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
            @Override
            public void render(){
                if (Game.loaded) {
                    render.drawString( string, (int) rect.getX(), (int) rect.getY(), org.newdawn.slick.Color.white);
                }else{
                    render.drawString( string, (int) rect.getX(), (int) rect.getY(), org.newdawn.slick.Color.gray);
                }
            }
        });
        gui.add(new guiButton(app,Loader.getString("Help"),  mainFont,4,
                s_height/2-  mainFont.getHeight()/2-30){
            @Override
            public void onClicked(){
               if(Help.getShown())
                   Help.setShown(false);
                else Help.setShown(true);
                Loader.playSound("click", 1, 1, false);
            }
        });
        gui.add(new guiButton(app,Loader.getString("Params"),  mainFont,4,
                s_height/2-  mainFont.getHeight()/2+50){
            @Override
            public void onClicked(){
                gui.setShown(false);
                params.setShown(true);
                Loader.playSound("click", 1, 1, false);
            }
        });

        gui.add(new guiButton(app,Loader.getString("Exit"), mainFont,4,
                s_height/2-  mainFont.getHeight()/2+130){
            @Override
            public void onClicked(){
                ConfigReader.setConfig("width", String.valueOf(Display.getWidth()));
                ConfigReader.setConfig("height", String.valueOf(Display.getHeight()));
                app.exit();
                Loader.playSound("click", 1, 1, false);
            }
        });
        params.add(new guiCheckBox(app,Loader.getString("Sound"), mainFont,4,
                s_height/2-  mainFont.getHeight()/2-220,app.isSoundOn()){
            @Override
            public void onClicked (){
                if (active) {
                    active=false;
                    ConfigReader.setConfig("sound", "false");
                    WizardGame.setParams(app);
                }else {
                    active = true;
                    ConfigReader.setConfig("sound", "true");
                    WizardGame.setParams(app);
                }
                Loader.playSound("click", 1, 1, false);
            }
        });
        guiElement e=params.ve.get(params.ve.size() - 1);
        params.add(new guiCheckBox(app,Loader.getString("FPS"), mainFont,4,
                (int)(e.getY() +e.getHeight()+30),app.isShowingFPS()){
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
        e=params.ve.get(params.ve.size() -  1);
        params.add(new guiButton(app, Loader.getString("Return"), mainFont, 4,
                (int)(e.getY() +e.getHeight()+30)) {
            @Override
            public void onClicked() {
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
