package com.vbarzola.catalogo.professional.infrastructure.web.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class ProfessionalCategoryRequest {

    @NotNull
    @Size(max = 200)
    private String name;

    @Size(max = 500)
    private String description;

    @NotNull
    private Boolean enabled;
}
