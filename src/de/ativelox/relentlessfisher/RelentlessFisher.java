package de.ativelox.relentlessfisher;

import java.util.concurrent.ExecutorService;

import de.ativelox.relentlessfisher.irc.twitch.TwitchClient;
import de.ativelox.relentlessfisher.listeners.IConnectionListener;
import de.ativelox.relentlessfisher.listeners.IJoinListener;
import de.ativelox.relentlessfisher.listeners.IWhisperListener;
import de.ativelox.relentlessfisher.logging.ELogType;
import de.ativelox.relentlessfisher.logging.ILogger;
import de.ativelox.relentlessfisher.logging.LoggerFactory;
import de.ativelox.relentlessfisher.protocols.LobotJrProtocolMapper;
import de.ativelox.relentlessfisher.timer.ITimeoutListener;
import de.ativelox.relentlessfisher.timer.ITimer;
import de.ativelox.relentlessfisher.timer.SimpleTimer;
import de.ativelox.relentlessfisher.utils.EFishingState;

/**
 * This class provides the actual functionality to play the fishing mini-game
 * from "lobotjr. Gets driven by callbacks from the underlying
 * {@link TwitchClient}.
 * 
 * @author Ativelox {@literal<ativelox.dev@web.de>}
 *
 */
public class RelentlessFisher implements IWhisperListener, IConnectionListener, IJoinListener, ITimeoutListener {

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
     * The delay (in milliseconds) used to respond to whispers. This is used, since
     * twitch will not allow rapid conversations.
     */
    private final static int WHISPER_DELAY = 2000;

    /**
     * The time in ms after which this application assumes the bot isn't working
     * properly and starts the mini-game from anew.
     */
    private final static long NO_RESPONSE_TIMEOUT = 300000;

    /**
     * The accuracy of the underlying timer in ms.
     */
    private final static long TIMER_ACCURACY = 30000;

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
     * The timer used to handle possible bot failure.
     */
    private final ITimer mWhisperTimer;

    /**
     * The executor used to handle threading.
     */
    private final ExecutorService mExecutor;

    /**
     * Creates a new {@link RelentlessFisher}.
     * 
     * @param client   The client which drives this instances callbacks.
     * @param executor The executor used to handle threading.
     */
    public RelentlessFisher(final TwitchClient client, final ExecutorService executor) {
	mClient = client;
	mLogger = LoggerFactory.Get();
	mExecutor = executor;

	mWhisperTimer = new SimpleTimer(NO_RESPONSE_TIMEOUT, TIMER_ACCURACY, this);

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
	mExecutor.submit(mWhisperTimer);

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

    @Override
    public void onTimeout(final long ms) {
	// the bot hasn't responded for some amount of time, so we retry.
	mCurrentState = EFishingState.CAN_CAST;
	mClient.whisper(CHANNEL_NAME, BOT_NAME, CAST_COMMAND);

	mExecutor.submit(mWhisperTimer);

    }

    /*
     * (non-Javadoc)
     * 
     * @see de.ativelox.relentlessfisher.bridge.listeners.IWhisperListener#
     * onWhisperReceived(java.lang.String, java.lang.String)
     */
    @Override
    public void onWhisperReceived(final String sender, final String message) {
	mWhisperTimer.reset();

	if (!sender.equals(BOT_NAME)) {
	    return;
	}
	try {
	    Thread.sleep(WHISPER_DELAY);
	} catch (InterruptedException e) {
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
