package br.gov.sp.fatec.infra.database.jpa.models;

import br.gov.sp.fatec.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "users")
public class UserJpa {

    @Id
    @Column(name = "USER_ID", nullable = false)
    private UUID id;

    @Column(name = "USER_EMAIL", nullable = false, unique = true)
    private String email;

    @Column(name = "USER_PASSWORD", nullable = false)
    private String password;

    @Column(name = "USER_NAME", nullable = false)
    private String name;

    public UserJpa() {
    }

    public UserJpa(UUID id, String email, String password, String name) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User toDomain() {
        return new User(
            this.id.toString(),
            this.name,
            this.email,
            this.password
        );
    }

    public static UserJpa fromDomain(User user) {
        return new UserJpa(
            UUID.fromString(user.id()),
            user.email(),
            user.password(),
            user.name()
        );
    }
}
