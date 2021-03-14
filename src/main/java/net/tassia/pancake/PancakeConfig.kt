package net.tassia.pancake

import net.tassia.pancake.config.ConfigDescription
import net.tassia.pancake.config.ConfigEntry
import java.util.logging.Level

/**
 * The core configuration for Pancake. All settings that are non-crucial for the boot process
 * are stored in the database.
 *
 * @since Pancake 1.0
 * @author Tassilo
 */
data class PancakeConfig(

	/**
	 * The level used to determine what events to log.
	 */
	@ConfigEntry("Logging.Level")
	@ConfigDescription("The level used to determine what events to log.")
	var loggingLevel: Level = Level.INFO,



	/**
	 * Which database to use (SQLite, MySQL).
	 */
	@ConfigEntry("Database.Driver")
	@ConfigDescription("Which database to use (SQLite, MySQL).")
	var databaseDriver: DatabaseDriver = DatabaseDriver.SQLITE,

	/**
	 * The hostname of the MySQL Server.
	 */
	@ConfigEntry("Database.MySQL.Hostname")
	@ConfigDescription("The hostname of the MySQL Server.")
	var mysqlHostname: String = "localhost",

	/**
	 * The port of the MySQL Server.
	 */
	@ConfigEntry("Database.MySQL.Port")
	@ConfigDescription("The port of the MySQL Server.")
	var mysqlPort: Int = 3306,

	/**
	 * The name of the database to connect to.
	 */
	@ConfigEntry("Database.MySQL.Database")
	@ConfigDescription("The name of the database to connect to.")
	var mysqlDatabase: String = "database",

	/**
	 * The username to use when connecting to the MySQL Server.
	 */
	@ConfigEntry("Database.MySQL.Username")
	@ConfigDescription("The username to use when connecting to the MySQL Server.")
	var mysqlUsername: String = "username",

	/**
	 * The password to use when connecting to the MySQL Server.
	 */
	@ConfigEntry("Database.MySQL.Password")
	@ConfigDescription("The password to use when connecting to the MySQL Server.")
	var mysqlPassword: String = "password",



	/**
	 * Whether to launch an HTTP server.
	 */
	@ConfigEntry("HTTP.Enabled")
	@ConfigDescription("Whether to launch an HTTP server.")
	var httpEnabled: Boolean = true,

	/**
	 * The hostname to bind the HTTP server to.
	 */
	@ConfigEntry("HTTP.Hostname")
	@ConfigDescription("The hostname to bind the HTTP server to.")
	var httpHostname: String = "127.0.0.1",

	/**
	 * The port of the HTTP server.
	 */
	@ConfigEntry("HTTP.Port")
	@ConfigDescription("The port of the HTTP server.")
	var httpPort: Int = 8080,

)
