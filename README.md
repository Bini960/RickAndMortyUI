# RickAndMorty

App de Android en Kotlin con Jetpack Compose, desarrollada para el curso de Programación de Plataformas Móviles. Incluye un login con el nombre del usuario y sesión persistente, un listado de personajes de Rick & Morty con su pantalla de detalle, la opción de marcarlos como favoritos y de crearlos, editarlos y eliminarlos, un listado de locations con su pantalla de detalle, favoritas y filtro, y una pantalla de perfil con cierre de sesión. Los datos se obtienen de la API pública de Rick & Morty mediante Ktor al iniciar sesión y se guardan en una base de datos local, por lo que la app funciona sin conexión y los cambios del usuario existen solo en el dispositivo. Las secciones principales se recorren mediante una barra de navegación inferior.

## Estructura

- `main` – código del laboratorio 8
- `laboratorio7` – login, listado de personajes y detalle de personaje, con navegación type-safe (`@Serializable`) y carga de imágenes con Coil
- `laboratorio8` – barra de navegación inferior (`NavigationBar`) con las secciones Characters, Locations y Profile; grafos de navegación anidados para Characters y Locations; detalle de location y cierre de sesión que vacía el back stack.
- `laboratorio9` – arquitectura con ViewModel y StateFlow en los listados y detalles de personajes y locations; capa de repositorios con latencia simulada; layouts de carga y de error con opción de reintentar.
- `laboratorio10` – login con campo de nombre; sesión persistente con DataStore Preferences; pantalla de inicio que decide si abrir el login o la lista de personajes; perfil con el nombre guardado y cierre de sesión que elimina el nombre.
- `laboratorio11` – base de datos local con Room (tablas `characters` y `locations`, con su entidad, DAO y funciones de mapeo); sincronización inicial al iniciar sesión con indicador de carga; listados y detalles que leen Room; personajes favoritos con filtro y vista vacía; cierre de sesión que borra los datos locales.
- `laboratorio12` – Offline First: los personajes y las locations se obtienen del API de Rick & Morty con Ktor únicamente al iniciar sesión (con precarga de las imágenes) y se guardan en Room, de donde leen todas las pantallas; cliente único en `HttpClientFactory`; repositorios con interfaz e implementación que combinan la API y Room; crear, editar y eliminar personajes de forma local, incluso los que vienen del API; locations favoritas con filtro entre todas, favoritas y no favoritas.

## APK

La versión compilada (debug) de la rama `laboratorio12` se encuentra en `apk/RickAndMorty.apk` y requiere Android 8.0 (API 26) o superior. Para instalarla:

1. Descargar el archivo `apk/RickAndMorty.apk` desde este repositorio.
2. En un emulador de Android Studio, arrastrar el archivo sobre la ventana del emulador. En un teléfono, copiar el archivo al dispositivo, abrirlo y permitir la instalación desde esa fuente.
3. Como alternativa, con el dispositivo o emulador conectado, ejecutar `adb install -r apk/RickAndMorty.apk`.
4. Abrir la aplicación RickAndMorty, escribir un nombre e iniciar sesión. El primer inicio de sesión requiere conexión a internet; después la app funciona sin conexión.

## Herramientas

- Kotlin
- Android Studio
- Jetpack Compose
- Navigation Compose
- Coil
- ViewModel y StateFlow
- DataStore Preferences
- Room y KSP
- Ktor Client
- kotlinx.serialization

**Para ejecutar este proyecto:**

1. Clonar el repositorio.
2. Cambiar a la rama correspondiente (ej. `git checkout laboratorio7`).
3. Abrir la carpeta en Android Studio.
4. Esperar a que Gradle sincronice las dependencias.
5. Ejecutar en un emulador o dispositivo físico con el botón "Run".