package de.ativelox.relentlessfisher.logging;

/**
 * This factory provides access to an {@link ILogger} which should be used
 * consistently throughout the project. This is done by using the singleton
 * concept.
 * 
 * @author Ativelox {@literal<ativelox.dev@web.de>}
 *
 */
public class LoggerFactory {

    /**
     * The current instance of the logger.
     */
    private static ILogger INSTANCE = null;

    /**
     * Gets the current logger instance. This should be used consistently throughout
     * the project as to avoid multiple different loggers.
     * 
     * @return The logger, used to log messages.
     */
    public static ILogger Get() {
	if (INSTANCE == null) {
	    INSTANCE = new ConsoleLogger();
	}
	return INSTANCE;

    }

    private LoggerFactory() {
    }

}
