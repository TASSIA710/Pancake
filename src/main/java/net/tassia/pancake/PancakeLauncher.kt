package net.tassia.pancake

import net.tassia.pancake.util.config.ConfigIO
import net.tassia.pancake.util.config.driver.ConfigIniDriver
import net.tassia.pancake.util.DatabaseConnector
import net.tassia.pancake.io.PancakeConfig
import net.tassia.pancake.util.PancakeIO
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
		// Prepare IO
		PancakeIO.createFolders()

		// Load configuration
		val cfg = File("config.ini").let {
			val config = PancakeConfig()
			if (it.exists()) {
				ConfigIO.load(it, config, ConfigIniDriver)
			} else {
				ConfigIO.save(it, config, ConfigIniDriver)
			}
			return@let config
		}

		// Connect to database
		DatabaseConnector.connect(cfg)

		// Launch
		return Pancake(cfg)
	}

}
