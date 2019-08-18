package de.ativelox.relentlessfisher.irc;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

import de.ativelox.relentlessfisher.logging.ILogger;
import de.ativelox.relentlessfisher.logging.LoggerFactory;

/**
 * Provides an abstract implementation for the {@link IIRCController} interface.
 * 
 * @author Ativelox {@literal<ativelox.dev@web.de>}
 *
 */
public abstract class AIRCController implements IIRCController {

    /**
     * The host (URI) to which this controller maintains connection.
     */
    protected final String mHost;

    /**
     * The port on which to connect to the {@link AIRCController#mHost host}.
     */
    protected final int mPort;

    /**
     * A <tt>BufferedReader</tt> which is used to read from the servers
     * <tt>OutputStream</tt>.
     */
    protected BufferedReader mReader;

    /**
     * A <tt>PrintWriter</tt> which is used to write to the servers
     * <tt>InputStream</tt>.
     */
    protected PrintWriter mWriter;

    /**
     * The logger used to log information.
     */
    protected ILogger mLogger;

    /**
     * The socket to the server specified by {@link AIRCController#mHost host} and
     * {@link AIRCController#mPort port}.
     */
    protected Socket mSocket;

    /**
     * The client managing top-level interaction to this controller.
     */
    protected AClient mClient;

    /**
     * Creates a new {@link AIRCController}.
     * 
     * @param host The host (URI) to connect to.
     * @param port The port on which to connect to the given host.
     */
    public AIRCController(final String host, final int port) {
	mHost = host;
	mPort = port;

	mLogger = LoggerFactory.Get();
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.ativelox.relentlessfisher.irc.IIRCController#register(de.ativelox.
     * relentlessfisher.irc.AClient)
     */
    @Override
    public void register(final AClient client) {
	mClient = client;

    }
}
