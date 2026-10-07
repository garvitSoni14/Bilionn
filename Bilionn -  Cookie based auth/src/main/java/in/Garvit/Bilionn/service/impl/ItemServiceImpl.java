package in.Garvit.Bilionn.service.impl;

import in.Garvit.Bilionn.entity.CategoryEntity;
import in.Garvit.Bilionn.entity.ItemEntity;
import in.Garvit.Bilionn.io.ItemRequest;
import in.Garvit.Bilionn.io.ItemResponse;
import in.Garvit.Bilionn.repository.CategoryRepository;
import in.Garvit.Bilionn.repository.ItemRepository;
import in.Garvit.Bilionn.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

//    private final FileUploadService fileUploadService;

    private final CategoryRepository categoryRepository;

    private final ItemRepository itemRepository;

//    @Override
//    public ItemResponse add(ItemRequest request, MultipartFile file) {
//        String imgUrl = fileUploadService.uploadFile(file);
//        ItemEntity newItem = convertToEntity(request);
//        CategoryEntity existingCategory = categoryRepository.findByCategoryId(request.getCategoryId())
//                .orElseThrow(()-> new RuntimeException("Category not found: "+request.getCategoryId()));
//        newItem.setCategory(existingCategory);
//        newItem.setImgUrl(imgUrl);
//        newItem = itemRepository.save(newItem);
//       return convertToResponse(newItem);
//
//    }

    @Override
    public ItemResponse add(ItemRequest request) {
        ItemEntity newItem = convertToEntity(request);

        CategoryEntity existingCategory = categoryRepository.findByCategoryId(request.getCategoryId())
                .orElseThrow(()-> new RuntimeException("Category not found: "+request.getCategoryId()));

        newItem.setCategory(existingCategory);

        newItem = itemRepository.save(newItem);
        return convertToResponse(newItem);

    }

//    private ItemResponse convertToResponse(ItemEntity newItem) {
//        return ItemResponse.builder()
//            .itemId(newItem.getItemId())
//            .name(newItem.getName())
//            .description(newItem.getDescription())
//            .price(newItem.getPrice())
//            .imgUrl(newItem.getImgUrl())
//            .categoryName(newItem.getCategory().getCategoryId())
//            .createdAt(newItem.getCreatedAt())
//            .updatedAt(newItem.getUpdatedAt())
//            .build();
//    }
private ItemResponse convertToResponse(ItemEntity newItem) {
    return ItemResponse.builder()
            .itemId(newItem.getItemId())
            .name(newItem.getName())
            .description(newItem.getDescription())
            .price(newItem.getPrice())
            .imgUrl("/api/v1.0/items/"+newItem.getItemId()+"/image")
            .categoryId(newItem.getCategory().getCategoryId())
            .categoryName(newItem.getCategory().getName())
            .createdAt(newItem.getCreatedAt())
            .updatedAt(newItem.getUpdatedAt())
            .build();
}

//    private ItemEntity convertToEntity(ItemRequest request) {
//        return ItemEntity.builder()
//                .itemId(UUID.randomUUID().toString())
//                .name(request.getName())
//                .description(request.getDescription())
//                .price(request.getPrice())
//                .build();
//    }

    private ItemEntity convertToEntity(ItemRequest request) {
        byte[] imageBytes = null;
        try{
            if(request.getImage()!=null &&  !request.getImage().isEmpty()){
                imageBytes = request.getImage().getBytes();
            }
        }
        catch (Exception e){
            throw new RuntimeException("Error reading uploaded image", e);
        }
        return ItemEntity.builder()
                .itemId(UUID.randomUUID().toString())
                .name(request.getName())
                .description(request.getDescription())
                .image(imageBytes)
                .price(request.getPrice())
                .build();
    }

    @Override
    public List<ItemResponse> fetchItems() {
        return itemRepository.findAll()
                .stream()
                .map(itemEntity -> convertToResponse(itemEntity))
                .collect(Collectors.toList());
    }

//    @Override
//    public void deleteItem(String itemId) {
//        itemRepository.findByItemId(itemId)
//                .orElseThrow(()-> new RuntimeException("Item not found: "+itemId));
//        boolean isFileDelete = fileUploadService.deleteFile(existingItem.getImgUrl());
//        if(isFileDelete){
//            itemRepository.delete(existingItem);
//        }
//        else{
//            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to delete the image");
//        }
//    }

    @Override
    public void deleteItem(String itemId) {
        ItemEntity existingItem = itemRepository.findByItemId(itemId)
                .orElseThrow(()-> new RuntimeException("Item not found: "+itemId));
            itemRepository.delete(existingItem);
    }
}
