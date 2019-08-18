package de.ativelox.relentlessfisher.irc;

import java.util.ArrayList;
import java.util.List;

import de.ativelox.relentlessfisher.listeners.IConnectionListener;
import de.ativelox.relentlessfisher.listeners.IJoinListener;
import de.ativelox.relentlessfisher.logging.ELogType;
import de.ativelox.relentlessfisher.logging.ILogger;
import de.ativelox.relentlessfisher.logging.LoggerFactory;

/**
 * A abstract implementation for a IRC Client, which mainly makes sure to
 * respond to IRC heartbeats.
 * 
 * @author Ativelox {@literal<ativelox.dev@web.de>}
 *
 */
public abstract class AClient implements Runnable {

    /**
     * The underlying controller, which manages connection to it's server.
     */
    protected final IIRCController mController;

    /**
     * The logger used to log information.
     */
    protected final ILogger mLogger;

    /**
     * All the objects wanting to receive callbacks when this application properly
     * connected to twitchs' IRC server.
     */
    protected final List<IConnectionListener> mConnectionListeners;

    /**
     * All the objects wanting to receive callbacks when this application properly
     * joins a channel.
     */
    protected final List<IJoinListener> mJoinListeners;

    /**
     * Creates a new {@link AClient}.
     * 
     * @param controller The controller which handles further server communication.
     */
    public AClient(final IIRCController controller) {
	mController = controller;
	mLogger = LoggerFactory.Get();
	controller.register(this);

	mConnectionListeners = new ArrayList<>();
	mJoinListeners = new ArrayList<>();

    }

    /**
     * Gets called by the underlying {@link IIRCController} when a message was sent
     * to the client by the server.
     * 
     * @param serverMessage The (raw) message from the server.
     */
    public void onServerMessageReceived(final String serverMessage) {
	if (serverMessage.contains("PING")) {
	    this.send("PONG");
	}
	mLogger.log(ELogType.SERVER, serverMessage);

    }

    /**
     * Registers the given listener to receive
     * {@link IConnectionListener#onConnection()} callbacks.
     * 
     * @param listener The listener to receive callbacks.
     */
    public void register(final IConnectionListener listener) {
	mConnectionListeners.add(listener);
    }

    /**
     * Registers the given listener to receive {@link IJoinListener#onJoin(String)}
     * callbacks.
     * 
     * @param listener The listener to receive callbacks.
     */
    public void register(final IJoinListener listener) {
	mJoinListeners.add(listener);
    }

    @Override
    public abstract void run();

    /**
     * Sends the given data to the underlying controllers server. Calls
     * {@link IIRCController#sendRawData(String)}.
     * 
     * @param data The data to send.
     */
    public void send(final String data) {
	mController.sendRawData(data);

    }
}
