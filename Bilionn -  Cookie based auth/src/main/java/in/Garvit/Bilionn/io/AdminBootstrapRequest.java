package in.Garvit.Bilionn.io;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminBootstrapRequest {
    private String name;
    private String email;
    private String password;
}
