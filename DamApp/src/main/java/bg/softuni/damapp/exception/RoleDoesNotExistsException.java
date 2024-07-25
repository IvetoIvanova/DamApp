package bg.softuni.damapp.exception;

public class RoleDoesNotExistsException extends RuntimeException {

    public RoleDoesNotExistsException(String message) {
        super(message);
    }
}
