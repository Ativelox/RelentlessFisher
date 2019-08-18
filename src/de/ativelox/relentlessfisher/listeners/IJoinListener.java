package de.ativelox.relentlessfisher.listeners;

import de.ativelox.relentlessfisher.irc.AClient;

/**
 * Provides callbacks for join related actions.
 * 
 * @author Ativelox {@literal<ativelox.dev@web.de>}
 *
 */
public interface IJoinListener {

    /**
     * Gets called when an implementation of {@link AClient} properly joins the
     * <tt>channel</tt>. This listener should be registered using
     * {@link AClient#register(IJoinListener)}
     * 
     * @param channel The channel which got joined.
     */
    void onJoin(final String channel);

}
