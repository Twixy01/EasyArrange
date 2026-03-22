package org.example.backend.DTO.Service;

public record ServiceResponse(
        Long id,
        String name,
        Integer price,
        Integer duration
) {
}
