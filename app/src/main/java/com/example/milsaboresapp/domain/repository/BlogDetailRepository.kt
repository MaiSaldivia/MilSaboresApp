package com.example.milsaboresapp.domain.repository

import com.example.milsaboresapp.domain.model.BlogDetail
import kotlinx.coroutines.flow.Flow

interface BlogDetailRepository {
    fun observeDetail(postId: String): Flow<BlogDetail?>
}
