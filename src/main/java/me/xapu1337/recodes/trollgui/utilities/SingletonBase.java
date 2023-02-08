package me.xapu1337.recodes.trollgui.utilities;

import java.util.function.Supplier;

public class SingletonBase<T> implements Supplier<T> {
    private volatile T instance;
    private final Class<T> type;

    public SingletonBase(Class<T> type) {
        this.type = type;
    }

    @Override
    public T get() {
        if (instance == null) {
            synchronized (type) {
                if (instance == null) {
                    try {
                        instance = type.newInstance();
                    } catch (InstantiationException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return instance;
    }
}
