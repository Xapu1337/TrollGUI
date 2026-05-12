package me.xapu1337.recodes.trollgui.utilities;

import me.xapu1337.recodes.trollgui.loaders.TrollLoader;

public record Services(
        DebuggingUtil debug,
        MessageUtils messages,
        Utils utils,
        TrollToggablesStorage toggles,
        TrollLoader loader) {
}
