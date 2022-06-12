package nl.rabobank.kotlinmovement.recipes.service;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message){
        super(message);
    }

}
