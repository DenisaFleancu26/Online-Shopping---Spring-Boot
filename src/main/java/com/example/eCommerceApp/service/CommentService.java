package com.example.eCommerceApp.service;

import com.example.eCommerceApp.dto.CommentDTO;
import com.example.eCommerceApp.exception.ResourceNotFoundException;
import com.example.eCommerceApp.mapper.CommentMapper;
import com.example.eCommerceApp.model.Comment;
import com.example.eCommerceApp.model.Product;
import com.example.eCommerceApp.model.User;
import com.example.eCommerceApp.repository.CommentRepository;
import com.example.eCommerceApp.repository.ProductRepository;
import com.example.eCommerceApp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final ProductRepository productRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;

    public CommentDTO addComment(Long productId, Long userId, CommentDTO commentDTO){
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Comment comment = commentMapper.toEntity(commentDTO);
        comment.setUser(user);
        comment.setProduct(product);
        Comment savedComment = commentRepository.save(comment);
        return commentMapper.toDTO(savedComment);
    }

    public List<CommentDTO> getCommentsByProduct(Long productId){
        List<Comment> comments = commentRepository.findByProductId(productId);

        return commentMapper.toCommentDTOs(comments);
    }


}
