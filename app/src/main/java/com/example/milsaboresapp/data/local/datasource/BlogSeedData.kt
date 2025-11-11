package com.example.milsaboresapp.data.local.datasource

import com.example.milsaboresapp.domain.model.BlogPost

object BlogSeedData {
    val posts = listOf(
        BlogPost(
            id = "blog-recetas-1",
            category = "Recetas",
            title = "Caso curioso 1: El misterio de la torta que desaparece",
            excerpt = "Aprende los secretos detras de nuestra torta de chocolate mas vendida y como replicarla en casa.",
            imageResName = "blog1"
        ),
        BlogPost(
            id = "blog-tips-1",
            category = "Tips",
            title = "Caso curioso 2: El betun perfecto para climas calidos",
            excerpt = "Te ensenamos trucos profesionales para mantener tus decoraciones impecables por mas tiempo.",
            imageResName = "blog2"
        )
    )
}
