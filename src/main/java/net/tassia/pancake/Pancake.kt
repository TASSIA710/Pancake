package net.tassia.pancake

import net.tassia.event.EventManager
import net.tassia.pancake.event.IncomingMailEvent
import net.tassia.pancake.event.MailRouteEvent
import net.tassia.pancake.event.MailRoutedEvent
import net.tassia.pancake.io.PancakeConfig
import net.tassia.pancake.io.PrintStreamLoggingHandler
import net.tassia.pancake.listener.CoreIncomingMailListener
import net.tassia.pancake.listener.CoreMailRouteListener
import net.tassia.pancake.listener.CoreMailRoutedListener
import net.tassia.pancake.plugin.PluginManager
import net.tassia.pancake.util.version.Version
import net.tassia.pancake.util.version.VersionType
import java.util.logging.Logger
import kotlin.system.exitProcess

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
	val logger: Logger = Logger.getLogger("Pancake")

	/**
	 * The [PluginManager] for Pancake.
	 */
	val plugins = PluginManager(this)



	init {
		// Setup logger
		logger.useParentHandlers = false
		logger.addHandler(PrintStreamLoggingHandler(System.out))
		logger.level = config.loggingLevel
		logger.info("Initializing Pancake...")

		// Register core events
		events.registerEvent<IncomingMailEvent>()
		events.registerEvent<MailRoutedEvent>()
		events.registerEvent<MailRouteEvent>()

		// Register core event listeners
		events.registerListener(CoreIncomingMailListener)
		events.registerListener(CoreMailRoutedListener)
		events.registerListener(CoreMailRouteListener)

		// Load plugins
		plugins.locatePlugins()

		// Enable plugins
		plugins.enablePlugins()

		// Done!
		logger.info("Done! Running Pancake/${VERSION.toDisplayString()}")
	}



	/**
	 * Exits the Pancake application.
	 *
	 * @param status the status code (e.g. `0` for success)
	 */
	fun exit(status: Int) {
		// Disable plugins
		plugins.disablePlugins()

		// Exit the running process
		exitProcess(status)
	}



	companion object {

		/**
		 * The current version of Pancake.
		 */
		val VERSION = Version(1, 0, 0, 1, "dbd3766fc98765487e213418f5c600e027c8957a", branch = "main", type = VersionType.SNAPSHOT)

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
