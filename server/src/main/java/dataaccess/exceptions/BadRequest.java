package dataaccess.exceptions;

/**
 * Indicates there was an error connecting to the database
 */
public class BadRequest extends Exception{
    public BadRequest(String message) {
        super(message);
    }
    public BadRequest(String message, Throwable ex) {
        super(message, ex);
    }
}
