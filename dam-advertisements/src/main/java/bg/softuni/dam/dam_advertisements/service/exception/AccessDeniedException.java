package bg.softuni.dam.dam_advertisements.service.exception;

public class AccessDeniedException extends RuntimeException{

    public AccessDeniedException(String message) {
        super(message);
    }
}
