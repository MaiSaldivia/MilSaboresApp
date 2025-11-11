package com.example.milsaboresapp.presentation.blogdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.milsaboresapp.domain.model.BlogDetail
import com.example.milsaboresapp.domain.repository.BlogDetailRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class BlogDetailViewModel(
    initialPostId: String,
    private val blogDetailRepository: BlogDetailRepository
) : ViewModel() {

    data class UiState(
        val isLoading: Boolean = true,
        val currentPostId: String = "",
        val detail: BlogDetail? = null,
        val notFound: Boolean = false
    )

    private val postIdState = MutableStateFlow(initialPostId)
    private val _uiState = MutableStateFlow(UiState(currentPostId = initialPostId))
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            postIdState
                .onEach { postId ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = true,
                        currentPostId = postId,
                        notFound = false
                    )
                }
                .flatMapLatest { postId ->
                    blogDetailRepository.observeDetail(postId).map { detail -> postId to detail }
                }
                .collect { (postId, detail) ->
                    if (detail == null) {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            detail = null,
                            notFound = true,
                            currentPostId = postId
                        )
                    } else {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            detail = detail,
                            notFound = false,
                            currentPostId = postId
                        )
                    }
                }
        }
    }

    fun openPost(postId: String) {
        if (postIdState.value == postId) {
            return
        }
        postIdState.value = postId
    }

    companion object {
        fun provideFactory(
            postId: String,
            blogDetailRepository: BlogDetailRepository
        ) = viewModelFactory {
            initializer {
                BlogDetailViewModel(postId, blogDetailRepository)
            }
        }
    }
}
