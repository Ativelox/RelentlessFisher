package de.ativelox.relentlessfisher.listeners;

import de.ativelox.relentlessfisher.irc.AClient;

/**
 * Provides callbacks for part related actions.
 * 
 * @author Ativelox {@literal<ativelox.dev@web.de>}
 *
 */
public interface IPartListener {

    /**
     * Gets called when an implementation of {@link AClient} properly parts from the
     * <tt>channel</tt>. This listener should be registered using
     * {@link AClient#register(IPartListener)}
     * 
     * @param channel The channel from which got parted.
     */
    void onPart(final String channel);

}
