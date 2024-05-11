package org.techstage.backendapplication.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;

@Entity
@Getter
@NoArgsConstructor
@Setter
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = {"email", "telephone"}))
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String surname;
    private String telephone;
    private String email;
    private String password;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"
            )
    )
    private Collection<Role> roles;

    public User(String name, String surname, String telephone, String email, String password, Collection<Role> roles) {
        this.name = name;
        this.surname = surname;
        this.telephone = telephone;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }
}
