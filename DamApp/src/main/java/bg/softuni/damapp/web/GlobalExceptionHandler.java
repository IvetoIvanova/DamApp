package bg.softuni.damapp.web;


import bg.softuni.damapp.exception.ObjectNotFoundException;
import bg.softuni.damapp.exception.UnauthorizedException;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Locale;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    @ExceptionHandler(ObjectNotFoundException.class)
    public ModelAndView handleObjectNotFound(ObjectNotFoundException objectNotFound) {
        ModelAndView modelAndView = new ModelAndView("404");
        modelAndView.addObject("objectId", objectNotFound.getId());

        return modelAndView;
    }

    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedException.class)
    public ModelAndView handleUnauthorizedException() {
        return new ModelAndView("/error/deactivated-account");
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleMaxSizeException(MaxUploadSizeExceededException ex, RedirectAttributes redirectAttributes, Locale locale) {
        String errorMessage = messageSource.getMessage("validation.file_size_too_large", null, locale);
        redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
        return "redirect:/advertisement-add";
    }
}
