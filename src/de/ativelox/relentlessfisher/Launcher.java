package de.ativelox.relentlessfisher;

import java.io.IOException;

import de.ativelox.relentlessfisher.irc.twitch.TwitchClient;
import de.ativelox.relentlessfisher.listeners.IConnectionListener;
import de.ativelox.relentlessfisher.listeners.IJoinListener;
import de.ativelox.relentlessfisher.listeners.IWhisperListener;
import de.ativelox.relentlessfisher.settings.Settings;

/**
 * Starts a new {@link TwitchClient} and {@link RelentlessFisher}. Also manages
 * listener registering.
 * 
 * @author Ativelox {@literal<ativelox.dev@web.de>}
 *
 */
public class Launcher {

    public static void main(final String[] args) throws IOException {
	final Settings s = new Settings();
	s.load("settings.cfg");

	TwitchClient client = new TwitchClient(s.get("client_id"), s.get("client_secret"), s.get("refresh_token"),
		s.get("user"));
	RelentlessFisher rf = new RelentlessFisher(client);
	client.register((IConnectionListener) rf);
	client.register((IJoinListener) rf);
	client.register((IWhisperListener) rf);

	client.run();
    }

}
