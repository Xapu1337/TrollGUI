package me.xapu1337.recodes.trollgui.Utilities;

import com.cryptomorin.xseries.XMaterial;
import me.xapu1337.recodes.trollgui.Cores.Core;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

public class EnumCollection {


    String getStr(String path){
        Core.instance.getServer().getLogger().warning(path);
        Core.instance.getServer().getLogger().warning(Core.instance.translateColorCodes(Core.instance.getConfig().getString(path)));
        return Core.instance.translateColorCodes(Core.instance.getConfig().getString(path));
    }

    public enum TrollGUIItems {
        BURN_PLAYER(getStr("MenuItems.trollMenu.burnPlayer.name"), getStr("MenuItems.trollMenu.burnPlayer.lore")),
        CLOSE_GUI(getStr("MenuItems.trollMenu.closeGUI.name"), getStr("MenuItems.trollMenu.closeGUI.lore")),
        DROP_ALL(getStr("MenuItems.trollMenu.dropAll.name"), getStr("MenuItems.trollMenu.dropAll.lore")),
        DROP_ITEM(getStr("MenuItems.trollMenu.dropItem.name"), getStr("MenuItems.trollMenu.dropItem.lore")),
        EXPLODE_PLAYER(getStr("MenuItems.trollMenu.explodePlayer.name"), getStr("MenuItems.trollMenu.explodePlayer.lore")),
        FAKE_BLOCK(getStr("MenuItems.trollMenu.fakeBlock.name"), getStr("MenuItems.trollMenu.fakeBlock.lore")),
        FAKE_CLEAR(getStr("MenuItems.trollMenu.fakeClear.name"), getStr("MenuItems.trollMenu.fakeClear.lore")),
        FAKE_OPERATOR(getStr("MenuItems.trollMenu.fakeOperator.name"), getStr("MenuItems.trollMenu.fakeOperator.lore")),
        FREEZE_PLAYER(getStr("MenuItems.trollMenu.freezePlayer.name"), getStr("MenuItems.trollMenu.freezePlayer.lore")),
        INV_SEE(getStr("MenuItems.trollMenu.invSee.name"), getStr("MenuItems.trollMenu.invSee.lore")),
        NO_BUILD(getStr("MenuItems.trollMenu.noBuild.name"), getStr("MenuItems.trollMenu.noBuild.lore")),
        NO_PLACE(getStr("MenuItems.trollMenu.noPlace.name"), getStr("MenuItems.trollMenu.noPlace.lore")),
        RANDOM_LOOK(getStr("MenuItems.trollMenu.randomLook.name"), getStr("MenuItems.trollMenu.randomLook.lore")),
        REVERSE_MESSAGE(getStr("MenuItems.trollMenu.reverseMessage.name"), getStr("MenuItems.trollMenu.reverseMessage.lore")),
        SCARE_PLAYER(getStr("MenuItems.trollMenu.scarePlayer.name"), getStr("MenuItems.trollMenu.scarePlayer.lore")),
        THUNDER(getStr("MenuItems.trollMenu.thunder.name"), getStr("MenuItems.trollMenu.thunder.lore")),
        ENABLED(getStr("MenuItems.trollMenu.extras.isEnabled"), getStr("MenuItems.trollMenu.extras.isEnabled")),
        DISABLED(getStr("MenuItems.trollMenu.extras.isDisabled"), getStr("MenuItems.trollMenu.extras.isDisabled"));

        private final String name, lore;

        TrollGUIItems(String name, String lore) {
            this.name = name;
            this.lore = lore;
        }

        public String getName() {
            return name;
        }

        public String getLore() {
            return lore;
        }

    }


    public enum SettingsItems {
        RELOAD(getStr("MenuItems.settingsMenu.reload.name"), getStr("MenuItems.settingsMenu.reload.lore"));

        private final String name, lore;

        SettingsItems(String name, String lore) {
            this.name = name;
            this.lore = lore;
        }

        public String getName() {
            return name;
        }

        public String getLore() {
            return lore;
        }

    }

    public enum Messages {
        NO_PERMISSIONS(getStr("Messages.noPermissions")),
        MISSING_PERMISSIONS(getStr("Messages.missingPermissions")),
        SETTINGS(getStr("Messages.settings"));

        String string;

        public String get() {
            return this.string;
        }

        public void set(String string) {
            this.string = string;
        }

        Messages(String inp){
            this.string = inp;
        }

    }


    public enum MenuTitles {
        TROLLGUI(getStr("MenuTitles.trollGUI")),
        SELECT_PLAYER(getStr("MenuTitles.selectPlayer")),
        RELOADED(getStr("MenuTitles.reloaded")),
        NO_ITEM_IN_HAND(getStr("MenuTitles.noPermissions"));

        String string;

        public String get() {
            return this.string;
        }

        public void set(String string) {
            this.string = string;
        }

        MenuTitles(String inp){
            this.string = inp;
        }

    }


    public enum Variables {
        PREFIX(getStr("Variables.prefix")),
        IS_OP(getStr("MenuItems.playerSelector.extra.isOP"));

        String string;

        public String get() {
            return this.string;
        }

        public void set(String string) {
            this.string = string;
        }

        Variables(String inp){
            this.string = inp;
        }
    }





}
