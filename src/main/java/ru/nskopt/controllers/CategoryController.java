package ru.nskopt.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.nskopt.models.entities.Category;
import ru.nskopt.models.requests.UpdateCategoryRequest;
import ru.nskopt.services.CategoryService;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Validated
@Tag(name = "Category Controller", description = "Управление категориями товаров")
public class CategoryController {

  private final CategoryService categoryService;

  @GetMapping
  @Operation(
      summary = "Получить все категории",
      description = "Возвращает список всех доступных категорий.")
  public List<Category> getAllCategories() {
    return categoryService.findAll();
  }

  @GetMapping("/{id}")
  @Operation(
      summary = "Получить категорию по ID",
      description = "Возвращает категорию по её уникальному идентификатору.")
  public Category getCategoryById(
      @Parameter(description = "ID категории", example = "1") @PathVariable Long id) {
    return categoryService.findById(id);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(
      summary = "Создать новую категорию",
      description = "Создаёт новую категорию на основе переданных данных.")
  public Category createCategory(@Valid @RequestBody UpdateCategoryRequest updateCategoryRequest) {
    return categoryService.save(updateCategoryRequest);
  }

  @PutMapping("/{id}")
  @Operation(
      summary = "Обновить категорию по ID",
      description = "Обновляет данные категории по её уникальному идентификатору.")
  public Category updateCategory(
      @Parameter(description = "ID категории", example = "1") @PathVariable Long id,
      @Valid @RequestBody UpdateCategoryRequest updateCategoryRequest) {
    return categoryService.update(id, updateCategoryRequest);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @Operation(
      summary = "Удалить категорию по ID",
      description = "Удаляет категорию по её уникальному идентификатору.")
  public void deleteCategory(
      @Parameter(description = "ID категории", example = "1") @PathVariable Long id) {
    categoryService.deleteById(id);
  }

  @PutMapping("/{categoryId}/images")
  @ResponseStatus(HttpStatus.OK)
  @Operation(
      summary = "Обновить изображения категории",
      description = "Обновляет список изображений, связанных с категорией.")
  public void updateImages(
      @Parameter(description = "ID категории", example = "1") @PathVariable Long categoryId,
      @RequestBody List<Long> imageIds) {
    categoryService.updateImages(categoryId, imageIds);
  }

  @GetMapping("/{categoryId}/images")
  @ResponseStatus(HttpStatus.OK)
  @Operation(
      summary = "Получить ID изображений категории",
      description = "Возвращает список ID изображений, связанных с категорией.")
  public List<Long> getImagesId(
      @Parameter(description = "ID категории", example = "1") @PathVariable Long categoryId) {
    return categoryService.getImagesIds(categoryId);
  }
}
