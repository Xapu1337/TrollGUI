package me.xapu1337.recodes.trollgui.utilities;

import me.xapu1337.recodes.trollgui.loaders.TrollLoader;
import org.bukkit.configuration.file.FileConfiguration;

public record Services(
        DebuggingUtil debug,
        MessageUtils messages,
        Utils utils,
        TrollToggablesStorage toggles,
        TrollLoader loader,
        FileConfiguration config) {
}
