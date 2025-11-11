package com.example.milsaboresapp.data.local.datasource

import com.example.milsaboresapp.domain.model.BlogDetail
import com.example.milsaboresapp.domain.model.BlogDetailHighlight
import com.example.milsaboresapp.domain.model.BlogDetailRelated
import com.example.milsaboresapp.domain.model.BlogDetailSection

object BlogDetailSeedData {

    val details: Map<String, BlogDetail> = listOf(
        BlogDetail(
            id = "blog-recetas-1",
            category = "Recetas",
            title = "Caso curioso 1: El misterio de la torta que desaparece",
            subtitle = "Como logramos una torta que nunca sobra en las celebraciones",
            heroImage = "blog1",
            publishDate = "12 de julio de 2024",
            readingTime = "6 minutos",
            introParagraphs = listOf(
                "Hay preparaciones que se convierten en leyenda dentro del taller. Esta torta de chocolate nacio como un pedido improvisado y termino siendo la receta que desaparece cada vez que la llevamos a un evento.",
                "En esta cronica repasamos los pasos clave, los ajustes que aprendimos sobre la marcha y pequenos trucos que puedes aplicar en casa para lograr la misma textura cremosa."
            ),
            sections = listOf(
                BlogDetailSection(
                    heading = "Capas que mantienen el suspenso",
                    paragraphs = listOf(
                        "Comenzamos con un bizcocho humedo de cacao holandes y leche de avena, ideal para quienes buscan un perfil intenso pero equilibrado.",
                        "El relleno combina ganache montada con pralin de avellanas, lo que aporta una nota crocante sin opacar el chocolate."
                    ),
                    bulletPoints = listOf(
                        "Hornea el bizcocho a 170 grados para evitar que se reseque.",
                        "Reposa el relleno 30 minutos en frio antes de montar las capas.",
                        "Sella los bordes con ganache espesa para que la cobertura no se deslice."
                    )
                ),
                BlogDetailSection(
                    heading = "Decoracion que despierta curiosidad",
                    paragraphs = listOf(
                        "El acabado exterior es una mantequilla suiza con cacao y un toque de sal de mar. Ese contraste salado realza los aromas sin abrumar.",
                        "Usamos laminas de oro comestible en diagonales suaves. No agrega sabor pero aporta ese efecto wow que todos fotografian antes del primer corte."
                    )
                ),
                BlogDetailSection(
                    heading = "Variaciones segun la temporada",
                    paragraphs = listOf(
                        "En primavera reemplazamos el pralin por compota de frambuesas y hojas de menta fresca.",
                        "Para eventos de invierno, incorporamos peras caramelizadas y especias tibias como canela y cardamomo."
                    )
                )
            ),
            highlights = listOf(
                BlogDetailHighlight(
                    title = "Dato del taller",
                    description = "Hidratamos las capas con un jarabe de cacao y espresso. Evita que se resequen y agrega un amargor elegante."
                ),
                BlogDetailHighlight(
                    title = "Favorita de los novios",
                    description = "El 40 por ciento de las tortas de matrimonio que entregamos en 2024 usaron esta base con personalizaciones dulces o adultas."
                )
            ),
            tips = listOf(
                "Usa chocolate con al menos 64 por ciento de cacao para que la ganache tenga cuerpo.",
                "Si necesitas transporte largo, congela la torta 45 minutos antes de salir para fijar la cobertura.",
                "Decora con frutos secos tostados justo antes de servir para conservar el crocante."
            ),
            conclusionTitle = "Listo para probar tu version",
            conclusionBody = "Si quieres replicar esta torta en casa, comienza con un molde de 20 centimetros y controla la humedad con una bandeja de agua en el horno. Nosotros podemos personalizar sabores, rellenos y mensajes para tu evento.",
            closingCta = "Cotizar mi torta personalizada",
            relatedPosts = listOf(
                BlogDetailRelated(
                    id = "blog-tips-1",
                    title = "Caso curioso 2: El betun perfecto para climas calidos",
                    excerpt = "Aprende a estabilizar cremas para que luzcan impecables incluso en terrazas a pleno sol.",
                    imageResName = "blog2"
                )
            )
        ),
        BlogDetail(
            id = "blog-tips-1",
            category = "Tips",
            title = "Caso curioso 2: El betun perfecto para climas calidos",
            subtitle = "Nuestra guia para que la decoracion no se derrita en pleno verano",
            heroImage = "blog2",
            publishDate = "3 de septiembre de 2024",
            readingTime = "5 minutos",
            introParagraphs = listOf(
                "Cuando la celebracion ocurre en exteriores, la batalla contra el calor es real. Este betun tuvo su origen en una boda al mediodia, con 32 grados de temperatura y cero sombra.",
                "Compartimos la formula y el paso a paso que usamos desde entonces. Spoiler: requiere precision con el termometro y paciencia con la batidora."
            ),
            sections = listOf(
                BlogDetailSection(
                    heading = "La base que resiste",
                    paragraphs = listOf(
                        "Partimos con claras pasteurizadas y azucar granulada, batidas a bano maria hasta alcanzar 70 grados. Esa temperatura elimina riesgos sanitarios y genera una espuma estable.",
                        "Agregamos mantequilla en cubos frios y cacao alcalino, batiendo a velocidad media hasta lograr una crema satinada."
                    ),
                    bulletPoints = listOf(
                        "Controla la temperatura con un termometro digital.",
                        "Anade la mantequilla poco a poco para evitar que se corte.",
                        "Si la mezcla pierde cuerpo, refrigera 10 minutos y vuelve a batir."
                    )
                ),
                BlogDetailSection(
                    heading = "Capas defensivas",
                    paragraphs = listOf(
                        "Antes de decorar, aplicamos una capa delgada llamada crumb coat. Se enfria 20 minutos y actua como barrera.",
                        "Sobre esa base, trabajamos con espatula curva y giros suaves para evitar calentar el betun con las manos."
                    )
                ),
                BlogDetailSection(
                    heading = "Plan de contingencia",
                    paragraphs = listOf(
                        "Siempre viajamos con un kit de emergencia: mangas extra, espatula fria y compresas de gel.",
                        "En eventos largos coordinamos con la produccion para tener un punto de refrigeracion cercano al montaje."
                    )
                )
            ),
            highlights = listOf(
                BlogDetailHighlight(
                    title = "Temperatura ideal",
                    description = "El betun se mantiene firme entre 18 y 22 grados. Ajusta la climatizacion del salon si es posible."
                ),
                BlogDetailHighlight(
                    title = "Apta para veganos",
                    description = "Esta tecnica funciona reemplazando la mantequilla por margarina vegetal y las claras por aquafaba reducida."
                )
            ),
            tips = listOf(
                "Transporta la torta en contenedores rigidos con acumuladores de frio reutilizables.",
                "Evita luz directa durante la espera del evento; una simple sombrilla puede marcar diferencia.",
                "Si el betun se ablanda en la mesa, pasa suavemente una espatula fria para recuperar textura."
            ),
            conclusionTitle = "Tu mesa dulce lista para el verano",
            conclusionBody = "Adaptamos decoraciones segun la ubicacion y la epoca del ano. Desde flores prensadas hasta texturas metalizadas, todo puede sobrevivir al sol si se planifica con anticipacion.",
            closingCta = "Hablar con una pastelera",
            relatedPosts = listOf(
                BlogDetailRelated(
                    id = "blog-recetas-1",
                    title = "Caso curioso 1: El misterio de la torta que desaparece",
                    excerpt = "Descubre la receta favorita de nuestros clientes, con capas humedas e irresistibles.",
                    imageResName = "blog1"
                )
            )
        )
    ).associateBy { it.id }
}
