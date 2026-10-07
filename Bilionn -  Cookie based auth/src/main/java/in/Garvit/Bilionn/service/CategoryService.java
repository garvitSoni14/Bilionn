package in.Garvit.Bilionn.service;

import in.Garvit.Bilionn.io.CategoryRequest;
import in.Garvit.Bilionn.io.CategoryResponse;
import java.util.List;

public interface CategoryService {

    CategoryResponse add(CategoryRequest request);

    List<CategoryResponse> read();

    void delete(String categoryId);
}
