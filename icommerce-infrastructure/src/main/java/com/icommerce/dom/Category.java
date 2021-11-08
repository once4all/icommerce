package com.icommerce.dom;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@SequenceGenerator(name = "category_gen", sequenceName = "category_seq", allocationSize = 1)
@Table(name = "category")
public class Category implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "category_gen")
    @Column(name = "id")
    private long id;

    @Column(name = "name", length = 2000)
    private String name;

    @Column(name = "active")
    private boolean active;
}
