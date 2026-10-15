package in.Garvit.Bilionn.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {

    @Column(name = "house_no", length = 50)
    @Size(max = 50, message = "House number must not exceed 50 characters")
    private String houseNo;

    @Column(name = "street", length = 150)
    @NotBlank(message = "Street is required")
    @Size(max = 150, message = "Street must not exceed 150 characters")
    private String street;

    @Column(name = "area", length = 150)
    @Size(max = 150, message = "Area must not exceed 150 characters")
    private String area;

    @Column(name = "city", length = 100)
    @NotBlank(message = "City is required")
    @Size(max = 100, message = "City must not exceed 100 characters")
    private String city;

    @Column(name = "state", length = 100)
    @NotBlank(message = "State is required")
    @Size(max = 100, message = "State must not exceed 100 characters")
    private String state;

    @Column(name = "pincode", length = 10)
    @NotBlank(message = "Pincode is required")
    @Pattern(
            regexp = "^[1-9]\\d{5}$",
            message = "Pincode must be 6 digits"
    )    private String pincode;
}
