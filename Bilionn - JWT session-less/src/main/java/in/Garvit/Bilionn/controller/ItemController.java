package in.Garvit.Bilionn.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import in.Garvit.Bilionn.entity.ItemEntity;
import in.Garvit.Bilionn.io.ItemRequest;
import in.Garvit.Bilionn.io.ItemResponse;
import in.Garvit.Bilionn.repository.ItemRepository;
import in.Garvit.Bilionn.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.print.attribute.standard.Media;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final ItemRepository itemRepository;


//    @PostMapping("/admin/items")
//    @ResponseStatus(HttpStatus.CREATED)
//    public ItemResponse addItem(@RequestPart("item")String itemString,
//                                @RequestPart("file")MultipartFile file){
//        ObjectMapper objectMapper = new ObjectMapper();
//        ItemRequest itemRequest = null;
//        try{
//            itemRequest = objectMapper.readValue(itemString, ItemRequest.class);
//            return itemService.add(itemRequest, file);
//        }
//        catch(JsonProcessingException ex){
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error occurred while processing the json");
//        }
//    }

    @PostMapping(value = "/admin/items", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ItemResponse addItem(@ModelAttribute ItemRequest request){
        return itemService.add(request);
    }

    @GetMapping("/items")
    public List<ItemResponse> readItems(){
        return itemService.fetchItems();
    }

    @DeleteMapping("/admin/items/{itemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeItem(@PathVariable String itemId){
        try{
            itemService.deleteItem(itemId);
        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found");
        }
    }

    @GetMapping("/items/{itemId}/image")
    public ResponseEntity<byte[]> getItemImage(@PathVariable String itemId){
        System.out.println("trying to get image...");
        ItemEntity item = itemRepository.findByItemId(itemId)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Image not found"));
        byte[] image = item.getImage();
        if(image == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No image uploaded for this item");
        }
        String mimeType = detectMimeType(image);
        if(mimeType == null){mimeType = MediaType.APPLICATION_OCTET_STREAM_VALUE;}
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(mimeType))
                .body(image);
    }
    private String detectMimeType(byte[] imageBytes){
        try{
            String mimeType = URLConnection.guessContentTypeFromStream(new ByteArrayInputStream(imageBytes));
            return mimeType;
        }catch (IOException e){
            return null;
        }
    }
}
