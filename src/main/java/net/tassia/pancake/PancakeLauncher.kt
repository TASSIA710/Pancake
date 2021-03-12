package net.tassia.pancake

import net.tassia.pancake.config.ConfigIO
import net.tassia.pancake.config.DatabaseDriver
import net.tassia.pancake.config.PancakeConfig
import org.jetbrains.exposed.sql.Database
import java.io.File

/**
 * This object is used to launch a new [Pancake] instance.
 *
 * @since Pancake 1.0
 * @author Tassilo
 */
object PancakeLauncher {

	const val VERSION: String = "1.0.0-PRE-1"

	/**
	 * Launches a new [Pancake] instance.
	 *
	 * @param args command-line arguments
	 * @return the instance
	 */
	fun new(args: Array<String>): Pancake {
		// Load configuration
		val cfg = ConfigIO.load(File("config.yml"), PancakeConfig())

		// Connect to database
		when (cfg.databaseDriver) {
			DatabaseDriver.SQLITE -> {
				Database.connect("jdbc:sqlite:./data.db", "org.sqlite.JDBC")
			}
			DatabaseDriver.MYSQL -> {
				Database.connect("jdbc:mysql://${cfg.mysqlHostname}:${cfg.mysqlPort}/${cfg.mysqlDatabase}",
					driver = "com.mysql.jdbc.Driver", user = cfg.mysqlUsername, password = cfg.mysqlPassword)
			}
		}

		// Launch
		return Pancake()
	}

}