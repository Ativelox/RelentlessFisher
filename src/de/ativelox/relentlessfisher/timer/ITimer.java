package de.ativelox.relentlessfisher.timer;

import java.util.Collection;

/**
 * Provides an interface for timers, being able to call
 * {@link ITimeoutListener#onTimeout(long)} when a given timeout is reached on
 * all it's listeners.
 * 
 * @author Ativelox ({@literal ativelox.dev@web.de})
 *
 */
public interface ITimer extends Runnable {

    /**
     * Adds the given {@link ITimeoutListener} to receive the callback
     * {@link ITimeoutListener#onTimeout(long) onTimeout(long)}.
     * 
     * @param timeoutListener The listener to start receiving callbacks.
     * @return As specified by {@link Collection#add(Object)}
     */
    boolean add(final ITimeoutListener timeoutListener);

    /**
     * The time that has passed since the start of this timer and it's timeout, in
     * milliseconds.
     * 
     * @return The passed time (ms).
     */
    long passed();

    /**
     * Removes the given {@link ITimeoutListener} such that it no longer receives
     * callbacks.
     * 
     * @param timeoutListener The listener to stop receiving callbacks.
     * @return As specified by {@link Collection#remove(Object)}
     */
    boolean remove(final ITimeoutListener timeoutListener);

    /**
     * Resets this timer, behaves the same as creating a new {@link ITimer}
     * instance, given the timeout hasn't yet been reached.
     */
    void reset();

}
