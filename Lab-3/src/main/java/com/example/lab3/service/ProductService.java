package com.example.lab3.service;

import com.example.lab3.dto.ProductDto;
import com.example.lab3.entity.Category;
import com.example.lab3.entity.Product;
import com.example.lab3.repository.CategoryRepository;
import com.example.lab3.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepo;
    private final CategoryRepository categoryRepo;

    @Transactional
    public ProductDto create(ProductDto dto) {
        Category cat = null;
        if (dto.categoryId() != null) {
            cat = categoryRepo.findById(dto.categoryId())
                    .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + dto.categoryId()));
        }
        Product p = new Product(null, dto.name(), dto.price(), cat);
        Product saved = productRepo.save(p);
        return toDto(saved);
    }

    @Transactional(readOnly = true)
    public ProductDto getById(Long id) {
        Product p = productRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));
        return toDto(p);
    }

    @Transactional(readOnly = true)
    public List<ProductDto> getAll() {
        return productRepo.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ProductDto> searchByName(String keyword) {
        return productRepo.findByNameContainingIgnoreCase(keyword).stream()
                .map(this::toDto)
                .toList();
    }

    @Transactional
    public ProductDto update(Long id, ProductDto dto) {
        Product existing = productRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));

        Category cat = null;
        if (dto.categoryId() != null) {
            cat = categoryRepo.findById(dto.categoryId())
                    .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + dto.categoryId()));
        }

        existing.setName(dto.name());
        existing.setPrice(dto.price());
        existing.setCategory(cat);

        Product updated = productRepo.save(existing);
        return toDto(updated);
    }

    @Transactional
    public void delete(Long id) {
        if (!productRepo.existsById(id)) {
            throw new EntityNotFoundException("Product not found with id: " + id);
        }
        productRepo.deleteById(id);
    }

    private ProductDto toDto(Product product) {
        Long categoryId = product.getCategory() != null ? product.getCategory().getId() : null;
        return new ProductDto(product.getId(), product.getName(), product.getPrice(), categoryId);
    }
}
