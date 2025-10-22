package praktikum.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Модель данных для входа пользователя.
 * Lombok автоматически создаёт геттеры, сеттеры, конструкторы и методы equals/hashCode/toString.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginCredentials {
    private String email;
    private String password;
}