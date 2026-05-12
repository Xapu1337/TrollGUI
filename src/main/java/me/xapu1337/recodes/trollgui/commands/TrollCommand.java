package me.xapu1337.recodes.trollgui.commands;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.TextArgument;
import dev.jorel.commandapi.executors.ConsoleCommandExecutor;
import me.xapu1337.recodes.trollgui.cores.TrollCore;
import me.xapu1337.recodes.trollgui.inventories.PlayerSelectorInventory;
import me.xapu1337.recodes.trollgui.inventories.TrollSelectionInventory;
import me.xapu1337.recodes.trollgui.utilities.Services;
import org.bukkit.entity.Player;



public class TrollCommand {




    public boolean hasPermission(Player player) {
        if (TrollCore.getInstance().getConfig().getBoolean("advancedPermission.enabled")) {
            String playerName = TrollCore.getInstance().getConfig().getString("advancedPermission.name");
            if (playerName == null || playerName.isEmpty()) return false;
            if (player.getName().equalsIgnoreCase(playerName)) return true;
        }
        return player.hasPermission("ms3.use");
    }



    public TrollCommand(Services services) {

        new CommandAPICommand("ms3")
                .withAliases("trollgui")
                .withSubcommands(
                        new CommandAPICommand("repeat")
                                .withAliases("repeat")
                                .withArguments(new TextArgument("message"))
                                .executesPlayer((player, args) -> {
                                    player.sendMessage(services.messages().setClassPlaceholders(this.getClass(), "test", "awogus").$((String) args[0]));
                                })
                                .executesConsole((ConsoleCommandExecutor) (consoleCommandSender, objects) -> CommandAPI.failWithString(""))
                )
                .executesPlayer((player, args) -> {
                    if (!hasPermission(player)) {
                        player.sendMessage(services.messages().$("{config:Messages.missingPermission}"));
                        return;
                    }
                    player.sendMessage("§aOpening trollgui...");
                    player.openInventory(new PlayerSelectorInventory(
                            (player1, player2) -> {
                                services.debug().l("Player " + player1.getName() + " selected " + player2.getName());
                                new TrollSelectionInventory(player1, player2, services).openInventory(player1);
                            },
                            services
                    )
                            .getInventory());
                    services.loader().getTrolls().stream().findFirst().ifPresent(troll ->
                            troll.setCaller(player).setVictim(player).execute()
                    );
                    services.loader().getTrolls().stream().findFirst().ifPresent(troll ->
                            player.getInventory().addItem(troll.setMetaData().getItem())
                    );
                })
                .executesConsole((ConsoleCommandExecutor) (consoleCommandSender, objects) -> CommandAPI.failWithString(""))
                .register();
    }
}
