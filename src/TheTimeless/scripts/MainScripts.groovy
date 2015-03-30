package TheTimeless.scripts

import TheTimeless.game.Upgrade
import TheTimeless.game.*;
import TheTimeless.game.World

/**
 * Created by yasmidrog on 27.03.15.
 */
class MainScripts {
   static World crWorld;
    public static def test(){
        try {
            crWorld.SpMn.addAvialibleAbility(new Upgrade("Helth upgrade","Increase health by 50",
                                                                "data/sprites/Health/health.png"){
                @Override
                public void apply(Spudi sp){
                   sp.MAXHEALTH=300;
                }
            },"maxhealth")
            crWorld.SpMn.applyAndRemoveAbility("maxhealth");
          crWorld.startSpeaking(6)

        }
      catch (Exception e){
          e.printStackTrace()
      }
    }
}
