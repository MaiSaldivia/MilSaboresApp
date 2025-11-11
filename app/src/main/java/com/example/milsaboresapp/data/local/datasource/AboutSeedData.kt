package com.example.milsaboresapp.data.local.datasource

import com.example.milsaboresapp.domain.model.AboutInfo
import com.example.milsaboresapp.domain.model.Achievement
import com.example.milsaboresapp.domain.model.TeamMember
import com.example.milsaboresapp.domain.model.TimelineEvent
import com.example.milsaboresapp.domain.model.ValueItem

object AboutSeedData {
    val info = AboutInfo(
        headline = "Celebramos con sabor desde 2010",
        subtitle = "Somos una pasteleria familiar de Valparaiso apasionada por crear recuerdos dulces con ingredientes honestos.",
        mission = "Acompanar cada momento importante con reposteria artesanal que combine tradicion chilena y tecnicas contemporaneas.",
        vision = "Ser la pasteleria de referencia en la region, reconocida por sabores innovadores y un servicio cercano.",
        timeline = listOf(
            TimelineEvent(
                year = "2010",
                title = "Nace el taller",
                description = "Abrimos nuestra primera cocina en la casa de la abuela, vendiendo tortas de chocolate a los vecinos."
            ),
            TimelineEvent(
                year = "2014",
                title = "Primer local",
                description = "Inauguramos nuestra tienda en el centro de Valparaiso y ampliamos la carta con opciones veganas."
            ),
            TimelineEvent(
                year = "2019",
                title = "Expansion",
                description = "Comenzamos a despachar a toda la region y lanzamos cursos de decoracion para emprendedores."
            ),
            TimelineEvent(
                year = "2023",
                title = "Mil Sabores App",
                description = "Digitalizamos la experiencia para que puedas comprar y personalizar tus tortas desde el celular."
            )
        ),
        values = listOf(
            ValueItem(
                title = "Ingredientes honestos",
                description = "Trabajamos con productores locales y seleccionamos materias primas frescas."
            ),
            ValueItem(
                title = "Disenos a medida",
                description = "Cada pedido es unico, nos adaptamos a tus ideas y celebraciones."
            ),
            ValueItem(
                title = "Sostenibilidad",
                description = "Reducimos desperdicios y usamos empaques compostables siempre que es posible."
            )
        ),
        team = listOf(
            TeamMember(
                name = "Carolina Saldivia",
                role = "Maestra pastelera",
                bio = "Fundadora y creadora de las recetas insignia, fanatica del chocolate amargo.",
                imageResName = null
            ),
            TeamMember(
                name = "Luis Yanez",
                role = "Director creativo",
                bio = "Disena las experiencias personalizadas y lidera los lanzamientos de temporada.",
                imageResName = null
            ),
            TeamMember(
                name = "Antonia Rivas",
                role = "Coordinadora de eventos",
                bio = "Asegura que cada pedido llegue impecable y a tiempo, desde matrimonios hasta desayunos.",
                imageResName = null
            )
        ),
        achievements = listOf(
            Achievement(
                title = "+1200 celebraciones felices",
                description = "Desde baby showers a graduaciones, acompanando a familias de la region."
            ),
            Achievement(
                title = "Receta premiada 2022",
                description = "Nuestra torta vegana de chocolate recibio menciones en ferias gastronomicas."
            ),
            Achievement(
                title = "Academia Mil Sabores",
                description = "Mas de 300 alumnos han aprendido tecnicas de pasteleria en nuestros talleres."
            )
        ),
        callToActionMessage = "Quieres planificar tu proxima celebracion con nosotros? Nuestro equipo te asesora desde la idea hasta la entrega.",
        callToActionButton = "Escribenos"
    )
}
