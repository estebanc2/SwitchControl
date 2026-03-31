# 🎨 feature/modern-ui — Rediseño SwitchControl Android

Branch con la versión modernizada de la UI usando **Jetpack Compose + Material 3 + Dark Theme**.

---

## 1. Crear el branch

```bash
git checkout master
git pull origin master
git checkout -b feature/modern-ui
```

---

## 2. Dependencias a agregar

### `app/build.gradle.kts`

```kotlin
android {
    compileSdk = 34

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.8"
    }
}

dependencies {
    // Compose BOM — gestiona todas las versiones automáticamente
    val composeBom = platform("androidx.compose:compose-bom:2024.02.00")
    implementation(composeBom)
    androidTestImplementation(composeBom)

    // Compose Core
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")

    // Material 3
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")

    // Activity Compose
    implementation("androidx.activity:activity-compose:1.8.2")

    // Navigation Compose
    implementation("androidx.navigation:navigation-compose:2.7.6")

    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")
}
```

---

## 3. Archivos del branch

```
app/src/main/java/com/example/switchcontrol/
├── ui/
│   ├── theme/
│   │   ├── Theme.kt              ← Material 3 Dark theme + Dynamic Color (Android 12+)
│   │   └── Type.kt               ← Escala tipográfica
│   ├── screens/
│   │   ├── DashboardScreen.kt    ← Lista de interruptores (Pantalla 1)
│   │   ├── DeviceDetailScreen.kt ← Config del interruptor (Pantalla 2)
│   │   ├── TimerBottomSheet.kt   ← Config de timers (Dialog → BottomSheet)
│   │   ├── MaintenanceBottomSheet.kt ← Mantenimiento (Dialog → BottomSheet)
│   │   └── ColorPickerBottomSheet.kt ← Selector de color moderno
│   └── components/
│       └── DeviceCard.kt         ← Card por dispositivo + ModernSwitch
└── MainActivity.kt               ← Migrar a setContent { SwitchControlTheme { ... } }
```

---

## 4. Cambios en MainActivity

```kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Edge-to-edge (Android 15 ready)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            SwitchControlTheme {
                // Tu NavHost o pantalla principal acá
                // Reemplaza el setContentView() existente
                DashboardScreen(
                    switches    = viewModel.switches.collectAsState().value,
                    onAddSwitch = { /* navegar a agregar */ },
                    onSwitchClick = { id -> /* navegar a detalle */ },
                    onToggle    = { id, isOn -> viewModel.toggle(id, isOn) }
                )
            }
        }
    }
}
```

---

## 5. Cómo mapear el modelo existente a `SwitchUiModel`

El modelo `SwitchUiModel` es solo para la capa visual. Adaptá el mapping
desde tu modelo de datos existente:

```kotlin
fun MiInterruptorModel.toUiModel(): SwitchUiModel = SwitchUiModel(
    id          = this.id,
    name        = this.nombre,
    isOn        = this.estado,
    statusText  = this.textoEstado,   // ej: "Cambia en 12:51 h"
    accentColor = colorNameToAccent(this.color),   // "orange" → DeviceOrange
    icon        = iconForDeviceType(this.tipo)     // opcional, ver abajo
)

fun iconForDeviceType(tipo: String): ImageVector = when (tipo.lowercase()) {
    "luz", "light"   -> Icons.Rounded.Lightbulb
    "riego", "water" -> Icons.Rounded.WaterDrop
    "filtro"         -> Icons.Rounded.FilterAlt
    "calefa", "heat" -> Icons.Rounded.Thermostat
    "termo"          -> Icons.Rounded.LocalFireDepartment
    else             -> Icons.Rounded.PowerSettingsNew
}
```

---

## 6. Cambios visuales vs versión actual

| Elemento               | Antes                              | Después (este branch)             |
|------------------------|------------------------------------|-----------------------------------|
| Fondo                  | Blanco                             | Dark (#030712)                    |
| Cards                  | Fondo de color sólido (naranja, verde…) | Dark con borde de color + glow |
| Switch                 | Ícono de palanca dibujado           | Material 3 Switch con animación  |
| Status text            | Negro sobre color                  | Color del dispositivo (sutil)     |
| Dialogs                | Floating dialogs                   | ModalBottomSheets con drag        |
| Color picker           | Botón con texto del color          | Grid circular de colores          |
| Timer config           | Diálogo flotante                   | BottomSheet con time spinners     |
| Mantenimiento          | Diálogo flotante                   | BottomSheet + confirm dialog      |
| Botones acción         | TextButtons con ícono pequeño      | Button filled / outlined          |
| Dark mode              | No soportado                       | Completo + Dynamic Color Android 12+ |

---

## 7. Lo que NO cambia

- ✅ Toda la lógica de red / WiFi
- ✅ ViewModels y repositorios
- ✅ Modelos de datos
- ✅ Configuración de timers (lógica)
- ✅ Sistema de colores guardados (los strings "orange", "green", etc.)

---

## 8. Push del branch

```bash
git add .
git commit -m "feat: modern UI redesign with Jetpack Compose Material 3"
git push origin feature/modern-ui
```
