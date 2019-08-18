package de.ativelox.relentlessfisher.irc.twitch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.ativelox.relentlessfisher.irc.AClient;
import de.ativelox.relentlessfisher.irc.IIRCController;
import de.ativelox.relentlessfisher.listeners.IConnectionListener;
import de.ativelox.relentlessfisher.listeners.IJoinListener;
import de.ativelox.relentlessfisher.listeners.IWhisperListener;
import de.ativelox.relentlessfisher.logging.ELogType;
import de.ativelox.relentlessfisher.protocols.TwitchProtocolConfirmation;
import de.ativelox.relentlessfisher.utils.HTTPRequest;

/**
 * Provides an implementation for an {@link AClient IRCClient}, which uses a
 * {@link UnsecuredTwitchIRCController} to control its behavior. Allows
 * registering of listeners, and provides callbacks for them accordingly.
 * 
 * @author Ativelox {@literal<ativelox.dev@web.de>}
 *
 * @see {@link IConnectionListener}
 * @see {@link IJoinListener}
 * @see {@link IPartListener}
 * @see {@link IWhisperListener}
 */
public class TwitchClient extends AClient {

    /**
     * The key that is given in a JSON object referring to the access token for the
     * oauth2 authentification for this application.
     */
    private final static String ACCESS_TOKEN_IDENTIFIER = "access_token";

    /**
     * The user having granted authorization to be controlled by this application.
     */
    private final String mUser;

    /**
     * The refresh token, which is used to refresh your access token. More can be
     * found here: <a href=
     * "https://dev.twitch.tv/docs/authentication/#refreshing-access-tokens">https://dev.twitch.tv/docs/authentication/#refreshing-access-tokens</a>
     */
    private final String mRefreshToken;

    /**
     * The client id, which is uniquely assigned to every twitch-application. Used
     * to identify this application.
     */
    private final String mClientId;

    /**
     * The client secret, which is used in refreshing access tokens.
     */
    private final String mClientSecret;

    /**
     * All the objects wanting to receive callbacks when the
     * {@link TwitchClient#mUser user} gets a whisper.
     */
    private final List<IWhisperListener> mWhisperListeners;

    /**
     * Creates a new {@link TwitchClient}.
     * 
     * @param clientId     The ID used to uniquely identify this application.
     *                     Provided by Twitch.
     * @param clientSecret The secret used to refresh access tokens. Provided by
     *                     Twitch.
     * @param refreshToken The refresh token, used to refresh access tokens.
     *                     Provided by Twitch when registering this application for
     *                     an oauth token.
     * @param user         The user which granted authorization to be controlled by
     *                     this application.
     */
    public TwitchClient(final String clientId, final String clientSecret, final String refreshToken,
	    final String user) {
	super(new UnsecuredTwitchIRCController(user));

	mUser = user;

	mRefreshToken = refreshToken;
	mClientId = clientId;
	mClientSecret = clientSecret;

	mWhisperListeners = new ArrayList<>();
    }

    /**
     * Gets (and refreshes) the access token needed to log onto twitch.
     * 
     * @return The access token.
     * @throws IOException When the access token couldn't properly get fetched.
     */
    private String getAccessToken() throws IOException {
	return HTTPRequest
		.Post("https://id.twitch.tv/oauth2/token?grant_type=refresh_token&refresh_token=" + mRefreshToken
			+ "&client_id=" + mClientId + "&client_secret=" + mClientSecret, "")
		.get(ACCESS_TOKEN_IDENTIFIER);

    }

    /**
     * Joins a given channel.
     * 
     * @param channel The channel to join.
     * 
     * @see {@link IIRCController#JOIN(String, String)}
     */
    public void join(final String channel) {
	mController.JOIN(channel, "");

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.ativelox.relentlessfisher.irc.AClient#onServerMessageReceived(java.lang.
     * String)
     */
    @Override
    public void onServerMessageReceived(final String serverMessage) {
	super.onServerMessageReceived(serverMessage);

	if (TwitchProtocolConfirmation.IsConnect(mUser, serverMessage)) {
	    mLogger.log(ELogType.INFO, "Got successful connection confirmation.");

	    for (final IConnectionListener listener : mConnectionListeners) {
		listener.onConnection();

	    }
	    return;
	}

	final String possibleChannel = TwitchProtocolConfirmation.IsJoin(mUser, serverMessage);

	if (possibleChannel != null) {
	    mLogger.log(ELogType.INFO, "Got channel confirmation for: " + possibleChannel);

	    for (final IJoinListener listener : mJoinListeners) {
		listener.onJoin(possibleChannel);
	    }
	    return;

	}

	final String[] possibleWhisper = TwitchProtocolConfirmation.IsWhisper(mUser, serverMessage);

	if (possibleWhisper != null) {
	    for (final IWhisperListener listener : mWhisperListeners) {
		listener.onWhisperReceived(possibleWhisper[0], possibleWhisper[1]);
	    }

	}
    }

    /**
     * Registers the given listener to receive
     * {@link IWhisperListener#onWhisperReceived(String, String)} callbacks.
     * 
     * @param listener The listener to receive callbacks.
     */
    public void register(final IWhisperListener listener) {
	mWhisperListeners.add(listener);

    }

    /*
     * (non-Javadoc)
     * 
     * @see de.ativelox.relentlessfisher.irc.AClient#run()
     */
    @Override
    public void run() {
	String access_token = null;
	try {
	    mLogger.log(ELogType.INFO, "Fetching access token...");
	    access_token = this.getAccessToken();
	    mLogger.log(ELogType.INFO, "Done.");

	} catch (final IOException e1) {
	    mLogger.log(ELogType.DANGER, "Could not get an access token.");
	    return;

	}

	int reconnectTries = 0;

	// exponential reconnect increase, as to not over-strain the server.
	while (!mController.connect(access_token)) {
	    try {
		Thread.sleep(1000 * (int) Math.pow(2, reconnectTries));
	    } catch (InterruptedException e) {
		mLogger.log(ELogType.WARNING, "Client got interrupted while sleeping.");
	    }
	    reconnectTries++;
	}
	reconnectTries = 0;

	while (true) {
	    mController.read();

	    try {
		// TODO: properly handle sleeping
		Thread.sleep(500);
	    } catch (InterruptedException e) {

	    }
	}
    }

    /**
     * Whispers (/w on twitch) a user (<tt>receiver</tt>) on a specific
     * <tt>channel</tt> a message (<tt>contents</tt>).
     * 
     * @param channel  The channel on which the user has to be, in order for this
     *                 whisper to work.
     * @param receiver The <tt>user</tt> who is to receive this message.
     * @param contents The <tt>contents</tt> of the whisper.
     * 
     * @see {@link IIRCController#PRIVMSG(String, String)}
     */
    public void whisper(final String channel, final String receiver, final String contents) {
	mController.PRIVMSG(channel, "/w " + receiver + " " + contents);
    }
}
