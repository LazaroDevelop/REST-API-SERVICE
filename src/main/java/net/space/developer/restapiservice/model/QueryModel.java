package net.space.developer.restapiservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Query model class for search queries
 *
 * @author Lazaro Noel Guerra Medina
 * @since 2025-04-29
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QueryModel {

    private String query;

}
