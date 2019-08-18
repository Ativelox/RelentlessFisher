package de.ativelox.relentlessfisher;

import de.ativelox.relentlessfisher.irc.twitch.TwitchClient;
import de.ativelox.relentlessfisher.listeners.IConnectionListener;
import de.ativelox.relentlessfisher.listeners.IJoinListener;
import de.ativelox.relentlessfisher.listeners.IWhisperListener;
import de.ativelox.relentlessfisher.logging.ELogType;
import de.ativelox.relentlessfisher.logging.ILogger;
import de.ativelox.relentlessfisher.logging.LoggerFactory;
import de.ativelox.relentlessfisher.protocols.LobotJrProtocolMapper;
import de.ativelox.relentlessfisher.utils.EFishingState;

/**
 * This class provides the actual functionality to play the fishing mini-game
 * from "lobotjr. Gets driven by callbacks from the underlying
 * {@link TwitchClient}.
 * 
 * @author Ativelox {@literal<ativelox.dev@web.de>}
 *
 */
public class RelentlessFisher implements IWhisperListener, IConnectionListener, IJoinListener {

    /**
     * The command used to cast the fishing rod.
     */
    private final static String CAST_COMMAND = "!cast";

    /**
     * The command to catch a fish after it has taken the bait.
     */
    private final static String CATCH_COMMAND = "!catch";

    /**
     * The name of the channel the bot operates on.
     */
    private final static String CHANNEL_NAME = "lobosjr";

    /**
     * The name of the bot providing the fishing mini-game.
     */
    private final static String BOT_NAME = "lobotjr";

    /**
     * The client which manages this class' callbacks.
     */
    private final TwitchClient mClient;

    /**
     * The logger used for logging.
     */
    private final ILogger mLogger;

    /**
     * Represents the current state of the fishing minigame.
     */
    private EFishingState mCurrentState;

    /**
     * The delay (in milliseconds) used to respond to whispers. This is used, since
     * twitch will not allow rapid conversations.
     */
    private final int WHISPER_DELAY = 2000;

    /**
     * Creates a new {@link RelentlessFisher}.
     * 
     * @param client The client which drives this instances callbacks.
     */
    public RelentlessFisher(final TwitchClient client) {
	mClient = client;
	mLogger = LoggerFactory.Get();

	mCurrentState = EFishingState.CAN_CAST;

    }

    /*
     * (non-Javadoc)
     * 
     * @see de.ativelox.relentlessfisher.bridge.listeners.IConnectionListener#
     * onConnection()
     */
    @Override
    public void onConnection() {
	mLogger.log(ELogType.INFO, "Got connection.");
	mClient.send("CAP REQ :twitch.tv/tags twitch.tv/commands");
	mClient.join(CHANNEL_NAME);

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.ativelox.relentlessfisher.bridge.listeners.IJoinListener#onJoin(java.lang.
     * String)
     */
    @Override
    public void onJoin(final String channel) {
	mClient.whisper(CHANNEL_NAME, BOT_NAME, CAST_COMMAND);

    }

    /*
     * (non-Javadoc)
     * 
     * @see de.ativelox.relentlessfisher.bridge.listeners.IWhisperListener#
     * onWhisperReceived(java.lang.String, java.lang.String)
     */
    @Override
    public void onWhisperReceived(final String sender, final String message) {
	if (!sender.equals(BOT_NAME)) {
	    return;
	}
	try {
	    Thread.sleep(WHISPER_DELAY);
	} catch (InterruptedException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	mCurrentState = LobotJrProtocolMapper.Next(mCurrentState, message);

	switch (mCurrentState) {
	case CAN_CAST:
	    mClient.whisper(CHANNEL_NAME, BOT_NAME, CAST_COMMAND);
	    break;
	case CAN_CATCH:
	    mClient.whisper(CHANNEL_NAME, BOT_NAME, CATCH_COMMAND);
	    break;
	default:
	    break;

	}

    }
}
