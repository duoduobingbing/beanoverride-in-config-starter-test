package io.github.duoduobingbing.test.beanoverride.reflection.wrapper.helper;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

public class ExecutionHelper {

    private static final Logger logger = LoggerFactory.getLogger(ExecutionHelper.class);

    /**
     * Represents a supplier of results that may throw a RuntimeException
     *
     * <p>There is no requirement that a new or distinct result be returned each
     * time the supplier is invoked.</p>
     *
     * <p>This is a functional interface
     * whose functional method is {@link #getInternal()}.</p>
     *
     * @param <T> the type of results supplied by this supplier
     */
    public static interface ThrowingSupplier<T, E extends Throwable> extends Supplier<T> {


        @Override
        default T get() {
            try {
                return getInternal();
            } catch (Throwable e) {
                logger.trace("error while executing function", e);
                throw new RuntimeException(e);
            }
        }

        public T getInternal() throws E;
    }

    /**
     *  Represents an operation that does not return a result and may throw a RuntimeException
     *
     *  <p> This is a {@linkplain java.util.function functional interface}
     *  whose functional method is {@link #runInternal()}.</p>
     * @param <E>
     */
    public static interface ThrowingRunnable<E extends Throwable> extends Runnable {

        @Override
        default void run() {
            try {
                runInternal();
            } catch (Throwable e) {
                logger.trace("error while executing function", e);

                if(e instanceof RuntimeException re) throw re;

                throw new RuntimeException(e);
            }
        }

        public void runInternal() throws E;
    }

    /**
     *
     * @param throwingSupplier
     * @return
     * @param <T>
     * @param <E>
     */
    public static <T, E extends Throwable> T executeMayThrows(ThrowingSupplier<T, E> throwingSupplier) {
        return throwingSupplier.get();
    }

    public static <E extends Throwable> void executeMayThrows(ThrowingRunnable<E> throwingSupplier) {
        throwingSupplier.run();
    }

}
