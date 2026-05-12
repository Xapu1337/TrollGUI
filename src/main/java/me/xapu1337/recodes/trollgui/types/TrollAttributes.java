package me.xapu1337.recodes.trollgui.types;

import me.xapu1337.recodes.trollgui.utilities.MessageUtils;

public enum TrollAttributes {
    POSSIBLE_CRASH_OR_FREEZE("{config:menus.troll-menu.extras.canCrash}"),
    POSSIBLE_DEATH_OR_ITEM_LOSS("{config:menus.troll-menu.extras.canBeDeadlyOrLoseItems}"),
    POSSIBLE_DESTRUCTION("{config:menus.troll-menu.extras.canDestroyBlocks}"),
    POSSIBLE_KICK("{config:menus.troll-menu.extras.canKickPlayer}");

    private final String configKey;

    TrollAttributes(String configKey) {
        this.configKey = configKey;
    }

    public String getAttributeLore(MessageUtils messages) {
        return messages.$(configKey);
    }
}