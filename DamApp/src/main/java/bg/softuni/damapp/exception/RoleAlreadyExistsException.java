package bg.softuni.damapp.exception;

public class RoleAlreadyExistsException extends RuntimeException {

    public RoleAlreadyExistsException(String message) {
        super(message);
    }
}
