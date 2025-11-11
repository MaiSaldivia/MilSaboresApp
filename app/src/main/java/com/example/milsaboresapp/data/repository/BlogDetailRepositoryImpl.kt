package com.example.milsaboresapp.data.repository

import com.example.milsaboresapp.data.local.datasource.BlogDetailSeedData
import com.example.milsaboresapp.domain.model.BlogDetail
import com.example.milsaboresapp.domain.repository.BlogDetailRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class BlogDetailRepositoryImpl : BlogDetailRepository {

    private val detailsState = MutableStateFlow(BlogDetailSeedData.details)

    override fun observeDetail(postId: String): Flow<BlogDetail?> =
        detailsState.map { it[postId] }
}
