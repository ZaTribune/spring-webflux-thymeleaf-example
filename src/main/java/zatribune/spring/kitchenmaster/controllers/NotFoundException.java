package zatribune.spring.kitchenmaster.controllers;


public class NotFoundException extends RuntimeException{
    public NotFoundException(String message) {
        super(message);
    }
}
