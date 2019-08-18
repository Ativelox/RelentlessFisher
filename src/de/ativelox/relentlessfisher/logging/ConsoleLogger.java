package de.ativelox.relentlessfisher.logging;

/**
 * A basic {@link ILogger} implementation which simply prints on the Systems
 * default console.
 * 
 * @author Ativelox {@literal<ativelox.dev@web.de>}
 *
 */
public final class ConsoleLogger implements ILogger {

    /**
     * Creates a new {@link ConsoleLogger}.
     */
    public ConsoleLogger() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.ativelox.relentlessfisher.logging.ILogger#log(de.ativelox.relentlessfisher
     * .logging.ELogType, java.lang.String)
     */
    @Override
    public void log(final ELogType type, final String message) {
	final String log = "[" + type.toString() + "]: " + message;

	if (type == ELogType.DANGER) {
	    System.err.println(log);
	    return;
	}
	System.out.println(log);

    }
}
