package de.ativelox.relentlessfisher.irc.twitch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import de.ativelox.relentlessfisher.irc.AIRCController;
import de.ativelox.relentlessfisher.irc.IIRCController;
import de.ativelox.relentlessfisher.logging.ELogType;

/**
 * Provides an implementation for the {@link IIRCController} interface which
 * controls an unsecured connection to twitchs' IRC server.
 * 
 * @author Ativelox {@literal<ativelox.dev@web.de>}
 * 
 * @see {@link AIRCController}
 * @see {@link IIRCController}
 *
 */
public class UnsecuredTwitchIRCController extends AIRCController {

    /**
     * The URI for twitchs' IRC server.
     */
    private final static String HOST = "irc.chat.twitch.tv";

    /**
     * The port used for unsecured communication to the server.
     */
    private final static int PORT = 6667;

    /**
     * A newline representation for twitch.
     */
    private final static String NEWLINE = "\r\n";

    /**
     * The user who gave authorization to be utilized by this application.
     */
    private final String mUser;

    /**
     * Creates a new {@link UnsecuredTwitchIRCController}.
     * 
     * @param user The user who gave authorization to be utilized by this
     *             application.
     */
    public UnsecuredTwitchIRCController(final String user) {
	super(HOST, PORT);
	mUser = user;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.ativelox.relentlessfisher.irc.IIRCController#connect()
     */
    @Override
    public boolean connect(final String token) {
	try {
	    mSocket = new Socket(mHost, mPort);
	    mReader = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
	    mWriter = new PrintWriter(new OutputStreamWriter(mSocket.getOutputStream()));

	    /*
	     * log onto twitchs' IRC server as specified by twitch:
	     * https://dev.twitch.tv/docs/irc/guide/ this implementation uses the non-SSL
	     * port.
	     */
	    this.sendRawData("PASS oauth:" + token);
	    this.sendRawData("NICK " + mUser);

	} catch (final IOException e) {
	    try {
		mSocket.close();
		mReader.close();
		mWriter.close();

	    } catch (final IOException e1) {
		mLogger.log(ELogType.WARNING,
			"Couldn't close possibly bound resources. Connections to I/O Streams or Sockets might still be opened");

	    }
	    mLogger.log(ELogType.WARNING, "Couldn't properly establish a connection to: " + mHost + ":" + mPort + ".");
	    return false;
	}
	return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.ativelox.relentlessfisher.irc.IIRCController#JOIN(java.lang.String,
     * java.lang.String)
     */
    @Override
    public void JOIN(final String channel, final String key) {
	this.sendRawData("JOIN #" + channel);

    }

    /*
     * (non-Javadoc)
     * 
     * @see de.ativelox.relentlessfisher.irc.IIRCController#PART(java.lang.String)
     */
    @Override
    public void PART(final String channel) {
	this.sendRawData("PART #" + channel);

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.ativelox.relentlessfisher.irc.IIRCController#PRIVMSG(java.lang.String,
     * java.lang.String)
     */
    @Override
    public void PRIVMSG(final String receiver, final String textToBeSent) {
	this.sendRawData("PRIVMSG #" + receiver + " :" + textToBeSent);

    }

    /*
     * (non-Javadoc)
     * 
     * @see de.ativelox.relentlessfisher.irc.IIRCController#read()
     */
    @Override
    public void read() {
	try {
	    while (mReader.ready()) {
		mClient.onServerMessageReceived(mReader.readLine());
	    }

	} catch (final IOException e) {
	    mLogger.log(ELogType.WARNING,
		    "Encountered an I/O issue while trying to read from the clients Input Stream.");
	}

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.ativelox.relentlessfisher.irc.IIRCController#sendRawData(java.lang.String)
     */
    @Override
    public void sendRawData(final String data) {
	mWriter.write(data + NEWLINE);
	mWriter.flush();

	mLogger.log(ELogType.CLIENT, data);

    }

    @Override
    public boolean disconnect() {
	try {
	    mSocket.close();
	    mReader.close();
	    mWriter.close();

	} catch (final IOException e) {
	    mLogger.log(ELogType.WARNING, "Couldn't properly disconnect.");
	    return false;

	}
	return true;
    }
}
