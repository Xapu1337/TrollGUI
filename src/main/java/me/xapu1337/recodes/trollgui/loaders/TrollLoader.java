package me.xapu1337.recodes.trollgui.loaders;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;
import me.xapu1337.recodes.trollgui.cores.TrollCore;
import me.xapu1337.recodes.trollgui.types.Troll;
import me.xapu1337.recodes.trollgui.utilities.SingletonBase;
import org.bukkit.Bukkit;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TrollLoader {

    private static final SingletonBase<TrollLoader> instance = new SingletonBase<>(TrollLoader.class);
    private final List<Troll> trolls = Collections.synchronizedList(new ArrayList<>());

    public TrollLoader() {
    }

    public synchronized void refreshTrolls() {
        TrollCore.getInstance().debuggingUtil.log("Starting to refresh trolls...");

        trolls.clear();

        try (ScanResult result = new ClassGraph()
                .enableAllInfo()
                .acceptPackages("me.xapu1337.recodes.trollgui")
                .scan()) {
            for (Class<?> clazz : result.getSubclasses("me.xapu1337.recodes.types.Troll").loadClasses()) {
               Troll troll = (Troll) clazz.getConstructor().newInstance();
               if (!trolls.contains(troll)) trolls.add(troll);
            }
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            TrollCore.getInstance().debuggingUtil.error("Error while refreshing trolls", e);
            Bukkit.getLogger().severe("Failed to fetch classes! (please report this to Ram#1337 on Discord)");
            throw new RuntimeException(e);
        }

        trolls.sort((a, b) -> a.getTrollMetaData().getTrollName().compareToIgnoreCase(b.getTrollMetaData().getTrollName()));
        TrollCore.getInstance().debuggingUtil.log("Finished refreshing trolls. Total trolls: " + trolls.size());
    }

    public List<Troll> getTrolls() {
        return Collections.unmodifiableList(trolls);
    }

    public static TrollLoader getInstance() {
        return instance.get();
    }
}
