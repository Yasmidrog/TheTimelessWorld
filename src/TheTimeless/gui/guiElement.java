package TheTimeless.gui;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Shape;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class guiElement {

        private boolean currentFocus = false;
        protected Input input=new Input(Display.getHeight());
        protected GameContainer container;
        protected Shape rect;
        public Image img;
        public String string="";
        protected boolean shown=true;
        protected Graphics graphics=new Graphics();
        protected VTextRender render=new VTextRender(16,"Sans");
       protected  javax.swing.Timer listener=new Timer(5, new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               if(mouseClicked())
                   onClicked();
           }
       });
        public void render()throws SlickException {

        }
        public void onClicked() {

        }

    public float getX() {return rect.getX();}

    public float getY() {return rect.getY();}

    public float getWidth(){return rect.getWidth();}

    public float getHeight(){return rect.getHeight();}

    public boolean getShown(){
     return shown;
 }

    public boolean hasFocus() {

        if(rect.contains(input.getMouseX(), input.getMouseX()))
            return true; else return false;
    }
    protected boolean mouseClicked()
    {
        if(rect.contains(input.getMouseX(),input.getMouseY())&&input.isMousePressed(0)) {
            return true;
        }
        else {return false;}
    }
    public void setShown(boolean show){
        this.shown=show;
    }
    public void addListener(ActionListener al){
        listener.addActionListener(al);
    }
    public void removeListener(ActionListener al){
        try {
            listener.removeActionListener(al);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
