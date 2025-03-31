package com.example.eCommerceApp.mapper;

import com.example.eCommerceApp.dto.CommentDTO;
import com.example.eCommerceApp.model.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "userId", source = "user.id")
    CommentDTO toDTO(Comment comment);

    @Mapping(target = "user.id", source = "userId")
    @Mapping(target = "product", ignore = true)
    Comment toEntity(CommentDTO commentDTO);

    List<CommentDTO> toCommentDTOs(List<Comment> comments);
    List<Comment> toCommentEntities(List<CommentDTO> commentDTOs);
}
