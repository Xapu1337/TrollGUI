package me.xapu1337.recodes.trollgui.types;

import me.xapu1337.recodes.trollgui.cores.TrollCore;
import me.xapu1337.recodes.trollgui.utilities.ConfigUtils;

public enum TrollAttributes {
    POSSIBLE_CRASH_OR_FREEZE(ConfigUtils.getInstance().$("{config:menus.troll-menu.extras.canCrash}")),
    POSSIBLE_DEATH_OR_ITEM_LOSS(ConfigUtils.getInstance().$("{config:menus.troll-menu.extras.canBeDeadlyOrLoseItems}")),
    POSSIBLE_DESTRUCTION(ConfigUtils.getInstance().$("{config:menus.troll-menu.extras.canDestroyBlocks}")),

    POSSIBLE_KICK(ConfigUtils.getInstance().$("{config:menus.troll-menu.extras.canKickPlayer}"));

    private final String ATTRIBUTE_LORE;

    TrollAttributes(String configValue) {
        this.ATTRIBUTE_LORE = configValue;
    }

    public String getAttributeLore() {
        return ATTRIBUTE_LORE;
    }
}