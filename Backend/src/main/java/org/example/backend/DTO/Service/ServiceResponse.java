package org.example.backend.DTO.Service;

public record ServiceResponse(
        Long serviceId,
        String name,
        Integer price,
        Integer duration,
        String description,
        String image
) {
}
