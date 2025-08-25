package co.com.crediya.model.user;
import co.com.crediya.model.role.Role;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
//import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@Builder(toBuilder = true)
public class User {
    private Long id;
    private String firstName;
    private String lastName;
    private String identityNumber;
    private String phoneNumber;
    private LocalDate birthdate;
    private String address;
    private Long baseSalary;
    private String email;
    private String password;
    private Role role;
}
