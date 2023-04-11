package me.xapu1337.recodes.trollgui.loaders;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;
import me.xapu1337.recodes.trollgui.cores.TrollCore;
import me.xapu1337.recodes.trollgui.types.Troll;
import me.xapu1337.recodes.trollgui.utilities.DebuggingUtil;
import me.xapu1337.recodes.trollgui.utilities.SingletonBase;
import org.bukkit.Bukkit;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class TrollLoader {

    private static final SingletonBase<TrollLoader> instance = new SingletonBase<>(TrollLoader.class);
    private final List<Troll> trolls = new CopyOnWriteArrayList<>();

    private static final ClassGraph CLASS_GRAPH;

    static {
        CLASS_GRAPH = new ClassGraph()
                .enableAllInfo()
                .acceptPackages("me.xapu1337.recodes.trollgui");
    }

    public synchronized void refreshTrolls() {
        DebuggingUtil.getInstance().l("Starting to refresh trolls...");

        trolls.clear();

        try (ScanResult result = CLASS_GRAPH.scan()) {
            List<ClassInfo> classInfos = result.getSubclasses("me.xapu1337.recodes.trollgui.types.Troll").getStandardClasses();
            DebuggingUtil.getInstance().l("Found " + classInfos.size() + " troll classes");
            List<Troll> newTrolls = classInfos.stream()
                    .map(ClassInfo::loadClass)
                    .filter(Troll.class::isAssignableFrom)
                    .map(clazz -> {
                        try {
                            return (Troll) clazz.getConstructor().newInstance();
                        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                            throw new RuntimeException(e);
                        } catch (NoSuchMethodException ignored) {
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .toList();

            trolls.addAll(newTrolls);
            DebuggingUtil.getInstance().l("Successfully refreshed trolls. Total trolls: " + trolls.size());
        } catch (RuntimeException e) {
            DebuggingUtil.getInstance().error("Error while refreshing trolls");
            Bukkit.getLogger().severe("Failed to fetch classes! (please report this to Ram#1337 on Discord)");
            e.printStackTrace();
        }

        trolls.sort((a, b) -> a.getTrollMetaData().getTrollName().compareToIgnoreCase(b.getTrollMetaData().getTrollName()));
        DebuggingUtil.getInstance().l("Finished refreshing trolls. Total trolls: " + trolls.size());
    }

    public List<Troll> getTrolls() {
        return Collections.unmodifiableList(trolls);
    }

    public static TrollLoader getInstance() {
        return instance.get();
    }
}
