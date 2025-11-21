package com.huertohogar.service;

import com.huertohogar.model.dto.BlogPostDTO;
import com.huertohogar.model.entity.BlogPost;
import com.huertohogar.repository.BlogPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BlogPostService {
    
    private final BlogPostRepository blogPostRepository;
    
    public List<BlogPostDTO> getAllPosts() {
        return blogPostRepository.findAllOrderByPublishedDateDesc().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    public BlogPostDTO getPostById(String id) {
        BlogPost post = blogPostRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post no encontrado: " + id));
        return toDTO(post);
    }
    
    public List<BlogPostDTO> getPostsByCategory(String category) {
        return blogPostRepository.findByCategoryOrderByPublishedDateDesc(category).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    public BlogPostDTO createPost(BlogPostDTO postDTO) {
        BlogPost post = toEntity(postDTO);
        if (post.getPublishedDate() == null) {
            post.setPublishedDate(LocalDateTime.now());
        }
        BlogPost saved = blogPostRepository.save(post);
        return toDTO(saved);
    }
    
    public BlogPostDTO updatePost(String id, BlogPostDTO postDTO) {
        BlogPost post = blogPostRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post no encontrado: " + id));
        
        post.setTitle(postDTO.getTitle());
        post.setContent(postDTO.getContent());
        post.setAuthor(postDTO.getAuthor());
        post.setImageUrl(postDTO.getImageUrl());
        post.setCategory(postDTO.getCategory());
        post.setReadTime(postDTO.getReadTime());
        
        BlogPost updated = blogPostRepository.save(post);
        return toDTO(updated);
    }
    
    public void deletePost(String id) {
        if (!blogPostRepository.existsById(id)) {
            throw new RuntimeException("Post no encontrado: " + id);
        }
        blogPostRepository.deleteById(id);
    }
    
    private BlogPostDTO toDTO(BlogPost post) {
        BlogPostDTO dto = new BlogPostDTO();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setContent(post.getContent());
        dto.setAuthor(post.getAuthor());
        dto.setImageUrl(post.getImageUrl());
        dto.setPublishedDate(post.getPublishedDate());
        dto.setCategory(post.getCategory());
        dto.setReadTime(post.getReadTime());
        dto.setCreatedAt(post.getCreatedAt());
        return dto;
    }
    
    private BlogPost toEntity(BlogPostDTO dto) {
        BlogPost post = new BlogPost();
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        post.setAuthor(dto.getAuthor());
        post.setImageUrl(dto.getImageUrl());
        post.setPublishedDate(dto.getPublishedDate());
        post.setCategory(dto.getCategory());
        post.setReadTime(dto.getReadTime());
        return post;
    }
}
