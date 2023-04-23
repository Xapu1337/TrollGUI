package me.xapu1337.recodes.trollgui.commands;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.CommandArgument;
import dev.jorel.commandapi.arguments.TextArgument;
import dev.jorel.commandapi.executors.ConsoleCommandExecutor;
import me.xapu1337.recodes.trollgui.cores.TrollCore;
import me.xapu1337.recodes.trollgui.inventories.PlayerSelectorInventory;
import me.xapu1337.recodes.trollgui.inventories.TrollSelectionInventory;
import me.xapu1337.recodes.trollgui.loaders.TrollLoader;
import me.xapu1337.recodes.trollgui.types.TrollMetaData;
import me.xapu1337.recodes.trollgui.utilities.ConfigUtils;
import me.xapu1337.recodes.trollgui.utilities.DebuggingUtil;
import me.xapu1337.recodes.trollgui.utilities.InventoryBuilder;
import me.xapu1337.recodes.trollgui.utilities.TempPool;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class TrollCommand {


    public boolean hasPermission(Player player) {
        if (TrollCore.getInstance().getConfig().getBoolean("advancedPermission.enabled")) {
            String playerName = TrollCore.getInstance().getConfig().getString("advancedPermission.name");
            if (playerName == null || playerName.isEmpty()) return false;
            if (player.getName().equalsIgnoreCase(playerName)) return true;
        }
        return player.hasPermission("ms3.use");
    }



    public TrollCommand() {

        new CommandAPICommand("ms3")
                .withAliases("trollgui")
                .withSubcommands(
                        new CommandAPICommand("repeat")
                                .withAliases("repeat")
                                .withArguments(new TextArgument("message"))
                                .executesPlayer((player, args) -> {
                                    player.sendMessage(ConfigUtils.getInstance().setClassPlaceholders(this.getClass(), "test", "awogus").$((String) args[0]));
                                })
                                .executesConsole((ConsoleCommandExecutor) (consoleCommandSender, objects) -> CommandAPI.failWithString(""))
                )
                .executesPlayer((player, args) -> {
                    if (!hasPermission(player)) {
                        player.sendMessage(ConfigUtils.getInstance().$("{config:Messages.missingPermission}"));
                        return;
                    }
                    player.sendMessage("§aOpening trollgui...");
                    UUID msg = UUID.randomUUID();
                    player.sendMessage("generated uuid: " + msg);
                    TempPool.getInstance().setMessage(msg, "§aOpening trollgui...");

                    player.sendMessage(ConfigUtils.getInstance().setClassPlaceholders(this.getClass(), "test", "awogus").$("OMG I CAN BELIEVE IT ITS &#8800FF&lAMONG US! {test} {config:menus.Clutches.clutches.water.lore} {VOID=" + msg + "}"));
                    player.openInventory(new PlayerSelectorInventory(
                            (player1, player2) -> {
                                DebuggingUtil.getInstance().l("Player " + player1.getName() + " selected " + player2.getName());
                                new TrollSelectionInventory(player1, player2).openInventory(player1);
                            }
                    )
                            .getInventory());
                    TrollLoader.getInstance().getTrolls().stream().findFirst().get().setCaller(player).setVictim(player).execute();
                    player.getInventory().addItem(TrollLoader.getInstance().getTrolls().stream().findFirst().get().setMetaData().getItem());
                })
                .executesConsole((ConsoleCommandExecutor) (consoleCommandSender, objects) -> CommandAPI.failWithString(""))
                .register();
    }
}
