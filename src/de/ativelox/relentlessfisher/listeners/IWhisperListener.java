package de.ativelox.relentlessfisher.listeners;

import de.ativelox.relentlessfisher.irc.twitch.TwitchClient;

/**
 * Provides callbacks for whisper related events.
 * 
 * @author Ativelox {@literal<ativelox.dev@web.de>}
 *
 */
public interface IWhisperListener {

    /**
     * Gets called when {@link TwitchClient} receives a whisper from
     * <tt>sender</tt>. This listener should be registered using
     * {@link TwitchClient#register(IWhisperListener)}.
     * 
     * @param sender  The user that sent the whisper.
     * @param message The contents of the whisper.
     */
    void onWhisperReceived(final String sender, final String message);

}
