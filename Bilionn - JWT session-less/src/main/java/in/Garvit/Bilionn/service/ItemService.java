package in.Garvit.Bilionn.service;

import in.Garvit.Bilionn.io.ItemRequest;
import in.Garvit.Bilionn.io.ItemResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ItemService {

    ItemResponse add(ItemRequest request);

    List<ItemResponse> fetchItems();

    void deleteItem(String itemId);

}
