package me.xapu1337.recodes.trollgui.Trolls;

import com.cryptomorin.xseries.XMaterial;
import me.xapu1337.recodes.trollgui.Enums.TrollAttributes;
import me.xapu1337.recodes.trollgui.Handlers.TemplateHandler;
import me.xapu1337.recodes.trollgui.Handlers.TrollHandler;
import me.xapu1337.recodes.trollgui.Types.TrollItemMetaData;

public class DebugTroll extends TrollHandler {



    @Override
    public TrollItemMetaData setMetaData() {
        return (
                new TrollItemMetaData()
                        .setItem(XMaterial.TNT)
                        .setDisplayName("§cDebug Troll")
                        .setAttributes(TrollAttributes.POSSIBLE_DESTRUCTION, TrollAttributes.POSSIBLE_DEATH_OR_ITEM_LOSS, TrollAttributes.POSSIBLE_CRASH_OR_FREEZE)
        );
    }

    @Override
    public void execute() {
        TemplateHandler.getInstance().dumpObject(caller.getDisplayName());
        TemplateHandler.getInstance().dumpMethod(caller, "getName");
        TemplateHandler.getInstance().printValues();
        System.out.println(TemplateHandler.getInstance().parseString("hello caller ${caller.getName}"));
    }


    /**
     * TODO:
     * FINISHED:
     *
     * NEEDS TESTING:
     * - Clutch Menu
     * - Dimension Menu
     * - GiveAllBad effects
     * - Lava bed
     * - No Drop
     * - Posted Cringe
     * - Swap players
     * - Send message as player
     * - Void TP
     *
     * FEATURES TO ADD:
     * - Vanish
     * - Random Player Selection (Command or GUI) AND Random Troll Selection (GUI Item)
     * - feature commands like gamemode, weather, etc.
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *
     */
}
