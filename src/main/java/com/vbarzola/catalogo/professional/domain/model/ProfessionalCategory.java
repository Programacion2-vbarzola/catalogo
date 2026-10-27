package com.vbarzola.catalogo.professional.domain.model;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfessionalCategory {

    private Long id;
    private String name;
    private String description;
    private Boolean enabled;
    private Instant createdAt;
    private Instant updatedAt;
}
