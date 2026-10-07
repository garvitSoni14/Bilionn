package in.Garvit.Bilionn.io;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CategoryResponse {

    private String categoryId;
    private String name;
    private String description;
    private String bgColor;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String imgUrl;
    private Integer items;

}
