package me.xapu1337.recodes.trollgui.Enums;

import me.xapu1337.recodes.trollgui.Utilities.Utilities;

public enum TrollAttributes {
    POSSIBLE_CRASH_OR_FREEZE(Utilities.getSingleInstance().getConfigPath("MenuItems.trollMenu.extras.canCrash")),
    POSSIBLE_DEATH_OR_ITEM_LOSS(Utilities.getSingleInstance().getConfigPath("MenuItems.trollMenu.extras.canBeDeadlyOrLoseItems")),
    POSSIBLE_DESTRUCTION(Utilities.getSingleInstance().getConfigPath("MenuItems.trollMenu.extras.canDestroyBlocks")),

    POSSIBLE_KICK(Utilities.getSingleInstance().getConfigPath("MenuItems.trollMenu.extras.canKickPlayer"));

    private final String ATTRIBUTE_LORE;

    TrollAttributes(String configValue) {
        this.ATTRIBUTE_LORE = configValue;
    }

    public String getAttributeLore() {
        return ATTRIBUTE_LORE;
    }
}
