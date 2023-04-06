package me.xapu1337.recodes.trollgui.types;

import java.util.function.Consumer;

/**
 * Represents an operation that accepts three input arguments and returns no
 * result. This is the three-arity specialization of {@link Consumer}.
 *
 * @param <T> the type of the first input argument
 * @param <U> the type of the second input argument
 * @param <V> the type of the third input argument
 */
@FunctionalInterface
public interface TriConsumer<T, U, V> {

    /**
     * Performs this operation on the given arguments.
     *
     * @param t the first input argument
     * @param u the second input argument
     * @param v the third input argument
     */
    void accept(T t, U u, V v);
}
