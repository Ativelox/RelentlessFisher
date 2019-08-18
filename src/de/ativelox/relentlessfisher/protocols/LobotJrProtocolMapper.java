package de.ativelox.relentlessfisher.protocols;

import de.ativelox.relentlessfisher.utils.EFishingState;

/**
 * Provides a transition relation between messages and states. This is based on
 * the conversation flow with the twitch bot "lobotjr".
 * 
 * @author Ativelox {@literal<ativelox.dev@web.de>}
 *
 */
public class LobotJrProtocolMapper {

    /**
     * This method will generate a {@link EFishingState} for a given state and
     * message. This can be used to implement a state machine, by using this
     * function as a transition relations.
     * 
     * @param currentState The state the application is currently in.
     * @param message      The message given.
     * @return A new state, which represents the transition given
     *         <tt>currentState</tt> and <tt>message</tt>.
     */
    public static EFishingState Next(final EFishingState currentState, final String message) {
	if (currentState == EFishingState.CAN_CAST) {
	    if (message.startsWith("Congratulations")) {
		return EFishingState.CAN_CAST;
	    }
	    return EFishingState.IS_CAST;

	} else if (currentState == EFishingState.IS_CAST) {
	    if (message.equals("Your line is already cast! I'm sure a fish'll be along soon...")) {
		return EFishingState.IS_CAST;
	    }
	    return EFishingState.CAN_CATCH;

	} else if (currentState == EFishingState.CAN_CATCH) {
	    if (!message.startsWith("Congratulations")) {
		return EFishingState.OPTIONAL_RECORD_STATE;
	    }
	    return EFishingState.CAN_CAST;
	} else if (currentState == EFishingState.OPTIONAL_RECORD_STATE) {
	    return EFishingState.CAN_CAST;
	}

	return EFishingState.CAN_CAST;

    }

    private LobotJrProtocolMapper() {
    }
}
