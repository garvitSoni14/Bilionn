package in.Garvit.Bilionn.io;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequest {


    private String name;
    private String description;
    private String bgColor;
//    private String imgUrl;
    private MultipartFile img;
}
