package comp3350.reshop.data.hsqldb;

public class DataException extends RuntimeException {
    public DataException(final Exception cause) {
        super(cause);
    }
}
