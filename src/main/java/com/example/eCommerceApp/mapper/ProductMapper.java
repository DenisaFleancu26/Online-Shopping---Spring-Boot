package com.example.eCommerceApp.mapper;

import com.example.eCommerceApp.dto.CommentDTO;
import com.example.eCommerceApp.dto.ProductDTO;
import com.example.eCommerceApp.model.Comment;
import com.example.eCommerceApp.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {

   // @Mapping(target = "productId", source = "product.id")
   // @Mapping(target = "comments", source = "comments")
    @Mapping(target = "image", source = "image")
    ProductDTO toDTO(Product product);

   // @Mapping(target = "product.id", source = "productId")
   // @Mapping(target = "comments", source = "comments")
    @Mapping(target = "image", source = "image")
    Product toEntity(ProductDTO productDTO);


    @Mapping(target = "userId", source = "user.id")
    CommentDTO toDTO(Comment comment);

    @Mapping(target = "user.id", source = "userId")
    @Mapping(target = "product", ignore = true)
    Comment toEntity(CommentDTO commentDTO);

}
