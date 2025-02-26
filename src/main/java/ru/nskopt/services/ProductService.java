package ru.nskopt.services;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.nskopt.exceptions.ResourceNotFoundException;
import ru.nskopt.mappers.Mapper;
import ru.nskopt.models.entities.Category;
import ru.nskopt.models.entities.Product;
import ru.nskopt.models.requests.UpdateProductRequest;
import ru.nskopt.repositories.ProductRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

  private final ProductRepository productRepository;
  private final Mapper<Product, UpdateProductRequest> productMapper;
  private final CategoryService categoryService;

  public void assertProductExists(Long id) {
    if (!productRepository.existsById(id)) throw new ResourceNotFoundException(id);
  }

  public List<Product> findAll() {
    return productRepository.findAll();
  }

  public Product findById(Long id) {
    return productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
  }

  public Product save(UpdateProductRequest updateProductRequest) {
    log.info("Save {}", updateProductRequest);
    return productRepository.save(productMapper.map(updateProductRequest));
  }

  public Product update(Long id, UpdateProductRequest updateProductRequest) {
    return productRepository
        .findById(id)
        .map(
            existingProduct -> {
              productMapper.update(existingProduct, updateProductRequest);
              log.info("Updating product with ID {}: {}", id, existingProduct);
              return productRepository.save(existingProduct);
            })
        .orElseThrow(() -> new ResourceNotFoundException(id));
  }

  public void deleteById(Long id) {
    assertProductExists(id);
    log.info("Delete product with id {}", id);
    productRepository.deleteById(id);
  }

  public void updateCategories(Long productId, List<Long> categoryIds) {
    Product product = findById(productId);

    product.getCategories().clear();

    if (categoryIds.isEmpty()) log.info("Removed all categories for product ID {}", productId);

    Set<Category> categories =
        categoryIds.stream().map(categoryService::findById).collect(Collectors.toSet());

    product.getCategories().addAll(categories);
    productRepository.save(product);

    log.info("Updated categories for product ID {}: {}", productId, categoryIds);
  }

  public Set<Category> getCategoriesByProductId(Long productId) {
    assertProductExists(productId);

    return productRepository.findCategoriesByProductId(productId);
  }
}
