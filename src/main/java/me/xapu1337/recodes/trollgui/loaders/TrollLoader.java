package me.xapu1337.recodes.trollgui.loaders;

import com.cryptomorin.xseries.XMaterial;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;
import me.xapu1337.recodes.trollgui.types.Troll;
import me.xapu1337.recodes.trollgui.types.TrollAttributes;
import me.xapu1337.recodes.trollgui.types.TrollMetaData;
import me.xapu1337.recodes.trollgui.types.TrollName;
import me.xapu1337.recodes.trollgui.utilities.DebuggingUtil;
import me.xapu1337.recodes.trollgui.utilities.Services;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

public class TrollLoader {

    private final DebuggingUtil debug;
    private final List<Troll> trolls = new CopyOnWriteArrayList<>();

    public TrollLoader(DebuggingUtil debug) {
        this.debug = debug;
    }

    private static final ClassGraph CLASS_GRAPH;

    static {
        CLASS_GRAPH = new ClassGraph()
                .enableAllInfo()
                .acceptPackages("me.xapu1337.recodes.trollgui");
    }

    public synchronized void refreshTrolls(Services services) {
        debug.l("Starting to refresh trolls...");

        trolls.clear();

        try (ScanResult result = CLASS_GRAPH.scan()) {
            List<ClassInfo> classInfos = result.getSubclasses("me.xapu1337.recodes.trollgui.types.Troll")
                    .getStandardClasses();
            debug.l("Found " + classInfos.size() + " troll classes");
            List<Troll> newTrolls = classInfos.stream()
                    .map(ClassInfo::loadClass)
                    .filter(Troll.class::isAssignableFrom)
                    .map(clazz -> {
                        try {
                            Troll troll = (Troll) clazz.getConstructor().newInstance();
                            troll.injectServices(services);

                            TrollMetaData metaData = troll.setMetaData();
                            if (metaData == null) {
                                TrollName annotation = clazz.getAnnotation(TrollName.class);
                                if (annotation == null) return null;

                                String trollName = annotation.value();
                                FileConfiguration config = services.config();
                                String basePath = "menus.troll-menu.items.trolls." + trollName;

                                String materialStr = config.getString(basePath + ".material", "BARRIER");
                                XMaterial material = XMaterial.matchXMaterial(
                                        materialStr != null ? materialStr : "BARRIER").orElse(XMaterial.BARRIER);

                                List<String> attrNames = config.getStringList(basePath + ".attributes");
                                TrollAttributes[] attrs = attrNames.stream()
                                        .map(name -> {
                                            try {
                                                return TrollAttributes.valueOf(name);
                                            } catch (IllegalArgumentException e) {
                                                Bukkit.getLogger().warning(
                                                        "[TrollGUI] Unknown attribute '" + name + "' for troll '" + trollName + "'");
                                                return null;
                                            }
                                        })
                                        .filter(Objects::nonNull)
                                        .toArray(TrollAttributes[]::new);

                                metaData = new TrollMetaData(material, services).setTrollName(trollName);
                                if (attrs.length > 0) metaData.setAttributes(attrs);
                            }

                            troll.setTrollMetaData(metaData);
                            return (Troll) troll.Init();
                        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                            debug.error("Error while instantiating troll", e, null);
                            throw new RuntimeException(e);
                        } catch (NoSuchMethodException ignored) {
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .toList();

            trolls.addAll(newTrolls);
            debug.l("Successfully refreshed trolls. Total trolls: " + trolls.size());
        } catch (RuntimeException e) {
            debug.error("Error while refreshing trolls", e, null);
            Bukkit.getLogger().severe("Failed to fetch classes! (please report this to Ram#1337 on Discord)");
            e.printStackTrace();
        }

        trolls.sort(
                (a, b) -> a.getTrollMetaData().getTrollName().compareToIgnoreCase(b.getTrollMetaData().getTrollName()));
        debug.l("Finished refreshing trolls. Total trolls: " + trolls.size());
    }

    public List<Troll> getTrolls() {
        return Collections.unmodifiableList(trolls);
    }
}
