<div align="center">

# ⚡ X-Fit & SportHouse — E-commerce & Catálogo Deportivo

**Solución móvil offline-first de alto rendimiento para la administración, visualización y control de inventario de tiendas deportivas.**  
*Desarrollado como proyecto de ingeniería de software con aplicación e impacto directo en entornos reales de retail deportivo.*

---

[![Android](https://img.shields.io/badge/Android-SDK%2024%20--%2036-3DDC84?style=for-the-badge&logo=android&logoColor=white)](https://developer.android.com/)
[![Java](https://img.shields.io/badge/Java-11-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![SQLite](https://img.shields.io/badge/SQLite-Local%20DB-003B57?style=for-the-badge&logo=sqlite&logoColor=white)](https://www.sqlite.org/)
[![Material Design](https://img.shields.io/badge/Material%20Design-3-757575?style=for-the-badge&logo=materialdesign&logoColor=white)](https://m3.material.io/)
[![Glide](https://img.shields.io/badge/Glide-4.16.0-4285F4?style=for-the-badge&logo=google&logoColor=white)](https://github.com/bumptech/glide)
[![Gradle](https://img.shields.io/badge/Gradle-9.3-02303A?style=for-the-badge&logo=gradle&logoColor=white)](https://gradle.org/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg?style=for-the-badge)](./LICENSE)
[![PRs Welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg?style=for-the-badge)](https://github.com/MiguelMD06/Gestor-Productos/pulls)

</div>

---

## 📌 Tabla de Contenidos

- [Descripción General](#-descripción-general)
- [Problema de Negocio y Solución](#-problema-de-negocio-y-solución)
- [Características Principales](#-características-principales)
- [Arquitectura de Software](#-arquitectura-de-software)
- [Stack Tecnológico](#-stack-tecnológico)
- [Modelo de Datos](#-modelo-de-datos)
- [Diseño y Sistema Visual](#-diseño-y-sistema-visual)
- [Demostración Visual](#-demostración-visual)
- [Requisitos Previos](#-requisitos-previos)
- [Instalación y Ejecución Local](#-instalación-y-ejecución-local)
- [Configuración de Entorno](#-configuración-de-entorno)
- [Contexto del Proyecto y Autoría](#-contexto-del-proyecto-y-autoría)
- [Hoja de Ruta (Roadmap)](#-hoja-de-ruta-roadmap)
- [Licencia](#-licencia)

---

## 📖 Descripción General

**X-Fit & SportHouse - Gestor de Productos** es una aplicación móvil nativa para Android diseñada para optimizar y agilizar la consulta, registro y actualización de precios e inventario en tiendas deportivas multimarca.

El proyecto implementa una arquitectura desacoplada y modular bajo el principio **DRY (Don't Repeat Yourself)**, persistencia relacional local con **SQLite**, manipulación reactiva de vistas con **View Binding**, y manejo de imágenes optimizado con **Glide** y persistencia de permisos URI mediante el **Storage Access Framework**.

---

## 💡 Problema de Negocio y Solución

| Desafío Operativo Previo | Solución Implementada con X-Fit |
| :--- | :--- |
| **Búsqueda lenta en mensajería:** En los puntos de venta de *XFit* y *SportHouse*, los vendedores debían rastrear listas de precios dispersas en chats de mensajería instantánea frente al cliente. | **Catálogo local centralizado:** Acceso instantáneo en menos de 1 segundo a listas de productos indexadas por tienda, con búsqueda visual por imagen y precio formateado. |
| **Pérdida de conectividad:** Las fluctuaciones de señal en los locales comerciales impedían consultar hojas de cálculo o catálogos en la nube. | **Offline-First:** Persistencia 100% nativa con SQLite embebido; la app opera sin interrupciones y con cero consumo de datos móviles. |
| **Duplicidad de mantenimiento:** Administrar dos tiendas con catálogos e identidades visuales distintas pero con la misma estructura funcional. | **Arquitectura Unificada (DRY):** Un único `FragmentsManager` paramétrico que segrega el catálogo por `tienda_id` compartiendo componentes de interfaz y lógica de negocio. |

---

## ✨ Características Principales

### 🏪 Gestión Multi-Tienda Independiente
- Segmentación por pestañas (*BottomNavigationView*) entre **XFit** (Tienda 1) y **SportHouse** (Tienda 2).
- Filtrado automático de inventario en base de datos según el contexto de la tienda seleccionada.
- Badges visuales identificativos con colores de marca diferenciados para cada producto.

### ⚡ CRUD Reactivo de Productos
- **Creación:** Formulario modal en `ProductDialog` con validaciones de campos (nombre no vacío, precio numérico válido y selección opcional de fotografía).
- **Lectura:** Visualización en `RecyclerView` con `LinearLayoutManager` optimizado para scroll fluido.
- **Actualización:** Edición en caliente de precios, nombres e imágenes con recarga atómica del adaptador.
- **Eliminación:** Diálogos de confirmación nativos (`AlertDialog.Builder`) para prevenir borrados accidentales en base de datos.

### 🖼️ Manejo Avanzado de Imágenes y Permisos
- Integración con el contrato moderno `ActivityResultContracts.GetContent()` registrado de forma segura en `onCreate`.
- Persistencia de permisos de acceso a la galería mediante `takePersistableUriPermission` con bandera `FLAG_GRANT_READ_URI_PERMISSION`, asegurando que las imágenes sigan siendo legibles tras reiniciar la aplicación o el dispositivo.
- Renderizado de alto rendimiento, escalado proporcional (`centerCrop`) y gestión de placeholders con la librería **Glide**.

### 🎨 Experiencia de Usuario Consistente (UI/UX)
- Interfaz basada en componentes de **Google Material Design 3**.
- Forzado de tema consistente (`AppCompatDelegate.MODE_NIGHT_NO`) para preservar el contraste y la fidelidad cromática corporativa.
- Retroalimentación inmediata mediante mensajes `Toast` y validaciones en los inputs (`EditText.setError`).

---

## 🏛️ Arquitectura de Software

La aplicación sigue una arquitectura modular centrada en componentes desacoplados de Android Jetpack:

```mermaid
graph TD
    User([Usuario / Vendedor]) <--> MA[MainActivity]
    MA -->|Navegación BottomNav| FM[FragmentsManager (DRY)]
    FM -->|Renderiza Catálogo| PA[ProductAdapter]
    FM -->|Abre Formulario| PD[ProductDialog]
    PD -->|Acceso a Galería| SAF[Storage Access Framework / Uri]
    PD -->|Guardar / Actualizar| DH[(DatabaseHelper - SQLite)]
    PA -->|Carga de Imagen| GL[Glide Image Loader]
    DH <-->|Persiste / Consulta| DB[(gestorproductos.db)]
```

### Estructura de Directorios

```text
app/src/main/
├── AndroidManifest.xml                  # Manifiesto, permisos y configuración de la app
├── java/com/example/gestorproductos/
│   ├── MainActivity.java                # Host principal y controlador de BottomNavigationView
│   ├── FragmentsManager.java            # Fragment unificado para XFit y SportHouse (Principio DRY)
│   ├── Product.java                     # Modelo de entidad de Producto (POJO)
│   ├── ProductAdapter.java              # Adaptador y ViewHolder para RecyclerView
│   ├── ProductDialog.java               # Controlador de modales para Crear y Editar productos
│   └── DatabaseHelper.java              # Capa de acceso a datos SQLiteOpenHelper (CRUD)
└── res/
    ├── color/nav_item_color.xml         # Estados de color para la barra de navegación
    ├── drawable/                        # Selectores, backgrounds vectoriales e íconos SVG
    ├── layout/
    │   ├── activity_main.xml            # Contenedor principal con BottomNavigationView
    │   ├── fragment_x_fit.xml           # Layout del catálogo de la tienda XFit
    │   ├── fragment_sport_house.xml     # Layout del catálogo de la tienda SportHouse
    │   ├── item_producto.xml            # CardView de producto en el RecyclerView
    │   └── dialog_producto.xml          # Layout del modal flotante de creación/edición
    ├── values/
    │   ├── colors.xml                   # Paleta cromática corporativa
    │   ├── strings.xml                  # Textos y recursos de internacionalización
    │   └── themes.xml                   # Estilos y temas Material
    └── navigation/nav_graph.xml         # Grafo de navegación de Jetpack
```

---

## 🛠️ Stack Tecnológico

| Capa / Dominio | Tecnología / Herramienta | Versión | Rol Funcional |
| :--- | :--- | :--- | :--- |
| **Plataforma** | Android SDK | API 24 - 36 | Runtime objetivo Android 16 (`compileSdk 36`, `minSdk 24`). |
| **Lenguaje** | Java SE | 11 (LTS) | Lógica de negocio, controladores y capa de acceso a datos. |
| **Build System** | Gradle (Kotlin DSL) | 9.3.1 (AGP 9.1.1) | Automatización de compilación, gestión de dependencias y empaquetado APK. |
| **UI Components** | Google Material Components | 1.14.0 | Botones flotantes (FAB), Badges, BottomNav y Cards. |
| **Layouts & Lists** | ConstraintLayout & RecyclerView | 2.2.1 / 1.3.2 | Vistas responsivas y listados eficientes con reciclaje de memoria. |
| **View Binding** | Android Gradle ViewBinding | Nativo | Enlace de vistas seguro con tipado estático, eliminando `findViewById`. |
| **Persistencia** | SQLite (`SQLiteOpenHelper`) | Embebido | Base de datos relacional local sin dependencias de red. |
| **Image Pipeline**| Bumptech Glide | 4.16.0 | Caching en memoria/disco, decodificación y transformación de imágenes. |
| **Pruebas** | JUnit & Espresso | 4.13.2 / 3.7.0 | Pruebas unitarias y de instrumentación UI. |

---

## 🗄️ Modelo de Datos

La aplicación persiste su información de manera relacional en la base de datos interna `gestorproductos.db`:

### Tabla: `product`

| Columna | Tipo de Dato | Restricciones | Descripción |
| :--- | :--- | :--- | :--- |
| `id` | `INTEGER` | `PRIMARY KEY AUTOINCREMENT` | Identificador único secuencial del producto. |
| `p_nombre` | `TEXT` | `NOT NULL` | Denominación comercial del artículo deportivo. |
| `p_precio` | `REAL` | `NOT NULL` | Precio de venta en moneda local. |
| `p_imagen` | `TEXT` | `NULLABLE` | Cadena URI del recurso multimedia en el almacenamiento del dispositivo. |
| `tienda_id` | `INTEGER` | `NOT NULL` | Clave de tienda: `1` = **XFit**, `2` = **SportHouse**. |

---

## 🎨 Diseño y Sistema Visual

La identidad visual ha sido formulada para transmitir dinamismo deportivo, legibilidad en pantallas AMOLED/IPS y alto contraste en entornos de mostrador con luz variable:

| Identificador | Muestra | Código HEX | Uso en la Aplicación |
| :--- | :---: | :--- | :--- |
| **Primary** | <img src="https://via.placeholder.com/20/1B3A5C/000000?text=+" width="20" height="20" /> | `#1B3A5C` | Barras de herramientas, encabezados modales y botón primario. |
| **Accent** | <img src="https://via.placeholder.com/20/F4801A/000000?text=+" width="20" height="20" /> | `#F4801A` | Floating Action Button (FAB de agregar) y acciones destacadas. |
| **Success / Price** | <img src="https://via.placeholder.com/20/4CAF82/000000?text=+" width="20" height="20" /> | `#4CAF82` | Resaltado tipográfico de precios formateados (`$ 00,000`). |
| **Background** | <img src="https://via.placeholder.com/20/F5F7FA/000000?text=+" width="20" height="20" /> | `#F5F7FA` | Fondo base con matiz frío para descanso visual. |
| **Surface** | <img src="https://via.placeholder.com/20/FFFFFF/000000?text=+" width="20" height="20" /> | `#FFFFFF` | Tarjetas de producto (`ItemView`) y cuerpo de diálogos. |
| **Text Primary** | <img src="https://via.placeholder.com/20/1A1A2E/000000?text=+" width="20" height="20" /> | `#1A1A2E` | Títulos y etiquetas de alta jerarquía. |
| **Border / Stroke** | <img src="https://via.placeholder.com/20/E0E4EB/000000?text=+" width="20" height="20" /> | `#E0E4EB` | Delimitadores de inputs y bordes sutiles en tarjetas. |

---

## 📱 Demostración Visual

<div align="center">
  <table>
    <tr>
      <td align="center"><b>Catálogo XFit</b></td>
      <td align="center"><b>Catálogo SportHouse</b></td>
      <td align="center"><b>Gestión & Diálogo Modal</b></td>
    </tr>
    <tr>
      <td>
        <img src="https://raw.githubusercontent.com/MiguelMD06/Gestor-Productos/master/app/src/main/res/drawable/xfit.xml" width="220" alt="Vista Tienda XFit" />
        <br />
        <sub><i>Catálogo segmentado con badge de marca</i></sub>
      </td>
      <td>
        <img src="https://raw.githubusercontent.com/MiguelMD06/Gestor-Productos/master/app/src/main/res/drawable/sporthouse.xml" width="220" alt="Vista Tienda SportHouse" />
        <br />
        <sub><i>Inventario independiente con navegación fluida</i></sub>
      </td>
      <td>
        <img src="https://via.placeholder.com/220x440/1B3A5C/FFFFFF?text=Dialogo+Crear/Editar" width="220" alt="Modal Crear y Editar" />
        <br />
        <sub><i>Formulario de alta/edición con selector de imagen</i></sub>
      </td>
    </tr>
  </table>
</div>

---

## 📋 Requisitos Previos

Antes de compilar y ejecutar el proyecto, asegúrate de contar con el siguiente entorno de desarrollo:

- **Java Development Kit (JDK):** Versión 11 o superior (recomendado JDK 17 o 21).
- **Android Studio:** Hedgehog (2023.1.1), Ladybug o versión posterior.
- **Android SDK:**
  - `compileSdk`: **36**
  - `minSdk`: **24** (Android 7.0 Nougat)
  - `targetSdk`: **36** (Android 16)
  - Build-Tools y Platform-Tools actualizados.
- **Dispositivo de Pruebas:** Emulador de Android Studio o teléfono físico con depuración USB habilitada (Android 7.0+).

---

## 🚀 Instalación y Ejecución Local

### 1. Clonar el Repositorio

```bash
git clone https://github.com/MiguelMD06/Gestor-Productos.git
cd Gestor-Productos
```

### 2. Configurar la Ubicación del Android SDK

Copia el archivo de plantilla `local.properties.example` y renómbralo a `local.properties`:

**En Windows (PowerShell):**
```powershell
Copy-Item local.properties.example local.properties
```

**En Linux / macOS:**
```bash
cp local.properties.example local.properties
```

Edita `local.properties` y define la ruta absoluta a tu Android SDK local:

```properties
sdk.dir=C\:\\Users\\<TuUsuario>\\AppData\\Local\\Android\\Sdk
# En macOS: sdk.dir=/Users/<TuUsuario>/Library/Android/sdk
# En Linux: sdk.dir=/home/<TuUsuario>/Android/Sdk
```

### 3. Compilación y Construcción por Terminal (CLI)

El proyecto incluye el binario Gradle Wrapper para garantizar compilaciones reproducibles:

**Verificar tareas disponibles:**
```bash
# Windows
.\gradlew.bat tasks

# Linux / macOS
./gradlew tasks
```

**Compilar el APK de depuración (Debug Build):**
```bash
# Windows
.\gradlew.bat assembleDebug

# Linux / macOS
./gradlew assembleDebug
```
*El binario generado se ubicará en:* `app/build/outputs/apk/debug/app-debug.apk`

**Instalar directamente en un dispositivo conectado (ADB):**
```bash
# Windows
.\gradlew.bat installDebug

# Linux / macOS
./gradlew installDebug
```

### 4. Ejecución desde Android Studio

1. Abre **Android Studio**.
2. Selecciona **Open** y navega hasta la carpeta raíz del proyecto clonado.
3. Espera a que la sincronización de Gradle finalice con éxito (*Gradle Sync*).
4. Elige tu dispositivo o emulador en la barra superior.
5. Presiona el botón verde de **Run** (`Shift + F10`).

---

## ⚙️ Configuración de Entorno

| Variable / Archivo | Ubicación | Propósito | Requerido |
| :--- | :--- | :--- | :---: |
| `local.properties` | Raíz del proyecto | Especifica el path absoluto al SDK de Android local. | **Sí** |
| `JAVA_HOME` | Variable de Sistema | Apunta a la instalación del JDK (Java 11+). | **Sí** |
| `ANDROID_HOME` | Variable de Sistema | Opcional si `local.properties` está configurado. | No |

> [!IMPORTANT]
> El archivo `local.properties` contiene rutas absolutas de tu máquina personal y está explícitamente ignorado en `.gitignore`. Nunca lo incluyas en tus commits de control de versiones.

---

## 💼 Contexto del Proyecto y Autoría

Este software fue diseñado e implementado como un **proyecto personal de desarrollo** para resolver una necesidad operativa real en mi entorno de trabajo en las tiendas deportivas *XFit* y *SportHouse*, aplicando estándares y buenas prácticas de ingeniería de software para desarrollo móvil:

- **Autor:** Miguel Medina Díaz ([@MiguelMD06](https://github.com/MiguelMD06))
- **Habilidades y Patrones Implementados:**
  - Persistencia de datos local estructurada con motor **SQLite** nativo y cero dependencia de red para entornos retail.
  - Implementación del principio **DRY** mediante refactorización de controladores duplicados a un componente unificado (`FragmentsManager`).
  - Manejo seguro de memoria e interfaces con **View Binding**.
  - Ciclo de vida robusto en Fragments y persistencia de permisos URI para galerías de imágenes (`Storage Access Framework`).
  - Aplicación de guías de diseño y accesibilidad de **Material Design 3**.

---

## 🗺️ Hoja de Ruta (Roadmap)

- [ ] **Escaneo de Código de Barras / QR:** Integración con *CameraX* y *ML Kit* para identificación inmediata de artículos en estantería.
- [ ] **Búsqueda y Filtros en Tiempo Real:** Barra de búsqueda predictiva con filtrado por rango de precios y nombres mediante `SearchView`.
- [ ] **Exportación de Catálogo a PDF:** Generación dinámica de catálogos imprimibles para enviar a clientes vía WhatsApp.
- [ ] **Sincronización en la Nube:** Migración o sincronización híbrida con backend REST / Firebase para actualización remota de inventario.
- [ ] **Soporte de Tema Oscuro Dinámico:** Adaptación de paleta a `values-night` para eficiencia energética en pantallas OLED.

---

## 📄 Licencia

Este proyecto se distribuye bajo la licencia **MIT**. Consulta el archivo [LICENSE](./LICENSE) para conocer los términos completos y condiciones de uso.

---

<div align="center">

Hecho con dedicación por [Miguel Medina Díaz (@MiguelMD06)](https://github.com/MiguelMD06) 🚀

</div>
