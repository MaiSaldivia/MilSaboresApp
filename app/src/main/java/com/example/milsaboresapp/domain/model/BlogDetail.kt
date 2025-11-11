package com.example.milsaboresapp.domain.model

data class BlogDetail(
    val id: String,
    val category: String,
    val title: String,
    val subtitle: String,
    val heroImage: String,
    val publishDate: String,
    val readingTime: String,
    val introParagraphs: List<String>,
    val sections: List<BlogDetailSection>,
    val highlights: List<BlogDetailHighlight>,
    val tips: List<String>,
    val conclusionTitle: String,
    val conclusionBody: String,
    val closingCta: String,
    val relatedPosts: List<BlogDetailRelated>
)
