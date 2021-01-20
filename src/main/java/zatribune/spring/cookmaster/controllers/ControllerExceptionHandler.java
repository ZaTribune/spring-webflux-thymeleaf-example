package zatribune.spring.cookmaster.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import zatribune.spring.cookmaster.exceptions.MyNotFoundException;

@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler {
    //because the handler method takes precedence the response will have status 200
    //because the response here is found-> which is the view error page
    //that's why we need to annotate the function with 404 response
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(MyNotFoundException.class)
    public ModelAndView handleRecipeNotFound(Exception e){
        log.error(e.getMessage());
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("/errors/404");
        modelAndView.addObject("exception",e);
        return modelAndView;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NumberFormatException.class)
    public ModelAndView handleBadRecipeIdValue(Exception e){
        log.error(e.getMessage());
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("/errors/400");
        modelAndView.addObject("exception",e);
        return modelAndView;
    }
}
