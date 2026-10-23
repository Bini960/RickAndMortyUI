# RickAndMorty

App de Android en Kotlin con Jetpack Compose, desarrollada para el curso de Programación de Plataformas Móviles. Incluye un login con el nombre del usuario y sesión persistente, un listado de personajes de Rick & Morty con su pantalla de detalle y la opción de marcarlos como favoritos, un listado de locations con su pantalla de detalle y una pantalla de perfil con cierre de sesión. Los datos se guardan en una base de datos local y las secciones principales se recorren mediante una barra de navegación inferior.

## Estructura

- `main` – código del laboratorio 8
- `laboratorio7` – login, listado de personajes y detalle de personaje, con navegación type-safe (`@Serializable`) y carga de imágenes con Coil
- `laboratorio8` – barra de navegación inferior (`NavigationBar`) con las secciones Characters, Locations y Profile; grafos de navegación anidados para Characters y Locations; detalle de location y cierre de sesión que vacía el back stack.
- `laboratorio9` – arquitectura con ViewModel y StateFlow en los listados y detalles de personajes y locations; capa de repositorios con latencia simulada; layouts de carga y de error con opción de reintentar.
- `laboratorio10` – login con campo de nombre; sesión persistente con DataStore Preferences; pantalla de inicio que decide si abrir el login o la lista de personajes; perfil con el nombre guardado y cierre de sesión que elimina el nombre.
- `laboratorio11` – base de datos local con Room (tablas `characters` y `locations`, con su entidad, DAO y funciones de mapeo); sincronización inicial al iniciar sesión con indicador de carga; listados y detalles que leen Room; personajes favoritos con filtro y vista vacía; cierre de sesión que borra los datos locales.

## Herramientas

- Kotlin
- Android Studio
- Jetpack Compose
- Navigation Compose
- Coil
- ViewModel y StateFlow
- DataStore Preferences
- Room y KSP

**Para ejecutar este proyecto:**

1. Clonar el repositorio.
2. Cambiar a la rama correspondiente (ej. `git checkout laboratorio7`).
3. Abrir la carpeta en Android Studio.
4. Esperar a que Gradle sincronice las dependencias.
5. Ejecutar en un emulador o dispositivo físico con el botón "Run".