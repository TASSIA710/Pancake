package net.tassia.pancake.util.config

/**
 * Marks a variable as being a configuration entry.
 *
 * @since Pancake 1.0
 * @author Tassilo
 */
@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class ConfigEntry(

	/**
	 * The name of this configuration entry. You can use nested paths (e.g. 'Database.Driver' or 'Database.MySQL.Port').
	 */
	val path: String

)