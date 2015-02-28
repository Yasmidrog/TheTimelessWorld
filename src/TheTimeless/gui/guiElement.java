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
        public String string="";
        protected boolean shown=true,active=true;
        protected Graphics graphics=new Graphics();
        protected VTextRender render;
       protected  void update(){
               if(mouseClicked()&&active)
                   onClicked();
           }
        public void render()throws SlickException {
            update();
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

}
