package TheTimeless.scripts

import TheTimeless.game.Upgrade
import TheTimeless.game.*;
import TheTimeless.game.World
import TheTimeless.gui.Shop

/**
 * Created by yasmidrog on 27.03.15.
 */
class MainScripts {
   static World crWorld;
    public static def test(){
        try {

            crWorld.SpMn.addAvialibleAbility(new Upgrade("Health upgrade","Increase health by 50",
                                                                      10,Upgrade.UpgradeType.GOD){
                @Override
                public void apply(Spudi sp){
                   sp.MAXHEALTH=300;
                }
            },"Health upgrade")
          crWorld.startSpeaking(6)

        }
      catch (Exception e){
          e.printStackTrace()
      }
    }
}
