package com.example.lab3.service;

import com.example.lab3.dto.CategoryDto;
import com.example.lab3.entity.Category;
import com.example.lab3.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepo;

    @Transactional
    public CategoryDto create(CategoryDto dto) {
        Category category = new Category(null, dto.name(), null);
        Category saved = categoryRepo.save(category);
        return new CategoryDto(saved.getId(), saved.getName());
    }

    @Transactional(readOnly = true)
    public CategoryDto getById(Long id) {
        Category category = categoryRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + id));
        return new CategoryDto(category.getId(), category.getName());
    }

    @Transactional(readOnly = true)
    public List<CategoryDto> getAll() {
        return categoryRepo.findAll().stream()
                .map(c -> new CategoryDto(c.getId(), c.getName()))
                .toList();
    }

    @Transactional
    public CategoryDto update(Long id, CategoryDto dto) {
        Category existing = categoryRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + id));
        existing.setName(dto.name());
        Category updated = categoryRepo.save(existing);
        return new CategoryDto(updated.getId(), updated.getName());
    }

    @Transactional
    public void delete(Long id) {
        if (!categoryRepo.existsById(id)) {
            throw new EntityNotFoundException("Category not found with id: " + id);
        }
        categoryRepo.deleteById(id);
    }
}
