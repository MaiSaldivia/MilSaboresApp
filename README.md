# MilSaboresApp

Aplicación móvil Android para la pastelería **Mil Sabores**, que permite a los clientes explorar el catálogo de productos, personalizar tortas, gestionar su carrito de compras y acceder a beneficios personalizados según su perfil.

## Funcionalidades implementadas

### 1. Navegación principal

- Pantalla de **Inicio** con banner principal, botón “Ver productos” y sección de productos destacados.
- Barra de navegación superior con secciones: **Inicio, Productos, Nosotros, Blog, Contacto**.
- Footer con información de contacto y enlaces útiles.

### 2. Catálogo de productos

- Pantalla **Productos** con listado de productos.
- Búsqueda por texto y filtros por categoría.
- Ordenamiento de productos (precio, etc.).
- Pantalla de **detalle de producto** con:
  - Descripción.
  - Imagen.
  - Precio.
  - Cantidad.
  - Mensaje personalizado para tortas.
  - Botón **“Agregar al carrito”**.

### 3. Carrito de compras

- Carrito siempre disponible desde el ícono de carrito en la barra superior.
- Ver listado de productos agregados, con:
  - Cantidad, mensaje personalizado y subtotal.
  - Botones para aumentar/disminuir cantidad.
  - Eliminar ítems del carrito.
- Resumen del pedido con:
  - **Subtotal, descuento, costo de despacho y total.**
- Selección de **tipo de envío**:
  - Despacho a domicilio (con costo).
  - Retiro en taller (sin costo).
  - Otras opciones según la pauta.
- Mensaje de confirmación al hacer clic en **“Continuar con el pago”**:
  - Se simula el registro del pedido y se limpia el carrito.

### 4. Autenticación de usuarios

- Pantalla de **Registro** con validaciones:
  - RUN chileno con dígito verificador.
  - Nombre y apellidos con largo máximo.
  - Correo electrónico con validación de formato.
  - Teléfono, fecha de nacimiento, región, comuna y dirección.
  - Contraseña con reglas básicas de seguridad y confirmación.
  - Código promocional opcional (`FELICES50`).
  - Opción de aceptar promociones.
- Registro de usuario usando **Room** (base de datos local).
- Al registrarse correctamente:
  - El usuario queda **logueado automáticamente**.
- Pantalla de **Login**:
  - Inicio de sesión con correo y contraseña.
  - Opción “Recordarme” (manejado en la lógica de la app).

### 5. Perfil de usuario

- Pantalla **Mi perfil**:
  - Muestra el nombre y correo del usuario logueado.
  - Permite editar datos personales (nombre, teléfono, dirección, etc.).
  - Permite cambiar la contraseña.
- Los datos se cargan desde la base de datos Room mediante `UserDao`.

### 6. Descuentos especiales en el carrito

Los siguientes descuentos se aplican automáticamente según el usuario logueado:

1. **50 % de descuento** para usuarios **mayores de 50 años** (se calcula desde la fecha de nacimiento).
2. **10 % de descuento de por vida** para quienes registraron el código promocional `FELICES50`.
3. **Torta gratis en cumpleaños** para estudiantes DUOC con correo institucional  
   (`@duoc.cl` o `@profesor.duoc.cl`) y fecha de nacimiento que coincida con el día actual.  

Cuando algún descuento se aplica, se muestra en el resumen del carrito y en las notas informativas.

---

## Tecnologías utilizadas

- **Kotlin** + **Jetpack Compose** para la interfaz de usuario.
- **Android Studio** como entorno de desarrollo.
- **Arquitectura por capas**:
  - `data` (fuentes de datos, Room, seed data).
  - `domain` (modelos y repositorios).
  - `presentation/ui` (ViewModels y pantallas Compose).
- **Room** para persistencia local de usuarios.
- **StateFlow / Flow** para manejo de estados en ViewModels.

---

## Requisitos para ejecutar la app

- Android Studio (versión reciente).
- JDK 17 (o la versión configurada en el proyecto).
- Dispositivo o emulador con **Android 8.0 (API 26)** o superior  
  (ajusta esta línea si tu `minSdk` es otra).

---

## Para ejecutar el proyecto

1. En tu computador, abre una terminal y clona el repositorio:

   ```bash
   git clone https://github.com/TU_USUARIO_GITHUB/MilSaboresApp.git
