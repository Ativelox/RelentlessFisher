package de.ativelox.relentlessfisher.timer;

/**
 * Provides an interface for classes wanting to receive callbacks from
 * implementations of {@link ITimer}.
 * 
 * @author Ativelox ({@literal ativelox.dev@web.de})
 *
 */
public interface ITimeoutListener {

    /**
     * Gets called when {@link ITimer#passed()} extends a given timeout.
     * 
     * @param ms The time that has passed since the start of {@link ITimer} and its
     *           timeout in milliseconds.
     */
    void onTimeout(final long ms);

}
