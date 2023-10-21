package com.project.item.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import static com.project.utils.exceptionhandler.ExceptionMessages.*;

import com.project.category.model.CategoryDTO;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDTO {
    private Long id;
    @Size(max = 45, message = ITEM_NAME_SHOULD_BE_LESS_THAN_45_CHARS)
    @NotBlank(message = ITEM_NAME_SHOULD_PRESENT_AND_NOT_BLANK)
    private String name;
    @NotNull(message = ITEM_PRICE_SHOULD_PRESENT)
    @Positive(message = ITEM_PRICE_SHOULD_BE_POSITIVE)
    private BigDecimal price;
    @Size(max = 500, message = ITEM_DESCRIPTION_SHOULD_BE_LESS_THAN_500_CHARS)
    @NotNull(message = ITEM_DESCRIPTION_SHOULD_PRESENT)
    private String description;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime creationDate;
    private byte[] imageData;
    @NotEmpty(message = ITEM_SHOULD_HAVE_AT_LEAST_1_CATEGORY)
    private Set<CategoryDTO> categories;
}