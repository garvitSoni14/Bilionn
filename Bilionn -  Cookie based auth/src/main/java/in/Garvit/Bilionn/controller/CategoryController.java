package in.Garvit.Bilionn.controller;

import in.Garvit.Bilionn.entity.CategoryEntity;
import in.Garvit.Bilionn.io.CategoryRequest;
import in.Garvit.Bilionn.io.CategoryResponse;
import in.Garvit.Bilionn.repository.CategoryRepository;
import in.Garvit.Bilionn.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.util.List;

@RestController
//@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryRepository categoryRepository;

    @PostMapping(value = "/admin/categories", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponse addCategory(@ModelAttribute CategoryRequest request){
        return categoryService.add(request);
    }

    @GetMapping("/categories")
    public List<CategoryResponse> fetchCategories(){
        return categoryService.read();
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/admin/categories/{categoryId}")
    public void remove(@PathVariable String categoryId){
        try {
            categoryService.delete(categoryId);
        }
        catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/categories/{categoryId}/image")
    public ResponseEntity<byte[]> getCategoryImage(@PathVariable String categoryId){
        System.out.println("finding the image");
        CategoryEntity category = categoryRepository.findByCategoryId(categoryId)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Image not found"));
        byte[] image = category.getImage();

        if (image == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(("No image uploaded for this category").getBytes());
        }
        String mimeType = detectMimeType(image);
        if(mimeType==null){
            mimeType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(mimeType))
                        .body(image);
    }
    private String detectMimeType(byte[] imageBytes){
        try{
            String mimeType = URLConnection.guessContentTypeFromStream(new ByteArrayInputStream(imageBytes));
            return mimeType;
        }catch(IOException e){
            return null;
        }
    }

}
