package de.ativelox.relentlessfisher.listeners;

import de.ativelox.relentlessfisher.irc.AClient;

/**
 * Provides callbacks for connection related actions.
 * 
 * @author Ativelox {@literal<ativelox.dev@web.de>}
 *
 */
public interface IConnectionListener {

    /**
     * Gets called when an implementation of {@link AClient} properly connects to
     * its server. This listener should be registered using
     * {@link AClient#register(IConnectionListener)}.
     */
    void onConnection();

}
