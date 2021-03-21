package net.tassia.pancake.plugin.http

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.tassia.pancake.JSON
import net.tassia.pancake.entity.group.Groups
import net.tassia.pancake.entity.session.Sessions
import net.tassia.pancake.plugin.http.data.ExceptionResponse
import net.tassia.pancake.plugin.http.data.StatusResponse
import org.jetbrains.exposed.sql.transactions.transaction
import java.nio.charset.StandardCharsets
import kotlin.reflect.KClass

/**
 * This class is used to register routes.
 *
 * @since Pancake 1.0
 * @author Tassilo
 */
class RouteRegistrar(private val route: Routing, private val http: PancakeHttp) {

	/**
	 * Determines whether the JSON response should be pretty-printed or not.
	 *
	 * @param call the [ApplicationCall]
	 * @return pretty-print
	 */
	private fun shouldPrettyPrint(call: ApplicationCall): Boolean {
		val pretty = call.request.queryParameters["pretty"] ?: "false"
		return pretty.toLowerCase() == "true"
	}

	/**
	 * Authenticates an HTTP request.
	 *
	 * @param call the [ApplicationCall]
	 * @param privilege the privilege to check
	 * @param transaction the transaction
	 * @return whether the client has the privilege, or nope
	 */
	private fun authenticate(call: ApplicationCall, privilege: String?, transaction: HttpTransaction<*>): Boolean {
		// Extract header
		val headerFull = call.request.header("Authentication") ?: return false
		if (!headerFull.startsWith("Personal ")) return false
		val header = headerFull.substring("Personal ".length)

		// Find credentials
		val session = Sessions.getSessionByToken(header) ?: return false
		val account = session.account
		val group = account.group

		// Store auth information
		val auth = AuthInformation(session, account, group, privilege?.let { Groups.getPrivilege(group, privilege) })
		transaction.auth = auth
		return auth.privilege?.let { it.value != "false" } ?: false
	}

	/**
	 * Sends a response back to the client.
	 *
	 * @param returnResult the first possible response
	 * @param call the [ApplicationCall]
	 * @param transaction the HTTP transaction
	 */
	private suspend fun submitResponse(returnResult: Any?, call: ApplicationCall, transaction: HttpTransaction<*>) {
		call.response.status(transaction.status)
		sendJsonResponse(returnResult ?: transaction.response, call)
	}

	/**
	 * Sends a JSON response back to the client.
	 *
	 * @param data the response
	 * @param call the [ApplicationCall]
	 */
	private suspend fun sendJsonResponse(data: Any, call: ApplicationCall) {
		val json = JSON.stringify(data, shouldPrettyPrint(call))
		call.respondBytes(json.toByteArray(StandardCharsets.UTF_8), ContentType.Application.Json)
	}

	/**
	 * If some request cause an exception to be thrown, it will be reported via this function.
	 * This also automatically sends an [ExceptionResponse] to the client.
	 *
	 * @param call the [ApplicationCall]
	 * @param error the error
	 */
	private suspend fun reportError(call: ApplicationCall, error: Throwable) {
		http.warn("Error processing HTTP request.", error)
		call.response.status(HttpStatusCode.InternalServerError)
		sendJsonResponse(ExceptionResponse(error), call)
	}





	/**
	 * Registers a new JSON `GET` route.
	 *
	 * @param path the path
	 * @param function the function
	 */
	fun get(path: String, function: (HttpTransaction<Nothing>) -> Any?) {
		route.get(path) {
			try {
				// Create transaction
				val transaction = HttpTransaction(call, null)

				// Authentication
				transaction { authenticate(call, null, transaction) }

				// Submit
				val response = transaction { function(transaction) }
				submitResponse(response, call, transaction)

			} catch (ex: Throwable) {
				// Send error response
				reportError(call, ex)
			}
		}
	}



	/**
	 * Registers a new JSON `POST` route.
	 *
	 * @param path the path
	 * @param function the function
	 */
	inline fun <reified T : Any> post(path: String, noinline function: (HttpTransaction<T>, T) -> Any?) {
		post(path, T::class, function)
	}

	/**
	 * Registers a new JSON `POST` route.
	 *
	 * @param path the path
	 * @param requestType the request type
	 * @param function the function
	 */
	fun <T : Any> post(path: String, requestType: KClass<T>, function: (HttpTransaction<T>, T) -> Any?) {
		route.post(path) {
			try {
				// Create transaction
				val transaction = HttpTransaction<T>(call, null)

				// Authentication
				transaction { authenticate(call, null, transaction) }

				// Ensure JSON content type
				if (!call.request.contentType().match(ContentType.Application.Json)) {
					call.response.status(HttpStatusCode.BadRequest)
					sendJsonResponse(StatusResponse.BadRequest("Content-Type must be 'application/json'."), call)
					return@post
				}

				try {
					// Read request body
					// TODO: Make JSON parsing non-blocking
					val payload = withContext(Dispatchers.IO) {
						JSON.parse(call.receiveStream(), requestType)
					}
					transaction.request = payload

					// Submit
					val response = transaction { return@transaction function(transaction, payload) }
					submitResponse(response, call, transaction)

				} catch (ex: UnrecognizedPropertyException) {
					// Unknown property
					call.response.status(HttpStatusCode.BadRequest)
					sendJsonResponse(StatusResponse.BadRequest("Unknown property: " + ex.propertyName), call)

				} catch (ex: JsonProcessingException) {
					// Invalid JSON
					call.response.status(HttpStatusCode.BadRequest)
					sendJsonResponse(StatusResponse.BadRequest("Invalid JSON payload."), call)

				}
			} catch (ex: Throwable) {
				// Send error response
				reportError(call, ex)
			}
		}
	}

}