package com.project.category.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.project.utils.exceptionhandler.ExceptionMessages.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {
    private Long id;
    @Size(max = 45, message = CATEGORY_NAME_SHOULD_BE_LESS_THAN_45_CHARS)
    @NotBlank(message = CATEGORY_NAME_SHOULD_PRESENT_AND_NOT_BLANK)
    private String name;
}