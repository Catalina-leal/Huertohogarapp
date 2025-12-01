package com.huertohogar.presentation.blog

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.huertohogar.data.db.AppDatabase
import com.huertohogar.data.model.BlogPost
import com.huertohogar.data.repository.BlogRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class BlogUiState(
    val posts: List<BlogPost> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class BlogViewModel(
    private val blogRepository: BlogRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(BlogUiState(isLoading = true))
    val uiState: StateFlow<BlogUiState> = _uiState.asStateFlow()

    init {
        loadBlogPosts()
    }

    private fun loadBlogPosts() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                blogRepository.getAllBlogPosts().collect { posts ->
                    _uiState.value = _uiState.value.copy(
                        posts = posts,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    class Factory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(BlogViewModel::class.java)) {
                val database = AppDatabase.getDatabase(application)
                val blogPostDao = database.blogPostDao()
                val blogRepository = BlogRepository(blogPostDao)

                @Suppress("UNCHECKED_CAST")
                return BlogViewModel(blogRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
