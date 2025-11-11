package com.example.milsaboresapp.domain.repository

import com.example.milsaboresapp.domain.model.BlogPost
import kotlinx.coroutines.flow.Flow

interface BlogRepository {
    fun observeHighlights(limit: Int = 4): Flow<List<BlogPost>>
    fun observePosts(): Flow<List<BlogPost>>
    fun observeCategories(): Flow<List<String>>
}
