package TheTimeless.gui;

import org.newdawn.slick.*;
import org.newdawn.slick.geom.Rectangle;


public class guiButton extends guiElement {


    public guiButton(GameContainer cntr, String str, VTextRender vr, int x, int y) {
        this.container = cntr;
        string=str;
        shown=true;
        this.render=vr;
        input = container.getInput();
        rect=new Rectangle(x,y,vr.getWidth(str),vr.getHeight());
        setLocation(x, y);
        listener.start();
    }
        public guiButton(GameContainer container, Image mg, int x, int y) {
            this.container = container;
            img=mg;
            input = container.getInput();
            rect=new Rectangle(x,y,img.getWidth(),img.getHeight());

            setLocation(x, y);
            listener.start();
        }

        public void render() throws SlickException{
            if(shown) {
                if (string == "")
                    img.draw(rect.getX(), rect.getY());
                else render.drawString( string, (int) rect.getX(), (int) rect.getY(),Color.white);
            }
        }


        public void setLocation(int x, int y){
            rect.setX(x);
            rect.setY(y);
        }




}


