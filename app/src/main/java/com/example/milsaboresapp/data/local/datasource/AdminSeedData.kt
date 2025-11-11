package com.example.milsaboresapp.data.local.datasource

import com.example.milsaboresapp.domain.model.ProfileRegion
import com.example.milsaboresapp.domain.model.admin.AdminDashboard
import com.example.milsaboresapp.domain.model.admin.AdminDestination
import com.example.milsaboresapp.domain.model.admin.AdminLoginContent
import com.example.milsaboresapp.domain.model.admin.AdminProductFormContent
import com.example.milsaboresapp.domain.model.admin.AdminProductItem
import com.example.milsaboresapp.domain.model.admin.AdminUserFormContent
import com.example.milsaboresapp.domain.model.admin.AdminUserItem
import com.example.milsaboresapp.domain.model.admin.AdminWidget

object AdminSeedData {

    val dashboard = AdminDashboard(
        title = "Panel Administrativo",
        subtitle = "Bienvenido al sistema de gestion de Pasteleria Mil Sabores",
        widgets = listOf(
            AdminWidget(
                title = "Productos",
                description = "Gestiona el inventario, agrega nuevos y controla stock.",
                ctaLabel = "Ir a Productos",
                destination = AdminDestination.Productos
            ),
            AdminWidget(
                title = "Usuarios",
                description = "Administra clientes, vendedores y administradores.",
                ctaLabel = "Ir a Usuarios",
                destination = AdminDestination.Usuarios
            )
        )
    )

    val loginContent = AdminLoginContent(
        title = "Panel Administrativo",
        subtitle = "Inicia sesion con tus credenciales de administrador.",
        emailLabel = "Correo",
        emailPlaceholder = "ejemplo@duoc.cl",
        passwordLabel = "Contrasena",
        passwordPlaceholder = "********",
        submitLabel = "Ingresar",
        footer = "© 2025 Pasteleria Mil Sabores - Admin"
    )

    val productFormContent = AdminProductFormContent(
        title = "Nuevo Producto",
        subtitle = "Completa el formulario para registrar un producto.",
        categories = listOf(
            "Tortas Cuadradas",
            "Tortas Circulares",
            "Sin Azucar",
            "Veganas",
            "Sin Gluten",
            "Postres"
        ),
        successMessage = "Producto registrado correctamente."
    )

    val userFormContent = AdminUserFormContent(
        title = "Nuevo Usuario",
        subtitle = "Completa el formulario para registrar un usuario.",
        roles = listOf("Administrador", "Vendedor", "Cliente"),
        regions = listOf(
            ProfileRegion(
                name = "Valparaiso",
                communes = listOf("Valparaiso", "Vina del Mar", "Quilpue", "Concon")
            ),
            ProfileRegion(
                name = "Region Metropolitana",
                communes = listOf("Santiago", "Providencia", "Las Condes", "Nunoa")
            ),
            ProfileRegion(
                name = "Biobio",
                communes = listOf("Concepcion", "Talcahuano", "San Pedro de la Paz")
            )
        ),
        successMessage = "Usuario registrado correctamente."
    )

    val products = listOf(
        AdminProductItem(code = "TE001", name = "Torta Especial de Cumpleanos", price = 24990, stock = 12, category = "Tortas Circulares"),
        AdminProductItem(code = "TC101", name = "Torta Cuadrada de Chocolate", price = 21990, stock = 8, category = "Tortas Cuadradas"),
        AdminProductItem(code = "VG205", name = "Torta Vegana de Chocolate", price = 23990, stock = 5, category = "Veganas"),
        AdminProductItem(code = "GF301", name = "Cheesecake sin Gluten", price = 18990, stock = 9, category = "Sin Gluten"),
        AdminProductItem(code = "PS010", name = "Mousse de Chocolate", price = 12990, stock = 15, category = "Postres")
    )

    val users = listOf(
        AdminUserItem(
            run = "19011022K",
            firstName = "Camila",
            lastName = "Soto Perez",
            email = "camila@milsabores.cl",
            role = "Administrador",
            region = "Valparaiso",
            commune = "Valparaiso"
        ),
        AdminUserItem(
            run = "20099855-2",
            firstName = "Javier",
            lastName = "Diaz Fuentes",
            email = "javier@milsabores.cl",
            role = "Vendedor",
            region = "Region Metropolitana",
            commune = "Santiago"
        ),
        AdminUserItem(
            run = "18455211-3",
            firstName = "Alejandra",
            lastName = "Guzman Vera",
            email = "alejandra@milsabores.cl",
            role = "Vendedor",
            region = "Region Metropolitana",
            commune = "Providencia"
        ),
        AdminUserItem(
            run = "21566789-1",
            firstName = "Monica",
            lastName = "Alarcon Riquelme",
            email = "monica@milsabores.cl",
            role = "Cliente",
            region = "Biobio",
            commune = "Concepcion"
        ),
        AdminUserItem(
            run = "17332999-K",
            firstName = "Luis",
            lastName = "Perez Ortiz",
            email = "luis@milsabores.cl",
            role = "Cliente",
            region = "Valparaiso",
            commune = "Quilpue"
        )
    )
}
