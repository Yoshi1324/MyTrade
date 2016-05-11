package ch.traiding.util;

import java.sql.Connection;
/**
 * Connection Pooling
 * MIN_CONNECTIONS are always built (typically 1).
 * More than MAX_CONNECTIONS will not be initialized.
 * @version 0.1 (May 27, 2015)
 * @author Philipp Gressly Freimann 
 *         (philipp.gressly@santis.ch)
 */
public interface ConnectionPooling {
	/**
	 * Get next free conneciton.
	 * @return next free connection or "null" if empty
	 */
	Connection getConnection();
	// TODO: Throw own Exception, if no more connections!

	/**
	 * Put unused connection back to the pool.
	 */
	void       putConnection(Connection con);
	// TODO: Throw own Exception, if not possible.
}