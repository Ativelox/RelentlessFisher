package de.ativelox.relentlessfisher.logging;

/**
 * Provides a basic interface to log messages.
 * 
 * @author Ativelox {@literal<ativelox.dev@web.de>}
 *
 */
public interface ILogger {

    /**
     * Used to log the given message with a given type, e.g. INFO, WARNING, ...
     * 
     * @param type    The type (severity) of the log.
     * @param message The message further describing the log.
     */
    void log(final ELogType type, final String message);

}
