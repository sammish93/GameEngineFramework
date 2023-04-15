package no.hiof.samuelcd.tbage.interfaces;

/**
 * An interface intended to ensure that a game interface always has a method of elegantly closing on command.
 * @param <T> A generic type. A terminal requires a String, along with the command 'exit' passed to close.
 *           A Swing window requires the base jFrame to close the entire application window.
 */
public interface Closeable<T> {

    void close(T t);
}
