package com.example.milsaboresapp.data.local.datasource

import com.example.milsaboresapp.domain.model.vendedor.VendedorDashboard
import com.example.milsaboresapp.domain.model.vendedor.VendedorDestination
import com.example.milsaboresapp.domain.model.vendedor.VendedorInventarioItem
import com.example.milsaboresapp.domain.model.vendedor.VendedorPedidoDetalle
import com.example.milsaboresapp.domain.model.vendedor.VendedorPedidoItem
import com.example.milsaboresapp.domain.model.vendedor.VendedorWidget

object VendedorSeedData {

    val dashboard = VendedorDashboard(
        title = "Panel de Vendedor",
        subtitle = "Bienvenido, aqui tienes un resumen de tus tareas.",
        widgets = listOf(
            VendedorWidget(
                title = "Pedidos Pendientes",
                description = "Revisa y gestiona los pedidos por preparar y enviar.",
                ctaLabel = "Ir a Pedidos",
                destination = VendedorDestination.Pedidos
            ),
            VendedorWidget(
                title = "Inventario Critico",
                description = "Identifica los productos con bajo stock para reponer.",
                ctaLabel = "Revisar Inventario",
                destination = VendedorDestination.Inventario
            )
        )
    )

    val inventario = listOf(
        VendedorInventarioItem(codigo = "TC101", nombre = "Torta Cuadrada de Chocolate", stock = 8, estado = "Normal"),
        VendedorInventarioItem(codigo = "VG205", nombre = "Torta Vegana de Chocolate", stock = 3, estado = "Bajo"),
        VendedorInventarioItem(codigo = "GF301", nombre = "Cheesecake sin Gluten", stock = 2, estado = "Critico"),
        VendedorInventarioItem(codigo = "PS010", nombre = "Mousse de Chocolate", stock = 10, estado = "Normal"),
        VendedorInventarioItem(codigo = "TE001", nombre = "Torta Especial de Cumpleanos", stock = 5, estado = "Bajo")
    )

    val pedidos = listOf(
        VendedorPedidoItem(
            id = "PED-1001",
            cliente = "Maria Gonzalez",
            total = 45980,
            estado = "Pendiente",
            detalle = listOf(
                VendedorPedidoDetalle(producto = "Torta Cuadrada de Chocolate", cantidad = 1, precio = 21990),
                VendedorPedidoDetalle(producto = "Cheesecake sin Gluten", cantidad = 1, precio = 23990)
            )
        ),
        VendedorPedidoItem(
            id = "PED-1002",
            cliente = "Carlos Ramirez",
            total = 24990,
            estado = "Preparacion",
            detalle = listOf(
                VendedorPedidoDetalle(producto = "Torta Especial de Cumpleanos", cantidad = 1, precio = 24990)
            )
        ),
        VendedorPedidoItem(
            id = "PED-1003",
            cliente = "Fernanda Silva",
            total = 36980,
            estado = "Pendiente",
            detalle = listOf(
                VendedorPedidoDetalle(producto = "Cheesecake sin Gluten", cantidad = 1, precio = 18990),
                VendedorPedidoDetalle(producto = "Mousse de Chocolate", cantidad = 1, precio = 17990)
            )
        )
    )
}
