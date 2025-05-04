package net.space.developer.restapiservice.common.exception;

/**
 * Product not found exception custom class
 *
 * @author Lazaro Noel Guerra Medina
 * @since 2025-04-29
 */

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(){}

    public ProductNotFoundException(String message){
        super(message);
    }

    public ProductNotFoundException(String message, Throwable cause){
        super(message, cause);
    }

}
