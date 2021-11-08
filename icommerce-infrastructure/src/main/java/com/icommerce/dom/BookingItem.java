package com.icommerce.dom;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "booking_item")
@SequenceGenerator(name = "booking_item_gen", sequenceName = "booking_item_seq", allocationSize = 1)
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingItem {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "booking_item_gen")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "booking_id")
    private Booking booking;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "colour_id")
    private Colour colour;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "total_price", nullable = false)
    private Double totalPrice;


}
