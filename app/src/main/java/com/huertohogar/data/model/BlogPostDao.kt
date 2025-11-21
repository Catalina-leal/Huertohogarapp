package com.huertohogar.data.model

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BlogPostDao {
    @Query("SELECT * FROM blog_posts ORDER BY publishedDate DESC")
    fun getAllBlogPosts(): Flow<List<BlogPost>>
    
    @Query("SELECT * FROM blog_posts WHERE id = :postId")
    suspend fun getBlogPostById(postId: String): BlogPost?
    
    @Query("SELECT * FROM blog_posts WHERE category = :category ORDER BY publishedDate DESC")
    fun getBlogPostsByCategory(category: String): Flow<List<BlogPost>>
}
