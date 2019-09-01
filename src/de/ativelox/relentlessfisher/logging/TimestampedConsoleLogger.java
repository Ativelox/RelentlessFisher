package de.ativelox.relentlessfisher.logging;

import java.time.LocalTime;

/**
 * * A basic {@link ILogger} implementation which simply prints on the Systems
 * default console, including a timestamp from the time on the system.
 * 
 * @author Ativelox ({@literal ativelox.dev@web.de})
 *
 */
public class TimestampedConsoleLogger implements ILogger {

    public TimestampedConsoleLogger() {

    }

    @Override
    public void log(final ELogType type, final String message) {
	final LocalTime t = LocalTime.now();

	final String log = t.getHour() + ":" + t.getMinute() + ":" + t.getSecond() + " [" + type.toString() + "]: "
		+ message;

	if (type == ELogType.DANGER) {
	    System.err.println(log);
	    return;
	}
	System.out.println(log);

    }

}
