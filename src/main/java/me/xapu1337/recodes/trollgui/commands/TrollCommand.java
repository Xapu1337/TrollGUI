package me.xapu1337.recodes.trollgui.commands;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.executors.ConsoleCommandExecutor;
import me.xapu1337.recodes.trollgui.cores.TrollCore;
import me.xapu1337.recodes.trollgui.utilities.ConfigUtils;
import me.xapu1337.recodes.trollgui.utilities.TempPool;
import org.bukkit.Bukkit;

import java.util.UUID;

public class trollcommand {

    public void TrollCommand() {
        new CommandAPICommand("troll")
                .withAliases("trollgui")
                .withPermission("trollgui.use")
                .executesPlayer((player, args) -> {
                    player.sendMessage("§aOpening trollgui...");
                    UUID msg = UUID.randomUUID();
                    TempPool.getInstance().setMessage(msg, "§aOpening trollgui...");
                    player.sendMessage(new ConfigUtils(TrollCore.getInstance()).setPlaceholder("test", "awogus").$("OMG I CAN BELIEVE IT ITS &l&#8800FFAMONG US! {test} {config:menus.Clutches.clutches.water.lore} {#" + msg + "})"));
                })
                .executesConsole((ConsoleCommandExecutor) (consoleCommandSender, objects) -> CommandAPI.failWithString(""))
                .register();
    }
}
