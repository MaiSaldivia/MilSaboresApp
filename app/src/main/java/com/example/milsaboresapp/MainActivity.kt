package com.example.milsaboresapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.milsaboresapp.di.AppGraph
import com.example.milsaboresapp.presentation.about.AboutViewModel
import com.example.milsaboresapp.presentation.auth.LoginViewModel
import com.example.milsaboresapp.presentation.auth.RegisterViewModel
import com.example.milsaboresapp.presentation.blog.BlogViewModel
import com.example.milsaboresapp.presentation.blogdetail.BlogDetailViewModel
import com.example.milsaboresapp.presentation.cart.CartViewModel
import com.example.milsaboresapp.presentation.contact.ContactViewModel
import com.example.milsaboresapp.presentation.home.HomeViewModel
import com.example.milsaboresapp.presentation.productdetail.ProductDetailViewModel
import com.example.milsaboresapp.presentation.products.ProductsViewModel
import com.example.milsaboresapp.presentation.profile.ProfileViewModel
import com.example.milsaboresapp.ui.screen.BlogDetalle1Screen
import com.example.milsaboresapp.ui.screen.BlogDetalle2Screen
import com.example.milsaboresapp.ui.screen.BlogScreen
import com.example.milsaboresapp.ui.screen.CarritoScreen
import com.example.milsaboresapp.ui.screen.ContactoScreen
import com.example.milsaboresapp.ui.screen.IndexScreen
import com.example.milsaboresapp.ui.screen.LoginScreen
import com.example.milsaboresapp.ui.screen.NosotrosScreen
import com.example.milsaboresapp.ui.screen.PerfilScreen
import com.example.milsaboresapp.ui.screen.ProductDetailScreen
import com.example.milsaboresapp.ui.screen.ProductsScreen
import com.example.milsaboresapp.ui.screen.RegistroScreen
import com.example.milsaboresapp.ui.theme.MilSaboresAppTheme

class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		// Inicializamos el grafo de dependencias (Room, repos, etc.)
		AppGraph.init(applicationContext)

		setContent {
			MilSaboresAppTheme {
				MainApp()
			}
		}
	}
}

@Composable
private fun MainApp() {
	val homeViewModel: HomeViewModel = viewModel(
		factory = HomeViewModel.provideFactory(
			productoRepository = AppGraph.productoRepository,
			blogRepository = AppGraph.blogRepository
		)
	)
	val homeUiState by homeViewModel.uiState.collectAsState()

	val productsViewModel: ProductsViewModel = viewModel(
		factory = ProductsViewModel.provideFactory(AppGraph.productoRepository)
	)
	val productsUiState by productsViewModel.uiState.collectAsState()

	val aboutViewModel: AboutViewModel = viewModel(
		factory = AboutViewModel.provideFactory(AppGraph.aboutRepository)
	)
	val aboutUiState by aboutViewModel.uiState.collectAsState()

	val blogViewModel: BlogViewModel = viewModel(
		factory = BlogViewModel.provideFactory(AppGraph.blogRepository)
	)
	val blogUiState by blogViewModel.uiState.collectAsState()

	val contactViewModel: ContactViewModel = viewModel(
		factory = ContactViewModel.provideFactory(AppGraph.contactRepository)
	)
	val contactUiState by contactViewModel.uiState.collectAsState()

	val cartViewModel: CartViewModel = viewModel(
		factory = CartViewModel.provideFactory(AppGraph.cartRepository)
	)
	val cartUiState by cartViewModel.uiState.collectAsState()

	val loginViewModel: LoginViewModel = viewModel(
		factory = LoginViewModel.provideFactory(AppGraph.authRepository)
	)
	val loginUiState by loginViewModel.uiState.collectAsState()

	val registerViewModel: RegisterViewModel = viewModel(
		factory = RegisterViewModel.provideFactory(AppGraph.authRepository)
	)
	val registerUiState by registerViewModel.uiState.collectAsState()

	// ProfileViewModel con Room
	val profileViewModel: ProfileViewModel = viewModel(
		factory = ProfileViewModel.provideFactory(
			profileRepository = AppGraph.profileRepository,
			userDao = AppGraph.userDao
		)
	)
	val profileUiState by profileViewModel.uiState.collectAsState()

	// Usuario actual (sesión)
	val currentUser by AppGraph.sessionManager.currentUser.collectAsState()

	var destination by remember { mutableStateOf<AppDestination>(AppDestination.Home) }
	val currentTab = destination.topTab

	LaunchedEffect(destination) {
		if (destination is AppDestination.Perfil) {
			// cada vez que navegues a Perfil recarga desde Room
			profileViewModel.refreshFromDatabase()
		}
	}

	// Navegaciones básicas
	val navigateToLoginScreen = {
		destination = AppDestination.Login
	}
	val navigateToRegister = {
		destination = AppDestination.Registro
	}
	val navigateToCart = {
		destination = AppDestination.Carrito
	}
	val navigateToProducts = {
		destination = AppDestination.Productos
	}
	val navigateToContact = {
		destination = AppDestination.Contacto
	}

	// Nueva navegación de autenticación:
	// si no hay usuario -> Login, si hay usuario -> Perfil
	val navigateAuth = {
		destination = if (currentUser == null) {
			AppDestination.Login
		} else {
			AppDestination.Perfil
		}
	}

	val onTabSelected: (String) -> Unit = { tab ->
		destination = when (tab) {
			"Inicio" -> {
				loginViewModel.resetSuccess()
				registerViewModel.resetSuccess()
				profileViewModel.resetProfileSuccess()
				profileViewModel.resetPasswordSuccess()
				AppDestination.Home
			}

			"Productos" -> AppDestination.Productos
			"Nosotros" -> AppDestination.Nosotros
			"Blog" -> AppDestination.Blog
			"Contacto" -> AppDestination.Contacto
			else -> AppDestination.Home
		}
	}

	Surface(
		modifier = Modifier.fillMaxSize(),
		color = MaterialTheme.colorScheme.background
	) {
		when (val current = destination) {
			AppDestination.Home -> {
				IndexScreen(
					state = homeUiState,
					currentTab = currentTab,
					onTabClick = onTabSelected,
					onLoginClick = navigateAuth,
					onCartClick = navigateToCart,
					onHeroCtaClick = navigateToProducts,
					onProductClick = { id -> destination = AppDestination.ProductoDetalle(id) },
					onBlogClick = { id -> destination = AppDestination.BlogDetalle(id) }
				)
			}

			AppDestination.Productos -> {
				ProductsScreen(
					state = productsUiState,
					currentTab = currentTab,
					onTabClick = onTabSelected,
					onLoginClick = navigateAuth,
					onCartClick = navigateToCart,
					onQueryChange = productsViewModel::onQueryChange,
					onCategorySelected = productsViewModel::onCategorySelected,
					onSortSelected = productsViewModel::onSortSelected,
					onProductClick = { id -> destination = AppDestination.ProductoDetalle(id) },
					onAddToCartClick = { productId ->
						val product = productsUiState.products.firstOrNull { it.id == productId }
						if (product != null) {
							cartViewModel.addProduct(product)
						}
					}
				)
			}

			is AppDestination.ProductoDetalle -> {
				val detailViewModel: ProductDetailViewModel = viewModel(
					key = "product-${current.productId}",
					factory = ProductDetailViewModel.provideFactory(
						productId = current.productId,
						productoRepository = AppGraph.productoRepository
					)
				)
				val detailState by detailViewModel.uiState.collectAsState()

				ProductDetailScreen(
					state = detailState,
					currentTab = currentTab,
					onTabClick = onTabSelected,
					onLoginClick = navigateAuth,
					onCartClick = navigateToCart,
					onNavigateHome = { destination = AppDestination.Home },
					onNavigateToProducts = navigateToProducts,
					onNavigateToCategory = { category ->
						productsViewModel.onCategorySelected(category)
						destination = AppDestination.Productos
					},
					onQuantityChange = detailViewModel::onQuantityChange,
					onMessageChange = detailViewModel::onCustomMessageChange,
					onAddToCart = {
						val product = detailState.product
						if (product != null) {
							val message = detailState.customMessage.takeIf { it.isNotBlank() }
							cartViewModel.addProduct(
								product = product,
								quantity = detailState.quantity,
								message = message
							)
						}
					},
					onRelatedClick = { id -> destination = AppDestination.ProductoDetalle(id) }
				)
			}

			AppDestination.Nosotros -> {
				NosotrosScreen(
					state = aboutUiState,
					currentTab = currentTab,
					onTabClick = onTabSelected,
					onLoginClick = navigateAuth,
					onCartClick = navigateToCart,
					onContactClick = navigateToContact
				)
			}

			AppDestination.Blog -> {
				BlogScreen(
					state = blogUiState,
					currentTab = currentTab,
					onTabClick = onTabSelected,
					onLoginClick = navigateAuth,
					onCartClick = navigateToCart,
					onQueryChange = blogViewModel::onQueryChange,
					onCategorySelected = blogViewModel::onCategorySelected,
					onPostClick = { id -> destination = AppDestination.BlogDetalle(id) }
				)
			}

			is AppDestination.BlogDetalle -> {
				val detailViewModel: BlogDetailViewModel = viewModel(
					key = "blog-${current.postId}",
					factory = BlogDetailViewModel.provideFactory(
						postId = current.postId,
						blogDetailRepository = AppGraph.blogDetailRepository
					)
				)
				val detailState by detailViewModel.uiState.collectAsState()

				val backToBlog = {
					blogViewModel.clearFilters()
					destination = AppDestination.Blog
				}
				val openRelated: (String) -> Unit = { postId ->
					if (postId != current.postId) {
						destination = AppDestination.BlogDetalle(postId)
					}
				}

				when (current.postId) {
					"blog-recetas-1", "blog-recetas-2" -> {
						BlogDetalle1Screen(
							state = detailState,
							currentTab = currentTab,
							onTabClick = onTabSelected,
							onLoginClick = navigateAuth,
							onCartClick = navigateToCart,
							onBackToBlog = backToBlog,
							onRelatedClick = openRelated,
							onContactClick = navigateToContact
						)
					}

					"blog-tips-1" -> {
						BlogDetalle2Screen(
							state = detailState,
							currentTab = currentTab,
							onTabClick = onTabSelected,
							onLoginClick = navigateAuth,
							onCartClick = navigateToCart,
							onBackToBlog = backToBlog,
							onRelatedClick = openRelated,
							onContactClick = navigateToContact
						)
					}

					else -> {
						BlogDetalle1Screen(
							state = detailState,
							currentTab = currentTab,
							onTabClick = onTabSelected,
							onLoginClick = navigateAuth,
							onCartClick = navigateToCart,
							onBackToBlog = backToBlog,
							onRelatedClick = openRelated,
							onContactClick = navigateToContact
						)
					}
				}
			}

			AppDestination.Contacto -> {
				ContactoScreen(
					state = contactUiState,
					currentTab = currentTab,
					onTabClick = onTabSelected,
					onLoginClick = navigateAuth,
					onCartClick = navigateToCart,
					onNameChange = contactViewModel::onNameChange,
					onEmailChange = contactViewModel::onEmailChange,
					onPhoneChange = contactViewModel::onPhoneChange,
					onEventTypeChange = contactViewModel::onEventTypeChange,
					onMessageChange = contactViewModel::onMessageChange,
					onSubmit = contactViewModel::onSubmit,
					onDismissSuccess = contactViewModel::resetSuccess
				)
			}

			AppDestination.Login -> {
				LoginScreen(
					state = loginUiState,
					currentTab = currentTab,
					onTabClick = onTabSelected,
					onLoginClick = navigateAuth,
					onCartClick = navigateToCart,
					onEmailChange = loginViewModel::onEmailChange,
					onPasswordChange = loginViewModel::onPasswordChange,
					onRememberChange = loginViewModel::onRememberChange,
					onSubmit = loginViewModel::submit,
					onNavigateToRegister = navigateToRegister,
					onDismissSuccess = {
						loginViewModel.resetSuccess()
						destination = AppDestination.Perfil
					}
				)
			}

			AppDestination.Registro -> {
				RegistroScreen(
					state = registerUiState,
					currentTab = currentTab,
					onTabClick = onTabSelected,
					onLoginClick = navigateAuth,
					onCartClick = navigateToCart,
					onRunChange = registerViewModel::onRunChange,
					onFirstNameChange = registerViewModel::onFirstNameChange,
					onLastNameChange = registerViewModel::onLastNameChange,
					onEmailChange = registerViewModel::onEmailChange,
					onPhoneChange = registerViewModel::onPhoneChange,
					onBirthDateChange = registerViewModel::onBirthDateChange,
					onRegionChange = registerViewModel::onRegionChange,
					onCommuneChange = registerViewModel::onCommuneChange,
					onAddressChange = registerViewModel::onAddressChange,
					onPromoCodeChange = registerViewModel::onPromoCodeChange,
					onPasswordChange = registerViewModel::onPasswordChange,
					onConfirmPasswordChange = registerViewModel::onConfirmPasswordChange,
					onAcceptPromotionsChange = registerViewModel::onAcceptsPromotionsChange,
					onSubmit = registerViewModel::submit,
					// link "Ya tienes cuenta / Inicia sesión"
					onNavigateToLogin = navigateToLoginScreen,
					onDismissSuccess = {
						registerViewModel.resetSuccess()
						// tras registro correcto ya hay usuario en SessionManager
						destination = AppDestination.Perfil
					}
				)
			}

			AppDestination.Carrito -> {
				CarritoScreen(
					state = cartUiState,
					currentTab = currentTab,
					onTabClick = onTabSelected,
					onLoginClick = navigateAuth,
					onCartClick = navigateToCart,
					onIncreaseQuantity = cartViewModel::increment,
					onDecreaseQuantity = cartViewModel::decrement,
					onRemoveItem = cartViewModel::remove,
					onCheckout = cartViewModel::checkout,
					onShippingOptionSelected = cartViewModel::selectShippingOption,
					onContinueShopping = navigateToProducts,
					onDismissSuccess = cartViewModel::resetCheckoutSuccess
				)
			}

			AppDestination.Perfil -> {
				PerfilScreen(
					state = profileUiState,
					currentTab = currentTab,
					onTabClick = onTabSelected,
					onLoginClick = navigateAuth,
					onCartClick = navigateToCart,
					onFirstNameChange = profileViewModel::onFirstNameChange,
					onLastNameChange = profileViewModel::onLastNameChange,
					onPhoneChange = profileViewModel::onPhoneChange,
					onBirthDateChange = profileViewModel::onBirthDateChange,
					onAddressChange = profileViewModel::onAddressChange,
					onRegionChange = profileViewModel::onRegionChange,
					onCommuneChange = profileViewModel::onCommuneChange,
					onShippingOptionChange = profileViewModel::onShippingOptionChange,
					onDefaultCouponChange = profileViewModel::onDefaultCouponChange,
					onNewsletterChange = profileViewModel::onNewsletterChange,
					onSaveAddressChange = profileViewModel::onSaveAddressChange,
					onSubmitProfile = profileViewModel::saveProfile,
					onCurrentPasswordChange = profileViewModel::onCurrentPasswordChange,
					onNewPasswordChange = profileViewModel::onNewPasswordChange,
					onConfirmPasswordChange = profileViewModel::onConfirmPasswordChange,
					onSubmitPassword = profileViewModel::updatePassword,
					onDismissProfileSuccess = profileViewModel::resetProfileSuccess,
					onDismissPasswordSuccess = profileViewModel::resetPasswordSuccess
				)
			}
		}
	}
}

private sealed class AppDestination(val topTab: String) {
	object Home : AppDestination("Inicio")
	object Productos : AppDestination("Productos")
	data class ProductoDetalle(val productId: String) : AppDestination("Productos")
	object Nosotros : AppDestination("Nosotros")
	object Blog : AppDestination("Blog")
	data class BlogDetalle(val postId: String) : AppDestination("Blog")
	object Contacto : AppDestination("Contacto")
	object Login : AppDestination("Inicio")
	object Registro : AppDestination("Inicio")
	object Carrito : AppDestination("Inicio")
	object Perfil : AppDestination("Inicio")
}
