package zatribune.spring.cookmaster.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)//404 and without it we'll get 500 error on the client side
public class MyNotFoundException extends RuntimeException {

    public MyNotFoundException() {
        super();
    }

    public MyNotFoundException(String message) {
        super(message);
    }

    public MyNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
