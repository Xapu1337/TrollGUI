package me.xapu1337.recodes.trollgui.Utilities;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;
import me.xapu1337.recodes.trollgui.Cores.TrollCore;
import me.xapu1337.recodes.trollgui.Handlers.TemplateHandler;
import me.xapu1337.recodes.trollgui.Handlers.TrollHandler;
import me.xapu1337.recodes.trollgui.Inventorys.TrollGUI;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Singleton {
    private volatile static Singleton instance;
    private Singleton() {}
    public static Singleton getSingleInstance() {
        if (instance == null) {
            synchronized (Singleton.class) {
                if (instance == null) {
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }

    public final NamespacedKey trollItemNamespaceKey = new NamespacedKey(TrollCore.instance, "assigned-troll-class");

    public final IndexableMap<String, Player> clearedPlayerInventories = new IndexableMap<String, Player>();
    public final IndexableMap<String, Player> frozenPlayers = new IndexableMap<String, Player>();
    public final IndexableMap<String, Player> noBuildPlayers = new IndexableMap<String, Player>();
    public final IndexableMap<String, Player> noBreakPlayers = new IndexableMap<String, Player>();

    public final IndexableMap<String, Player> noDropPlayers = new IndexableMap<String, Player>();
    public final IndexableMap<String, Player> reverseMessagePlayers = new IndexableMap<String, Player>();

    // Simple map holding owners of currently open inventories.
    public final IndexableMap<Player, TrollGUI> currentPlayersTrolling = new IndexableMap<Player, TrollGUI>();

    public final IndexableMap<String, Player> vanishedPlayers = new IndexableMap<String, Player>();
    public final ScanResult scanResult = new ClassGraph().enableAllInfo().acceptPackages("me.xapu1337.recodes.trollgui").scan();
    public final ClassInfoList trollHandlerClasses = scanResult.getSubclasses("me.xapu1337.recodes.trollgui.Handlers.TrollHandler");
    public final IndexableMap<String, TrollHandler> loadedTrollHandlers = new IndexableMap<>();
    public final TemplateHandler generalTemplateHandler = new TemplateHandler();

    // This works for now, until mojang adds bigger chests this should be fine.
    public final List<Integer> doubleChestCenterSlots = Arrays.asList(10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43);
}
