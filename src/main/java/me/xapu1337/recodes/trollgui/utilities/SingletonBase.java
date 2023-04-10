package me.xapu1337.recodes.trollgui.utilities;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

public class SingletonBase<T> {
    private volatile T instance;

    public SingletonBase(Class<T> clazz) {
        Objects.requireNonNull(clazz, "Class cannot be null");
        if (instance == null) {
            synchronized (SingletonBase.class) {
                if (instance == null) {
                    try {
                        Constructor<T> constructor = clazz.getDeclaredConstructor();
                        constructor.setAccessible(true);
                        instance = constructor.newInstance();
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public T get() {
        return instance;
    }

    public static <T> T getInstance() {
        return (T) SingletonBaseHolder.instance.get();
    }

    private static class SingletonBaseHolder {
        private static final SingletonBase<?> instance = new SingletonBase<>(Object.class);

        public static <T> T get(Class<T> clazz) {
            return clazz.cast(instance.get());
        }
    }
}
