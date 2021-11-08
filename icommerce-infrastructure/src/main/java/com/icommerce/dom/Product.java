package com.icommerce.dom;

import lombok.*;
import org.hibernate.annotations.WhereJoinTable;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@SequenceGenerator(name = "product_gen", sequenceName = "product_seq", allocationSize = 1)
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "brand_gen")
    @Column(name = "id")
    private long id;

    @Column(name = "code", length = 100)
    private String code;

    @Column(name = "name", length = 1000)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private Double price;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    @Column(name = "created_date")
    private Instant createdDate;

    @Column(name = "modified_date")
    private Instant modifiedDate;

    @WhereJoinTable(clause = "active = true")
    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "product_brand",
            joinColumns = {@JoinColumn(name = "product_id")},
            inverseJoinColumns = {@JoinColumn(name = "brand_id")}
    )
    List<Brand> brands;

    @WhereJoinTable(clause = "active = true")
    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "product_category",
            joinColumns = {@JoinColumn(name = "product_id")},
            inverseJoinColumns = {@JoinColumn(name = "category_id")}
    )
    List<Category> categories;

    @WhereJoinTable(clause = "active = true")
    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "product_colour",
            joinColumns = {@JoinColumn(name = "product_id")},
            inverseJoinColumns = {@JoinColumn(name = "colour_id")}
    )
    List<Colour> colours;

}
