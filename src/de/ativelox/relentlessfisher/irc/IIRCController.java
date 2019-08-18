package de.ativelox.relentlessfisher.irc;

/**
 * 
 * Provides a very incomplete interface for the <tt>RFC 1459</tt> protocol. The
 * specifications can be found here: <a href=
 * "https://tools.ietf.org/html/rfc1459.html">https://tools.ietf.org/html/rfc1459.html</a>.
 * 
 * @author Ativelox {@literal<ativelox.dev@web.de>}
 *
 */
public interface IIRCController {

    /**
     * Connects this controller to the client's specified <tt>(host, port)</tt>
     * pair.
     * 
     * @return <tt>True</tt> if the connection was successful, <tt>false</tt>
     *         otherwise.
     */
    boolean connect(final String token);

    /**
     * The JOIN command is used by client to start listening a specific channel.
     * Whether or not a client is allowed to join a channel is checked only by the
     * server the client is connected to; all other servers automatically add the
     * user to the channel when it is received from other servers.<br>
     * 
     * The actual functionality of this command is given by the implementation.
     * 
     * @param channel The channel to join.
     * @param key     The key (password) used to join the channel if needed.
     * 
     * @see <a href="https://tools.ietf.org/html/rfc1459.html#section-4.2.1">JOIN
     *      Protocol</a>
     */
    void JOIN(final String channel, final String key);

    /**
     * The PART message causes the client sending the message to be removed from the
     * list of active users for all given channels listed in the parameter
     * string.<br>
     * 
     * The actual functionality of this command is given by the implementation.
     * 
     * @param channel The channel to part from.
     */
    void PART(final String channel);

    /**
     * PRIVMSG is used to send private messages between users. <tt>receiver</tt> is
     * the nickname of the receiver of the message. <br>
     * 
     * The actual functionality of this command is given by the implementation.
     * 
     * @param receiver     The name of the receiver of the message.
     * @param textToBeSent The text to send to the receiver.
     */
    void PRIVMSG(final String receiver, final String textToBeSent);

    /**
     * Reads messages from the server.
     */
    void read();

    /**
     * Registers the client managing this controller to this controller, used for
     * callbacks etc.
     * 
     * @param client The client managing this controller.
     */
    void register(final AClient client);

    /**
     * Sends the given <tt>data</tt> String to the connected servers' input stream.
     * 
     * @param data The data to send to the connected server's input stream.
     */
    void sendRawData(final String data);

}
