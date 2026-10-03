# RickAndMorty

App de Android en Kotlin con Jetpack Compose, desarrollada para el curso de Programación de Plataformas Móviles. Incluye una pantalla de login, un listado de personajes de Rick & Morty con su pantalla de detalle, un listado de locations con su pantalla de detalle y una pantalla de perfil con cierre de sesión. Las secciones principales se recorren mediante una barra de navegación inferior.

## Estructura

- `main` – proyecto base generado por Android Studio
- `laboratorio7` – login, listado de personajes y detalle de personaje, con navegación type-safe (`@Serializable`) y carga de imágenes con Coil
- `laboratorio8` – barra de navegación inferior (`NavigationBar`) con las secciones Characters, Locations y Profile; grafos de navegación anidados para Characters y Locations; detalle de location y cierre de sesión que vacía el back stack.

## Herramientas

- Kotlin
- Android Studio
- Jetpack Compose
- Navigation Compose
- Coil

**Para ejecutar este proyecto:**

1. Clonar el repositorio.
2. Cambiar a la rama correspondiente (ej. `git checkout laboratorio7`).
3. Abrir la carpeta en Android Studio.
4. Esperar a que Gradle sincronice las dependencias.
5. Ejecutar en un emulador o dispositivo físico con el botón "Run".
