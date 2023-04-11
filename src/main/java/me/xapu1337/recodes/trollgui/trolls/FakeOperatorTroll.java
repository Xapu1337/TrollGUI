package me.xapu1337.recodes.trollgui.trolls;

import com.cryptomorin.xseries.XMaterial;

import me.xapu1337.recodes.trollgui.types.Troll;
import me.xapu1337.recodes.trollgui.types.TrollMetaData;
import org.bukkit.ChatColor;

public class FakeOperatorTroll extends Troll {

    @Override
    public TrollMetaData setMetaData() {
        return (
                new TrollMetaData(XMaterial.COMMAND_BLOCK)
                        .setTrollName("fakeOperator")

        );
    }


    @Override
    public void execute() {
        if(!getVictim().isOp())
            getVictim().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&o[Server: Made " + getVictim().getName() + " a server operator]"));
        else
            getVictim().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&o[Server: Made " + getVictim().getName() + " no longer a server operator]"));
    }
}
