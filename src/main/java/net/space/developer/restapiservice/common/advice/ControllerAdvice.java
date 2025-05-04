package net.space.developer.restapiservice.common.advice;

import net.space.developer.restapiservice.common.advice.model.ErrorMessage;
import net.space.developer.restapiservice.common.exception.ProductNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

/**
 * Controller advice class to manage custom exception messages
 *
 * @author Lazaro Noel Guerra Medina
 * @since 2025-04-29
 */

@RestControllerAdvice
public class ControllerAdvice {

    /**
     * Product not found exception function handler
     *
     * @param ex the {@link ProductNotFoundException} instance with the message
     * @param request the web request with the information
     * @return an instance of {@link ErrorMessage} with custom format
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ProductNotFoundException.class)
    public ErrorMessage productNotFoundException(ProductNotFoundException ex, WebRequest request){
        return ErrorMessage.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .timestamp(LocalDateTime.now())
                .message(ex.getMessage())
                .description(request.getDescription(false))
                .build();
    }

    /**
     * Global exception function handler
     *
     * @param ex the {@link Exception} instance with the message
     * @param request the {@link WebRequest} with the information
     * @return an instance of {@link ErrorMessage} with custom format
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorMessage globalException(Exception ex, WebRequest request){
        return ErrorMessage.builder()
                .status(HttpStatus.NO_CONTENT.value())
                .timestamp(LocalDateTime.now())
                .message(ex.getMessage())
                .description(request.getDescription(false))
                .build();
    }

}
