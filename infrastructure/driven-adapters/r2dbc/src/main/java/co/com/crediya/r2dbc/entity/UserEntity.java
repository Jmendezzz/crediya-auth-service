package co.com.crediya.r2dbc.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@Data
@Table(name = "users")
public class UserEntity {
    @Id
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
    private Long roleId;
}
