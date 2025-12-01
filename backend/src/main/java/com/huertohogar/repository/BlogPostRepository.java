package com.huertohogar.repository;

import com.huertohogar.model.entity.BlogPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BlogPostRepository extends JpaRepository<BlogPost, String> {
    
    List<BlogPost> findByCategoryOrderByPublishedDateDesc(String category);
    
    @Query("SELECT bp FROM BlogPost bp ORDER BY bp.publishedDate DESC")
    List<BlogPost> findAllOrderByPublishedDateDesc();
    
    Optional<BlogPost> findByIdAndPublishedDateIsNotNull(String id);
}
