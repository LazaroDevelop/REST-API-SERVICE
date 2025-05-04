package net.space.developer.restapiservice.common.advice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Error message class for controller advice
 *
 * @author Lazaro Noel Guerra Medina
 * @since 2025-04-29
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorMessage {
    /**
     * Http status code
     */
    private int status;

    /**
     * Error issue timestamp
     */
    private LocalDateTime timestamp;

    /**
     * Error message
     */
    private String message;

    /**
     * Error message description
     */
    private String description;
}
