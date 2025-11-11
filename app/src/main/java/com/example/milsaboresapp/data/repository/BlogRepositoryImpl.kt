package com.example.milsaboresapp.data.repository

import com.example.milsaboresapp.data.local.datasource.BlogSeedData
import com.example.milsaboresapp.domain.model.BlogPost
import com.example.milsaboresapp.domain.repository.BlogRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class BlogRepositoryImpl : BlogRepository {

    private val postsState = MutableStateFlow(
        BlogSeedData.posts.associateBy { it.id }
    )

    override fun observeHighlights(limit: Int): Flow<List<BlogPost>> =
        postsState.map { map ->
            map.values.take(limit)
        }

    override fun observePosts(): Flow<List<BlogPost>> =
        postsState.map { map -> map.values.toList() }

    override fun observeCategories(): Flow<List<String>> =
        postsState.map { map ->
            map.values.map { it.category }.distinct()
        }
}
