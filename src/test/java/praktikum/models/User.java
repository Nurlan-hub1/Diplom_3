package praktikum.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Модель данных пользователя для сериализации/десериализации.
 * Lombok автоматически создаёт геттеры, сеттеры, конструкторы и методы equals/hashCode/toString.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String email;
    private String password;
    private String name;
}