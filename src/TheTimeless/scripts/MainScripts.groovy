package TheTimeless.scripts

import TheTimeless.game.ScriptActivator
import TheTimeless.game.World

/**
 * Created by yasmidrog on 27.03.15.
 */
class MainScripts {
    static World crWorld;
    public static def test(){
        try {
          crWorld.startSpeaking(6)
        }
      catch (Exception e){
          e.printStackTrace()
      }
    }
}
