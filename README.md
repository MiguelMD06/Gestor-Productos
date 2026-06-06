# GestorProductos

Aplicación Android para gestionar el catálogo de productos de las tiendas deportivas **XFit** y **SportHouse** en las que laboro, esta aplicación resuelve una situación específica en mi jornada laboral, donde frecuentemente tengo que buscar en un chat todos los precios de los productos.

---

## Descripción

GestorProductos permite almacenar, visualizar y administrar los productos de dos tiendas deportivas desde un dispositivo Android. Cada producto se muestra con su nombre, precio e imagen, y puede ser creado, editado o eliminado directamente desde la app.

---

## Funcionalidades

### Principales
- Visualizar productos con nombre, precio e imagen
- Crear productos con todos sus detalles
- Editar productos existentes
- Eliminar productos

### Secundarias
- Manejo de dos tiendas independientes (XFit y SportHouse)
- Selección de imagen desde la galería del dispositivo
- Almacenamiento local con SQLite

---

## Arquitectura y estructura del proyecto

```
com.example.gestorproductos/
│
├── MainActivity.java           # Actividad principal, navegación entre fragments
├── Product.java                # Modelo de datos del producto
├── DatabaseHelper.java         # Manejo de base de datos SQLite (CRUD)
├── ProductAdapter.java         # Adapter del RecyclerView
├── ProductDialog.java          # Lógica del AlertDialog para crear/editar productos
├── XFit.java                   # Fragment de la tienda XFit
└── SportHouse.java             # Fragment de la tienda SportHouse

res/
├── layout/
│   ├── activity_main.xml       # Layout principal con BottomNavigationView
│   ├── fragment_x_fit.xml      # Layout del fragment XFit
│   ├── fragment_sport_house.xml# Layout del fragment SportHouse
│   ├── item_producto.xml       # Layout del item del RecyclerView
│   └── dialog_producto.xml     # Layout del AlertDialog
├── drawable/
│   ├── bg_imagen_producto.xml
│   ├── bg_badge_tienda.xml
│   ├── bg_btn_editar.xml
│   ├── bg_btn_eliminar.xml
│   ├── bg_selector_imagen.xml
│   └── bg_input.xml
├── menu/
│   └── menu_navegacion.xml     # Menú del BottomNavigationView
└── color/
    └── nav_item_color.xml      # Selector de color para íconos del nav
```

---

## Base de datos

La app usa **SQLite** local con una única tabla:

**Tabla: `product`**

| Columna     | Tipo    | Descripción                        |
|-------------|---------|------------------------------------|
| id          | INTEGER | Clave primaria autoincremental     |
| p_nombre    | TEXT    | Nombre del producto                |
| p_precio    | REAL    | Precio del producto                |
| p_imagen    | TEXT    | URI de la imagen (como String)     |
| tienda_id   | INTEGER | 1 = XFit, 2 = SportHouse           |

---

## Paleta de colores

| Nombre              | Hex       | Uso                                 |
|---------------------|-----------|-------------------------------------|
| Fondo               | `#F5F7FA` | Fondo general de la app             |
| Primario            | `#1B3A5C` | Toolbar, header del dialog, botón editar |
| Acento              | `#F4801A` | FAB, botón guardar                  |
| Superficie          | `#FFFFFF` | Cards, fondo del dialog             |
| Texto principal     | `#1A1A2E` | Nombre del producto                 |
| Precio              | `#4CAF82` | Precio del producto                 |
| Borde               | `#E0E4EB` | Bordes de cards e inputs            |

---

## Dependencias

```gradle
implementation("androidx.recyclerview:recyclerview:1.3.2")
implementation("com.github.bumptech.glide:glide:4.16.0")
annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")
```

---

## Requisitos

- Android Studio Hedgehog o superior
- SDK mínimo: API 24 (Android 7.0)
- SDK objetivo: API 36
- Java 11

---

## Permisos

```xml
<uses-permission android:name="android.permission.READ_MEDIA_IMAGES"/>
```

---

## Líneas de trabajo futuro

- Soporte para tomar fotos con la cámara (CameraX)
- Gestión dinámica de tiendas
- Búsqueda y filtrado de productos
- Exportar catálogo a PDF
- Tema oscuro personalizado

---

## Notas de desarrollo

- La app fuerza el **tema claro** con `AppCompatDelegate.MODE_NIGHT_NO` para garantizar consistencia visual con la paleta de colores definida.
- Las imágenes se almacenan como URI persistente usando `takePersistableUriPermission`, lo que garantiza acceso a la imagen aunque la app se reinicie.
- El `ActivityResultLauncher` para la galería se registra en `onCreate` del fragment para evitar el error `unregistered ActivityResultLauncher`.
