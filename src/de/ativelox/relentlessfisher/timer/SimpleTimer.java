package de.ativelox.relentlessfisher.timer;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides a simple {@link ITimer} implementation, which will call
 * {@link ITimeoutListener#onTimeout(long)} on all its listeners when the given
 * timeout is reached. When the timeout is reached, this thread will stop
 * running. Calling {@link ITimer#run()} will ensure that the this instance is
 * in a legal state by calling {@link ITimer#reset()} internally.
 * 
 * @author Ativelox ({@literal ativelox.dev@web.de})
 *
 */
public class SimpleTimer implements ITimer {

    /**
     * A list of all the listeners that are currently registered to this instance.
     */
    private final List<ITimeoutListener> mTimeoutListeners;

    /**
     * The time in milliseconds after which a time out is reached.
     */
    private final long mTimeOut;

    /**
     * The time that has passed from the start of this timer.
     */
    private long mPassedTime;

    /**
     * The time that this timer has started, given by
     * {@link System#currentTimeMillis()}.
     */
    private long mStartTime;

    /**
     * The accuracy of the timer, i.e. how often the timer checks whether the
     * timeout is reached (in ms).
     */
    private final long mAccuracy;

    /**
     * Creates a new {@link SimpleTimer}.
     * 
     * @param timeOut          The time in ms when this timer calls
     *                         {@link ITimeoutListener#onTimeout(long)} on all its
     *                         listeners.
     * @param accuracy         The accuracy of the timer, see
     *                         {@link SimpleTimer#mAccuracy}.
     * @param timeoutListeners All the listeners initially registered to this timer.
     */
    public SimpleTimer(final long timeOut, final long accuracy, final ITimeoutListener... timeoutListeners) {
	mTimeoutListeners = new ArrayList<>();
	mTimeOut = timeOut;
	mAccuracy = accuracy;

	for (final ITimeoutListener t : timeoutListeners) {
	    mTimeoutListeners.add(t);

	}

    }

    @Override
    public boolean add(final ITimeoutListener timeoutListener) {
	return mTimeoutListeners.add(timeoutListener);

    }

    @Override
    public long passed() {
	return mPassedTime;
    }

    @Override
    public boolean remove(final ITimeoutListener timeoutListener) {
	return mTimeoutListeners.remove(timeoutListener);
    }

    @Override
    public void reset() {
	mStartTime = System.currentTimeMillis();
	mPassedTime = 0;

    }

    @Override
    public void run() {
	this.reset();

	while (true) {
	    mPassedTime = System.currentTimeMillis() - mStartTime;

	    if (mPassedTime >= mTimeOut) {
		for (final ITimeoutListener t : mTimeoutListeners) {
		    t.onTimeout(mPassedTime);
		}
		break;
	    }

	    try {
		Thread.sleep(mAccuracy);

	    } catch (final InterruptedException e) {
		e.printStackTrace();

	    }
	}
    }

}
