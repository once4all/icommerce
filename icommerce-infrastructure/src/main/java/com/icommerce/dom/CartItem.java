package com.icommerce.dom;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "cart_item")
@SequenceGenerator(name = "cart_item_gen", sequenceName = "cart_item_seq", allocationSize = 1)
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartItem {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cart_item_gen")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "colour_id")
    private Colour colour;

    @Column(name = "quantity", nullable = false)
    private int quantity;


}
