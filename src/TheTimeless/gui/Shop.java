package TheTimeless.gui;

import TheTimeless.game.*;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;


import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Map;


public class Shop extends guiElement {
    private Upgrade.UpgradeType Tab;
    public Spudi Hero;
    private int X,Y;
    private ArrayList<UpgradeButton> GodUpgrades;
    private ArrayList<UpgradeButton> DevilUpgrades;
    public Shop(Spudi hero,GameContainer cntr,int x, int y){
        this.container = cntr;
        input = cntr.getInput();
        X=x;
        Y=y;
        Tab= Upgrade.UpgradeType.GOD;
        Hero=hero;
        setShown(true);
    }
    @Override
    public  void render(){
        if(shown){
            Graphics g=container.getGraphics();
            Fonts.TableText.drawString(Loader.getString("DarkTab"), X + 20, Y + 5, Color.red);
            Fonts.TableText.drawString(Loader.getString("LightTab"),X+20+300+45,Y+5,Color.cyan);
            g.setColor(Color.darkGray);
            g.drawRect(X, Y, 700, 700);
            g.setColor(new Color(0, 0, 0, 80));
            g.fillRect(X, Y, 700, 700);
            try {
                for (UpgradeButton u : DevilUpgrades) {
                    u.render();
                }
                for (UpgradeButton u : GodUpgrades) {
                    u.render();
                }
            }catch (ConcurrentModificationException ignored){}
        }
    }
    public void getUpgrades(){
        GodUpgrades=new ArrayList<UpgradeButton>();
        DevilUpgrades=new ArrayList<UpgradeButton>();
        int sy=Y+70;
        try {
            for (Map.Entry<String, Upgrade> entry : Hero.getUpgrades().entrySet()) {
                if (entry.getValue().getType() == Upgrade.UpgradeType.DEVIL) {
                    DevilUpgrades.add(new UpgradeButton(entry.getValue(),
                            World.ResLoader.getSprite("BoowLeft").getImage(0).getScaledCopy(64,64)
                            , X + 10, sy, container,this));
                } else if (entry.getValue().getType() == Upgrade.UpgradeType.GOD) {
                    GodUpgrades.add(new UpgradeButton(entry.getValue(),
                            World.ResLoader.getSprite("BoowLeft").getImage(0).getScaledCopy(64,64),
                            X + 10 + 300 + 60, sy, container,this));
                }
                sy+=80;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void setShown(boolean show){
        super.setShown(show);
        getUpgrades();
    }
    public ArrayList<UpgradeButton> getGodUpgrades() {
        return GodUpgrades;
    }

    public ArrayList<UpgradeButton> getDevilUpgrades() {
        return DevilUpgrades;
    }
}
