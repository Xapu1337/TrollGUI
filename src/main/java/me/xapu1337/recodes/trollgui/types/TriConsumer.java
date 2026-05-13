package me.xapu1337.recodes.trollgui.types;

import java.util.function.Consumer;

@FunctionalInterface
public interface TriConsumer<T, U, V> {
    void accept(T t, U u, V v);
}
