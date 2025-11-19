package exceptions;

/**
 * Indicates there was an error connecting to the database
 */
public class InvalidID extends Exception{
    public InvalidID(String message) {
        super(message);
    }
    public InvalidID(String message, Throwable ex) {
        super(message, ex);
    }
}
