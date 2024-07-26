package bg.softuni.damapp.web;


import bg.softuni.damapp.exception.ObjectNotFoundException;
import bg.softuni.damapp.exception.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    @ExceptionHandler(ObjectNotFoundException.class)
    public ModelAndView handleObjectNotFound(ObjectNotFoundException objectNotFound) {
        ModelAndView modelAndView = new ModelAndView("404");
        modelAndView.addObject("objectId", objectNotFound.getId());

        return modelAndView;
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<String> handleUnauthorizedException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Акаунтът ви е деактивиран. Моля, свържете се с администратор.");
    }
}
