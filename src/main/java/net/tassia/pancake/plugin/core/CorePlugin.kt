package net.tassia.pancake.plugin.core

import net.tassia.event.Event
import net.tassia.pancake.Pancake
import net.tassia.pancake.plugin.Plugin
import net.tassia.pancake.plugin.PluginInfo
import net.tassia.pancake.plugin.core.event.mail.IncomingMailEvent
import net.tassia.pancake.plugin.core.event.mail.MailRouteEvent
import net.tassia.pancake.plugin.core.event.mail.MailRoutedEvent
import net.tassia.pancake.plugin.core.listener.mail.CoreIncomingMailListener
import net.tassia.pancake.plugin.core.listener.mail.CoreMailRouteListener
import net.tassia.pancake.plugin.core.listener.mail.CoreMailRoutedListener
import kotlin.reflect.KClass

/**
 * The core plugin for Pancake. Adds the base functions.
 *
 * @param pancake the [Pancake] instance
 *
 * @since Pancake 1.0
 * @author Tassilo
 */
class CorePlugin(override val pancake: Pancake) : Plugin(pancake) {

	override val info: PluginInfo = Info

	override val events: Set<KClass<out Event>> = setOf(
		IncomingMailEvent::class, MailRoutedEvent::class, MailRouteEvent::class
	)



	override fun onEnable() {
		// Register listeners
		pancake.events.registerListener(CoreIncomingMailListener)
		pancake.events.registerListener(CoreMailRoutedListener)
		pancake.events.registerListener(CoreMailRouteListener)
	}



	override fun onDisable() {
		// TODO
	}



	companion object {

		/**
		 * The version information for the core plugin.
		 */
		val Version = Pancake.VERSION

		/**
		 * The plugin information for the core plugin.
		 */
		val Info = PluginInfo(
			id = "net.tassia:Core",
			name = "Core",
			description = "Core systems for Pancake.",
			authors = setOf("Tassilo"),
			version = Version,
			constructor = ::CorePlugin
		)

	}

}
