package TheTimeless.gui;

import TheTimeless.game.Fonts;
import TheTimeless.game.WizardGame;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.openal.Audio;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Created by yasmidrog on 1/15/15.
 */
public class SavesList extends guiContainer {
    private ArrayList<guiButton> buttons=new ArrayList<guiButton>();
    private  int elem;
    private VTextRender Font=Fonts.regulartext;
    private WizardGame WGame;
    public SavesList(WizardGame game,final Menu menu){
        int y=150;
        final GameContainer cntr=game.getContainer();
        input=cntr.getInput();
        WGame=game;
        elem=0;
        final File[] file=new File("data/saves").listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                if(pathname.getName().contains(".ttws"))
                    return true;
                else return false;
            }
        });
        if(! (file==null)&&!(file.length==0)) {
            Arrays.sort(file, new Comparator() {
                public int compare(Object o1, Object o2) {
                    if (((File) o1).lastModified() > ((File) o2).lastModified()) {
                        return -1;
                    } else if (((File) o1).lastModified() < ((File) o2).lastModified()) {
                        return +1;
                    } else {
                        return 0;
                    }
                }
            });
            for (final File f:file) {
                if (f.getName().contains("ttws")) {
                    buttons.add(new guiButton(cntr, f.getName(), Font, 10, y) {
                        @Override
                        public void onClicked() {
                            WGame.load("data/saves/" + f.getName());
                            WGame.loaded = true;
                            exit(menu, cntr);
                        }
                    });
                    y += 30;
                }
            }
        }
        if(buttons.isEmpty()) {
            String str="There is no saves";
            buttons.add(new guiButton(cntr, str, Font,10, y){
                @Override
                public void onClicked() {
                    exit(menu,cntr);
                }
            });
            return;
            }
        buttons.add(new guiButton(cntr, "Exit", Font,10, y){
            @Override
            public void onClicked() {
                exit(menu,cntr);
            }
        });
    }
    public void render() {
        if (shown) {
            renderSaves();
        }
    }
    public void setSound(Audio a){
        mus=a;
    }
    public void remove(guiElement e) {
        buttons.remove(e);
    }
   private void renderSaves(){
       if (input.isKeyPressed(Input.KEY_UP)) {

           if (elementNumber != 0) {
               elementNumber--;
           } else {
               elementNumber = buttons.size() - 1;
               elem = buttons.size() - 1 - 24;
           }
           if (elementNumber < elem)
               elem--;
           if (mus != null)
               mus.playAsMusic(1, 1, false);
       }
       if (input.isKeyPressed(Input.KEY_DOWN)) {
           if (elementNumber < buttons.size() - 1) {
               elementNumber++;
           } else {
               elementNumber = 0;
           }
           elem = 0;
           if (mus != null)
               mus.playAsMusic(1, 1, false);
       }
       if (input.isKeyPressed(Input.KEY_ENTER)) {
           buttons.get(elementNumber).onClicked();
       }

       guiElement e = buttons.get(elementNumber);
       guiElement f = buttons.get(buttons.size() - 1);
       int w=0;
       for(guiElement el:buttons){
           if(el.getWidth()+110>w)
               w=(int)el.getWidth()+110;

       }
       graphics.setColor(Color.darkGray);

       graphics.setColor(new Color(0, 0, 0, 85));
       graphics.fillRect(buttons.get(0).getX() -20, buttons.get(0).getY() - 30,w, f.getY()
               - buttons.get(0).getY() + f.getHeight() + 60);
       graphics.setColor(Color.white);
       graphics.drawRect(e.getX() - 3, e.getY() - 3, e.getWidth() + 6, e.getHeight() + 6);


           for (guiElement element : buttons) {
               try {
                   if (element.getShown())
                       element.render();
               } catch (Exception ex) {
                   ex.printStackTrace();
               }
           }
   }
    protected void exit(Menu menu,GameContainer container){
        menu.setShown(true);
        menu.gui.setShown(true);
        this.shown=false;
    }
}

