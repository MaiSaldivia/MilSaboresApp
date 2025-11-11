package com.example.milsaboresapp.di

import android.content.Context
import com.example.milsaboresapp.data.local.dao.AppDatabase
import com.example.milsaboresapp.data.local.dao.UserDao
import com.example.milsaboresapp.data.local.datasource.InMemoryProductDataSource
import com.example.milsaboresapp.data.repository.AboutRepositoryImpl
import com.example.milsaboresapp.data.repository.AdminAuthRepositoryImpl
import com.example.milsaboresapp.data.repository.AdminDashboardRepositoryImpl
import com.example.milsaboresapp.data.repository.AdminProductRepositoryImpl
import com.example.milsaboresapp.data.repository.AdminUserRepositoryImpl
import com.example.milsaboresapp.data.repository.AuthRepositoryImpl
import com.example.milsaboresapp.data.repository.BlogDetailRepositoryImpl
import com.example.milsaboresapp.data.repository.BlogRepositoryImpl
import com.example.milsaboresapp.data.repository.CartRepositoryImpl
import com.example.milsaboresapp.data.repository.ContactRepositoryImpl
import com.example.milsaboresapp.data.repository.ProductoRepositoryImpl
import com.example.milsaboresapp.data.repository.ProfileRepositoryImpl
import com.example.milsaboresapp.data.repository.VendedorDashboardRepositoryImpl
import com.example.milsaboresapp.data.repository.VendedorInventarioRepositoryImpl
import com.example.milsaboresapp.data.repository.VendedorPedidoRepositoryImpl
import com.example.milsaboresapp.data.session.SessionManager
import com.example.milsaboresapp.domain.repository.AboutRepository
import com.example.milsaboresapp.domain.repository.AdminAuthRepository
import com.example.milsaboresapp.domain.repository.AdminDashboardRepository
import com.example.milsaboresapp.domain.repository.AdminProductRepository
import com.example.milsaboresapp.domain.repository.AdminUserRepository
import com.example.milsaboresapp.domain.repository.AuthRepository
import com.example.milsaboresapp.domain.repository.BlogDetailRepository
import com.example.milsaboresapp.domain.repository.BlogRepository
import com.example.milsaboresapp.domain.repository.CartRepository
import com.example.milsaboresapp.domain.repository.ContactRepository
import com.example.milsaboresapp.domain.repository.ProductoRepository
import com.example.milsaboresapp.domain.repository.ProfileRepository
import com.example.milsaboresapp.domain.repository.vendedor.VendedorDashboardRepository
import com.example.milsaboresapp.domain.repository.vendedor.VendedorInventarioRepository
import com.example.milsaboresapp.domain.repository.vendedor.VendedorPedidoRepository

object AppGraph {

    // ------------------------------------------------------------------------
    // Inicialización con Context (NECESARIO para Room)
    // ------------------------------------------------------------------------
    private lateinit var appContext: Context

    fun init(context: Context) {
        appContext = context.applicationContext
    }

    // ------------------------------------------------------------------------
    // Base de datos Room y DAOs
    // ------------------------------------------------------------------------

    private val database: AppDatabase by lazy {
        AppDatabase.getInstance(appContext)
    }

    val userDao: UserDao by lazy {
        database.userDao()
    }

    // ------------------------------------------------------------------------
    // SessionManager (usuario logueado en memoria)
    // ------------------------------------------------------------------------

    val sessionManager: SessionManager by lazy {
        SessionManager()
    }

    // ------------------------------------------------------------------------
    // Data sources en memoria (productos)
    // ------------------------------------------------------------------------

    private val productDataSource: InMemoryProductDataSource by lazy {
        InMemoryProductDataSource()
    }

    // ------------------------------------------------------------------------
    // Repositorios de dominio
    // ------------------------------------------------------------------------

    val productoRepository: ProductoRepository by lazy {
        ProductoRepositoryImpl(productDataSource)
    }

    val blogRepository: BlogRepository by lazy {
        BlogRepositoryImpl()
    }

    val blogDetailRepository: BlogDetailRepository by lazy {
        BlogDetailRepositoryImpl()
    }

    val aboutRepository: AboutRepository by lazy {
        AboutRepositoryImpl()
    }

    val contactRepository: ContactRepository by lazy {
        ContactRepositoryImpl()
    }

    // CartRepositoryImpl ahora necesita el userFlow
    val authRepository: AuthRepository by lazy {
        AuthRepositoryImpl(userDao, sessionManager)
    }

    val cartRepository: CartRepository by lazy {
        CartRepositoryImpl(
            userFlow = authRepository.observeCurrentUser()
        )
    }

    // Repo que entrega el contenido base del perfil
    val profileRepository: ProfileRepository by lazy {
        ProfileRepositoryImpl()
    }

    val vendedorDashboardRepository: VendedorDashboardRepository by lazy {
        VendedorDashboardRepositoryImpl()
    }

    val vendedorInventarioRepository: VendedorInventarioRepository by lazy {
        VendedorInventarioRepositoryImpl()
    }

    val vendedorPedidoRepository: VendedorPedidoRepository by lazy {
        VendedorPedidoRepositoryImpl()
    }

    val adminDashboardRepository: AdminDashboardRepository by lazy {
        AdminDashboardRepositoryImpl()
    }

    val adminAuthRepository: AdminAuthRepository by lazy {
        AdminAuthRepositoryImpl()
    }

    val adminProductRepository: AdminProductRepository by lazy {
        AdminProductRepositoryImpl()
    }

    val adminUserRepository: AdminUserRepository by lazy {
        AdminUserRepositoryImpl()
    }
}
