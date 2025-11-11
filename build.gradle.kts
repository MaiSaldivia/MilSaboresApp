// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    // Estos alias vienen de tu catálogo de versiones (libs.versions.toml)
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false

    // AÑADE ESTA LÍNEA MANUALMENTE (El instalador KSP para Room)
    // IMPORTANTE: La versión debe coincidir con la que usas en tu proyecto.
    // Si te da error de versión, prueba con "1.9.23-1.0.19" o la más reciente compatible.
    //id("com.google.devtools.ksp") version "1.9.23-1.0.19" apply false
}