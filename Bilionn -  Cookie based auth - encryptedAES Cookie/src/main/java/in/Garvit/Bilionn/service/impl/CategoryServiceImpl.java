package in.Garvit.Bilionn.service.impl;

import in.Garvit.Bilionn.entity.CategoryEntity;
import in.Garvit.Bilionn.io.CategoryRequest;
import in.Garvit.Bilionn.io.CategoryResponse;
import in.Garvit.Bilionn.repository.CategoryRepository;
import in.Garvit.Bilionn.repository.ItemRepository;
import in.Garvit.Bilionn.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {


    private final CategoryRepository categoryRepository;

    private final ItemRepository itemRepository;

    @Override
    public CategoryResponse add(CategoryRequest request) {
        CategoryEntity newCategory = convertToEntity(request);
        newCategory = categoryRepository.save(newCategory);

        return convertToResponse(newCategory);

    }

    @Override
    public List<CategoryResponse> read() {
        return categoryRepository.findAll()
                .stream()
                .map(categoryEntity -> convertToResponse(categoryEntity))
                .collect(Collectors.toList());
    }

    @Override
    public void delete(String categoryId) {
       CategoryEntity existingCategory = categoryRepository.findByCategoryId(categoryId)
               .orElseThrow(()-> new RuntimeException("Category not found: "+categoryId));
       categoryRepository.delete(existingCategory);
    }

    private CategoryResponse convertToResponse(CategoryEntity newCategory) {
        Integer itemsCount = itemRepository.countByCategoryId(newCategory.getId());
        return CategoryResponse.builder()
                .categoryId(newCategory.getCategoryId())
                .name(newCategory.getName())
                .description(newCategory.getDescription())
                .bgColor(newCategory.getBgColor())
                .imgUrl("/api/v1.0/categories/"+ newCategory.getCategoryId() +"/image")
                .createdAt(newCategory.getCreatedAt())
                .updatedAt(newCategory.getUpdatedAt())
                .items(itemsCount)
                .build();
    }

    private CategoryEntity convertToEntity(CategoryRequest request) {

        byte[] imageBytes = null;
        try{
            if(request.getImg()!=null && !request.getImg().isEmpty()){
                imageBytes = request.getImg().getBytes();
            }
        }catch (Exception e){
            throw new RuntimeException("Error reading uploaded image", e);
        }

        return CategoryEntity.builder()
                .categoryId(UUID.randomUUID().toString())
                .name(request.getName())
                .description(request.getDescription())
                .bgColor(request.getBgColor())
//                .imgUrl(request.getImgUrl())
                .image(imageBytes)
                .build();
    }
}
