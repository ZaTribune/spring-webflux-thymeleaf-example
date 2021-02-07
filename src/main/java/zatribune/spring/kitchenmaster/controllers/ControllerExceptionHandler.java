package zatribune.spring.kitchenmaster.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.thymeleaf.exceptions.TemplateInputException;

@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler {
    //because the handler method takes precedence the response will have status 200
    //because the response here is found-> which is the view error page
    //that's why we need to annotate the function with 404 response
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({TemplateInputException.class})
    public String handleNotFound(Exception e,Model model){
        log.error(e.getMessage());
        model.addAttribute("exception",e);
        return "error/404";
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({NumberFormatException.class})
    public String handleBadNumberValue(Exception e,Model model){
        log.error(e.getMessage());
        model.addAttribute("exception",e);
        return "error/400";
    }
}
