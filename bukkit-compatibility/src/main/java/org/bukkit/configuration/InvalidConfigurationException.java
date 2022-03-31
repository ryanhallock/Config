package org.bukkit.configuration;

/** Property of Bukkit (Stored here, due to api changes in older versions.)
 * Exception thrown when attempting to load an invalid {@link Configuration}
 */
@SuppressWarnings("serial")
public class InvalidConfigurationException extends Exception {

    /** Property of Bukkit (Stored here, due to api changes in older versions.)
     * Creates a new instance of InvalidConfigurationException without a
     * message or cause.
     */
    public InvalidConfigurationException() {}

    /** Property of Bukkit (Stored here, due to api changes in older versions.)
     * Constructs an instance of InvalidConfigurationException with the
     * specified message.
     *
     * @param msg The details of the exception.
     */
    public InvalidConfigurationException(String msg) {
        super(msg);
    }

    /** Property of Bukkit (Stored here, due to api changes in older versions.)
     * Constructs an instance of InvalidConfigurationException with the
     * specified cause.
     *
     * @param cause The cause of the exception.
     */
    public InvalidConfigurationException(Throwable cause) {
        super(cause);
    }

    /** Property of Bukkit (Stored here, due to api changes in older versions.)
     * Constructs an instance of InvalidConfigurationException with the
     * specified message and cause.
     *
     * @param cause The cause of the exception.
     * @param msg The details of the exception.
     */
    public InvalidConfigurationException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
