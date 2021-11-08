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
@SequenceGenerator(name = "colour_gen", sequenceName = "colour_seq", allocationSize = 1)
@Table(name = "colour")
public class Colour implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "colour_gen")
    @Column(name = "id")
    private long id;

    @Column(name = "name", length = 2000)
    private String name;

    @Column(name = "active")
    private boolean active;
}
