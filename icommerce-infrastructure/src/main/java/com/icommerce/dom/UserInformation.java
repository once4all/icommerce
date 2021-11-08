package com.icommerce.dom;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "user_information")
@Getter
@Setter
@SequenceGenerator(name = "user_information_gen", sequenceName = "user_information_seq", allocationSize = 1)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_information_gen")
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "active")
    private boolean active;

}
