package me.xapu1337.recodes.trollgui.utilities;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

public class SingletonBase<T> {
    private final T instance;

    public SingletonBase(Class<T> clazz) {
        Objects.requireNonNull(clazz, "Class cannot be null");
        try {
            Constructor<T> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            instance = constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException
                | NoSuchMethodException e) {
            throw new RuntimeException("Failed to create singleton instance of " + clazz.getName(), e);
        }
    }

    public T get() {
        return instance;
    }
}
