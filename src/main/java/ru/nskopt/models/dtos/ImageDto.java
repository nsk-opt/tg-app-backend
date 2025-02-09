package ru.nskopt.models.dtos;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ImageDto {
  @Pattern(regexp = "^(http|https)://.*$",
      message = "Link must be a valid URL starting with http or https")
  private String link;
}
