package net.tassia.pancake

import net.tassia.event.EventManager
import net.tassia.pancake.config.PancakeConfig
import net.tassia.pancake.event.IncomingMailEvent
import net.tassia.pancake.event.MailRouteEvent
import net.tassia.pancake.event.MailRoutedEvent
import net.tassia.pancake.listener.CoreIncomingMailListener
import net.tassia.pancake.listener.CoreMailRouteListener
import net.tassia.pancake.listener.CoreMailRoutedListener
import java.util.logging.Logger

/**
 * The base class for Pancake.
 *
 * @param config the configuration
 *
 * @since Pancake 1.0
 * @author Tassilo
 */
class Pancake(val config: PancakeConfig) {

	/**
	 * Used to handle events for Pancake.
	 */
	val events = EventManager.newDefault()

	/**
	 * The logger for Pancake.
	 */
	val logger = Logger.getLogger("Pancake")



	init {
		// Register core events
		events.registerEvent(IncomingMailEvent::class)
		events.registerEvent(MailRoutedEvent::class)
		events.registerEvent(MailRouteEvent::class)

		// Register core event listeners
		events.registerListener(IncomingMailEvent::class, CoreIncomingMailListener(this))
		events.registerListener(MailRoutedEvent::class, CoreMailRoutedListener())
		events.registerListener(MailRouteEvent::class, CoreMailRouteListener())
	}



	companion object {

		/**
		 * The hard-coded limit of how long a single mail address ('name@hostname.tld') can be (in bytes).
		 */
		const val MAIL_ADDRESS_MAX = 255

		/**
		 * The hard-coded limit of how long a mail subject can be (in bytes).
		 */
		const val MAIL_SUBJECT_MAX = 255

		/**
		 * The hard-coded limit of how long a mails displayable content can be (in bytes).
		 */
		const val MAIL_CONTENT_MAX = Int.MAX_VALUE

		/**
		 * The hard-coded limit of how many recipients a single mail can have at maximum.
		 */
		const val MAIL_RECIPIENTS_MAX = 63

	}

}
