package comp3350.reshop.logic.exceptions;

public class ExpiredCardException extends InvalidInputException{
    public ExpiredCardException(String message) {
        super(message);
    }
}
