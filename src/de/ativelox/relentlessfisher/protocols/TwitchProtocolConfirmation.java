package de.ativelox.relentlessfisher.protocols;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is used to check whether a message from twitchs' IRC server is of
 * a specific type.
 * 
 * @author Ativelox {@literal<ativelox.dev@web.de>}.
 *
 */
public class TwitchProtocolConfirmation {

    /**
     * Checks whether <tt>toCheck</tt> is a successful connection message.
     * 
     * @param user    The user who connects to the server.
     * @param toCheck The message to check, whether it's a connection message.
     * @return <tt>True</tt> if the given message was a connection message,
     *         <tt>false</tt> otherwise.
     */
    public static final boolean IsConnect(final String user, final String toCheck) {
	final String wanted = ":tmi.twitch.tv 376 " + user + " :>";

	return toCheck.equals(wanted);
    }

    /**
     * Checks whether <tt>toCheck</tt> is a successful JOIN message.
     * 
     * @param user    The user who joins the channel.
     * @param toCheck The message to check, whether it's a JOIN message.
     * @return The name of the channel if the JOIN was successful, <tt>null</tt>
     *         otherwise.
     */
    public static final String IsJoin(final String user, final String toCheck) {
	Pattern pattern = Pattern.compile(
		"^:" + user + "\\.tmi\\.twitch\\.tv\\s366\\s" + user + "\\s#(\\w+)\\s:End\\sof\\s\\/NAMES\\slist$");
	Matcher matcher = pattern.matcher(toCheck);

	if (matcher.matches()) {
	    return matcher.group(1);

	}
	return null;
    }

    /**
     * Checks whether <tt>toCheck</tt> is a successful whisper message.
     * 
     * @param user    The user who received the whisper.
     * @param toCheck The message to check, whether it's a successful whisper.
     * @return A string array with exactly <tt>2</tt> entries, the first being the
     *         name of the sender of the whisper message, and the second being the
     *         contents of the message. <tt>null</tt> if the message wasn't a proper
     *         whisper message.
     */
    public static final String[] IsWhisper(final String user, final String toCheck) {
	Pattern pattern = Pattern
		.compile("^.*?:(\\w+)?\\!\\w+@\\w+\\.tmi.twitch.tv\\sWHISPER\\s" + user + "\\s:(.*)?$");
	Matcher matcher = pattern.matcher(toCheck);

	if (matcher.matches()) {
	    return new String[] { matcher.group(1), matcher.group(2) };

	}
	return null;
    }

    private TwitchProtocolConfirmation() {
    }
}
