package net.tassia.pancake.plugin.core.listener.mail

import net.tassia.event.EventListener
import net.tassia.pancake.entity.route.Route
import net.tassia.pancake.entity.route.Routes
import net.tassia.pancake.plugin.core.CorePlugin
import net.tassia.pancake.plugin.core.event.mail.MailRouteEvent

/**
 * Listens for mails wanting to be routed and, well, routes them.
 *
 * @since Pancake 1.0
 * @author Tassilo
 */
class CoreMailRouteListener(private val core: CorePlugin) : EventListener<MailRouteEvent> {

	override fun onEvent(event: MailRouteEvent) {
		// 1. Check EQUAL routes
		Routes.findFirstEqual(event.address)?.let { submitRoute(event, it) }

		// 2. Check NOT_EQUAL routes
		Routes.findFirstNotEqual(event.address)?.let { submitRoute(event, it) }

		// 3. Check MATCH routes
		Routes.findFirstMatch(event.address)?.let { submitRoute(event, it) }

		// 4. Check NOT_MATCH routes
		Routes.findFirstNotMatch(event.address)?.let { submitRoute(event, it) }

		// 5. Not routes matched :/
		// Do nothing.
	}

	private fun submitRoute(event: MailRouteEvent, route: Route) {
		// Update fields in the old event (for other listeners in propagation)
		route.target.let {
			event.folder = it
			event.account = it.account
		}
		event.route = route
	}

}
