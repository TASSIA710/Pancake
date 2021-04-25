package net.tassia.pancake.security.hash

/**
 * Defines how to hash passwords (actually, how to hash any data).
 *
 * @since Pancake 1.0
 * @author Tassilo
 */
enum class HashAlgorithm(val id: String, val constructor: () -> HashFunction) {

	/**
	 * The MessageDigest 5 algorithm.
	 *
	 * @see net.tassia.pancake.security.hash.MD5
	 */
	MD5("1", ::MD5),

	// BLOWFISH("2y"), TODO

	/**
	 * The SHA-256 algorithm.
	 *
	 * @see net.tassia.pancake.security.hash.SHA256
	 */
	SHA256("5", ::SHA256),

	/**
	 * The SHA-512 algorithm.
	 *
	 * @see net.tassia.pancake.security.hash.SHA512
	 */
	SHA512("6", ::SHA512);



	operator fun invoke(): HashFunction = constructor()

	companion object {

		/**
		 * Finds the [HashAlgorithm] with the given ID.
		 *
		 * @param id the ID
		 * @return the algorithm, or `null`
		 */
		fun fromID(id: String): HashAlgorithm? {
			return values().firstOrNull { it.id == id }
		}

	}

}