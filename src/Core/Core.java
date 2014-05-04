package Core;

import CaveBot.CaveBot;
import CaveBot.Looter;
import GUI.GUI;
import Healer.Healer;
import Misc.Misc;

public class Core {

    private static Healer healer;
    private static CaveBot caveBot;
    private static Misc misc;

    private static long curTime;
    private static long oldTime;
    private final long cycleDelay = 1;

    private boolean setBPS = false;
    private static int totalBackpacks = 4;

    /*
     Constructor where we initialize timing variables and objects
     */
    public Core() {
        healer = Healer.getInstance();
        caveBot = CaveBot.getInstance();
        misc = Misc.getInstance();

        //initialize timing variables
        curTime = System.currentTimeMillis();
        oldTime = System.currentTimeMillis();
    }

    /*
     Here is the main loop of the whole application. The heart.
     */
    public void cycle() {
        curTime = System.currentTimeMillis();

        //Every 10ms we cycle and do the actions below
        if (curTime - oldTime > cycleDelay && !GUI.isPaused) {
            //update the timing
            oldTime = curTime;

            //make sure we gave the user enough time to tab back to the game
            if (System.currentTimeMillis() - GUI.healStartTime > GUI.startDelay) {
                if (!setBPS) {
                    setBPS();
                }
                if (GUI.highHealCheck.isSelected() || GUI.lowHealCheck.isSelected() || GUI.manaRestoreCheck.isSelected()) {
                    healer.heal();
                }
                if (GUI.lootCheck.isSelected()) {
                    Looter.getInstance().findLoot();
                }

            }
            //do the misc things
            misc.doMisc();
            
            //check to see if hunting is started and that the user has had
            //enough time to tab back to the game. then cycle through hunting bot
            //actions
            if (GUI.huntingStarted && System.currentTimeMillis() - GUI.botStartTime > GUI.startDelay) {
                if (!GUI.caveBotIsPaused) {
                    caveBot.bot();
                }
            }
        }
    }

    /*
     Set the bps for the cavebot looter
     */
    private void setBPS() {

        //Check the bp selects and set the states.
        if (GUI.foodBPSelector.getSelectedIndex() != 6 && GUI.arrowBPSelector.getSelectedIndex() != 6) {
            totalBackpacks = 4;
        }
        if (GUI.foodBPSelector.getSelectedIndex() == 6 ^ GUI.arrowBPSelector.getSelectedIndex() == 6) {
            totalBackpacks = 5;
        }
        if (GUI.foodBPSelector.getSelectedIndex() == 6 && GUI.arrowBPSelector.getSelectedIndex() == 6) {
            totalBackpacks = 6;
        }
        setBPS = true;
    }

    /*
     Returns the number of backpacks in use.
     */
    public static int returnNumberOfBackpacks() {
        return totalBackpacks;
    }
}
