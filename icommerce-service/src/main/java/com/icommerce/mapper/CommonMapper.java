package com.icommerce.mapper;

import com.icommerce.category.model.*;
import com.icommerce.dom.*;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface CommonMapper {

    List<CategoryDTO> categoriesToResponse(List<Category> categories);

    List<BrandDTO> brandsToResponse(List<Brand> brands);

    List<ColourDTO> coloursToResponse(List<Colour> colours);

    List<ProductDTO> productToResponse(List<Product> products);

    List<CartItemDTO> cartItemsToResponse(List<CartItem> cartItems);

    CartDTO cartToResponse(Cart cart);

    BookingDTO bookingToResponse(Booking booking);

    List<BookingDTO> bookingToResponse(List<Booking> booking);
}
