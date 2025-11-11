package com.example.milsaboresapp.data.local.datasource

import com.example.milsaboresapp.data.local.entity.ProductoEntity

/**
 * Datos base que replican la informacion de js/data/data.js y js/data/producto.js.
 * Se utilizan como punto de partida para el almacenamiento en memoria.
 */
object ProductSeedData {

    val categorias = listOf(
        "Tortas Cuadradas",
        "Tortas Circulares",
        "Postres Individuales",
        "Productos Sin Az\u00facar",
        "Pasteler\u00eda Tradicional",
        "Productos Sin Gluten",
        "Productos Vegana",
        "Tortas Especiales"
    )

    val productos = listOf(
        ProductoEntity(
            id = "TC001",
            nombre = "Torta Cuadrada de Chocolate",
            precio = 45000,
            categoria = "Tortas Cuadradas",
            attr = "20 porciones",
            img = "torta_cuadrada_de_chocolate",
            stock = 6,
            stockCritico = 2,
            descripcion = "Deliciosa torta de chocolate con capas de ganache y un toque de avellanas. Personalizable con mensajes especiales."
        ),
        ProductoEntity(
            id = "TC002",
            nombre = "Torta Cuadrada de Frutas",
            precio = 50000,
            categoria = "Tortas Cuadradas",
            attr = "Frutas frescas",
            img = "torta_cuadrada_de_frutas",
            stock = 6,
            stockCritico = 2,
            descripcion = "Mezcla de frutas frescas y crema chantilly sobre un suave bizcocho de vainilla. Ideal para celebraciones."
        ),
        ProductoEntity(
            id = "TT001",
            nombre = "Torta Circular de Vainilla",
            precio = 40000,
            categoria = "Tortas Circulares",
            attr = "12 porciones",
            img = "torta_circular_de_vainilla",
            stock = 6,
            stockCritico = 2,
            descripcion = "Bizcocho de vainilla cl\u00e1sico relleno con crema pastelera y glaseado dulce, perfecto para cualquier ocasi\u00f3n."
        ),
        ProductoEntity(
            id = "TT002",
            nombre = "Torta Circular de Manjar",
            precio = 42000,
            categoria = "Tortas Circulares",
            attr = "Con nueces",
            img = "torta_circular_de_manjar",
            stock = 6,
            stockCritico = 2,
            descripcion = "Torta tradicional con manjar y nueces, un deleite para los amantes de los sabores cl\u00e1sicos."
        ),
        ProductoEntity(
            id = "PI001",
            nombre = "Mousse de Chocolate",
            precio = 5000,
            categoria = "Postres Individuales",
            attr = "Individual",
            img = "mousse_de_chocolate",
            stock = 6,
            stockCritico = 2,
            descripcion = "Postre individual cremoso y suave, hecho con chocolate de alta calidad."
        ),
        ProductoEntity(
            id = "PI002",
            nombre = "Tiramis\u00fa Cl\u00e1sico",
            precio = 5500,
            categoria = "Postres Individuales",
            attr = "Individual",
            img = "tiramisu_clasico",
            stock = 6,
            stockCritico = 2,
            descripcion = "Postre italiano individual con capas de caf\u00e9, mascarpone y cacao. Perfecto para finalizar cualquier comida."
        ),
        ProductoEntity(
            id = "PSA001",
            nombre = "Torta Sin Az\u00facar de Naranja",
            precio = 48000,
            categoria = "Productos Sin Az\u00facar",
            attr = "Endulzada naturalmente",
            img = "torta_sin_azucar_de_naranja",
            stock = 6,
            stockCritico = 2,
            descripcion = "Ligera y deliciosa, endulzada naturalmente. Ideal para opciones m\u00e1s saludables."
        ),
        ProductoEntity(
            id = "PSA002",
            nombre = "Cheesecake Sin Az\u00facar",
            precio = 47000,
            categoria = "Productos Sin Az\u00facar",
            attr = "Sin az\u00facar",
            img = "cheesecake",
            stock = 6,
            stockCritico = 2,
            descripcion = "Suave y cremoso, opci\u00f3n perfecta para disfrutar sin culpa."
        ),
        ProductoEntity(
            id = "PG001",
            nombre = "Brownie Sin Gluten",
            precio = 4000,
            categoria = "Productos Sin Gluten",
            attr = "Cacao 70%",
            img = "brownie",
            stock = 6,
            stockCritico = 2,
            descripcion = "Rico y denso; ideal para quienes evitan el gluten sin sacrificar el sabor."
        ),
        ProductoEntity(
            id = "PG002",
            nombre = "Pan Sin Gluten",
            precio = 3500,
            categoria = "Productos Sin Gluten",
            attr = "Pan de molde",
            img = "pan_integral",
            stock = 6,
            stockCritico = 2,
            descripcion = "Suave y esponjoso, perfecto para s\u00e1ndwiches o acompa\u00f1ar cualquier comida."
        ),
        ProductoEntity(
            id = "PV001",
            nombre = "Torta Vegana de Chocolate",
            precio = 50000,
            categoria = "Productos Vegana",
            attr = "Vegano",
            img = "torta_vegana_de_chocolate",
            stock = 6,
            stockCritico = 2,
            descripcion = "Torta h\u00fameda y deliciosa, sin productos de origen animal."
        ),
        ProductoEntity(
            id = "PV002",
            nombre = "Galletas Veganas de Avena",
            precio = 4500,
            categoria = "Productos Vegana",
            attr = "Pack x 10",
            img = "galletas_veganas_de_avena",
            stock = 6,
            stockCritico = 2,
            descripcion = "Crujientes y sabrosas; excelente opci\u00f3n de snack."
        ),
        ProductoEntity(
            id = "TE001",
            nombre = "Torta Especial de Cumplea\u00f1os",
            precio = 55000,
            categoria = "Tortas Especiales",
            attr = "Personalizable",
            img = "torta_especial_de_cumpleanios",
            stock = 6,
            stockCritico = 2,
            descripcion = "Personalizable con decoraciones y mensajes \u00fanicos."
        ),
        ProductoEntity(
            id = "TE002",
            nombre = "Torta Especial de Boda",
            precio = 60000,
            categoria = "Tortas Especiales",
            attr = "Dise\u00f1o elegante",
            img = "torta_especial_de_boda",
            stock = 6,
            stockCritico = 2,
            descripcion = "Elegante y deliciosa; pensada para ser el centro de tu boda."
        )
    )
}
