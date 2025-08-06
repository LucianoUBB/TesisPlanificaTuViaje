# 🧳 TesisPlanificaTuViaje

**Una aplicación Android completa para la planificación inteligente de viajes basada en preferencias personalizadas**

[![Android](https://img.shields.io/badge/Android-6.0%2B-green.svg)](https://android.com/) [![Firebase](https://img.shields.io/badge/Firebase-Enabled-orange.svg)](https://firebase.google.com/) [![Java](https://img.shields.io/badge/Java-8%2B-blue.svg)](https://java.com/) [![License](https://img.shields.io/badge/License-Academic-yellow.svg)](LICENSE)

## 📱 Descripción

TesisPlanificaTuViaje es una aplicación móvil desarrollada como proyecto de tesis que implementa un sistema inteligente de planificación de viajes. La aplicación utiliza un formulario multi-paso para capturar las preferencias del usuario y genera recomendaciones personalizadas de destinos y actividades.

## ✨ Características Principales

### 🔐 Sistema de Autenticación Completo
- **Registro y Login** con Firebase Authentication
- **Sesión persistente** que mantiene al usuario logueado
- **Verificación automática** de estado de autenticación
- **Cierre de sesión seguro** con limpieza completa del stack

### 📋 Formulario Multi-Paso Inteligente
**6 pasos estructurados para capturar preferencias:**

1. **📝 Información Personal**
   - Nombre de usuario, edad y género
   - Validación en tiempo real
   - Modo edición con campos protegidos

2. **🍽️ Sabores y Clima**
   - Preferencias gastronómicas (múltiple selección)
   - Tipo de clima preferido (selección única)
   - Campos personalizables con "Otro"

3. **🏝️ Vacaciones y Actividades**
   - Tipos de vacaciones favoritos
   - Actividades de interés
   - Opciones expandibles

4. **🚗 Viaje y Presupuesto**
   - Estación del año preferida
   - Medio de transporte
   - Rango de presupuesto

5. **📅 Planificación y Deportes**
   - Estilo de planificación de viajes
   - Deportes y actividades físicas
   - Manejo de preferencias deportivas

6. **📊 Resumen Final**
   - Vista consolidada de todas las preferencias
   - Confirmación antes del guardado
   - Navegación bidireccional

### 🗺️ Navegación y Mapas
- **Google Maps** integrado para visualización
- **Sistema de navegación unificado** con 4 pantallas principales:
  - 🏠 **Inicio (Mapas)**: Vista principal con mapas interactivos
  - 👤 **Perfil**: Gestión de datos personales y preferencias
  - 🤖 **ChatBot**: Asistente virtual (preparado para IA)
  - 📚 **Historial**: Registro de viajes y búsquedas

### 👤 Gestión de Perfil Avanzada
- **Visualización de datos** personales
- **Edición de preferencias** con modo protegido para campos críticos
- **Actualización en tiempo real** de la información
- **Navegación fluida** entre perfil y formulario

### 🔥 Integración Firebase Completa
- **Firestore Database**: Almacenamiento de preferencias del usuario
- **Persistencia offline**: Funcionamiento sin conexión
- **Estructura de datos optimizada**: Colecciones jerárquicas por usuario
- **Sincronización automática**: Cuando se restaura la conexión

## 🏗️ Arquitectura Técnica

### 📁 Estructura del Proyecto
```
app/src/main/java/com/luciano/tesisplanificatuviaje/
├── 📱 Activities/
│   ├── SplashActivity.java          # Pantalla de carga inicial
│   ├── LoginActivity.java           # Autenticación
│   ├── RegisterActivity.java        # Registro de usuarios
│   ├── FormularioActivity.java      # Coordinador del formulario
│   ├── MapsActivity.java            # Vista principal con mapas
│   ├── PerfilActivity.java          # Gestión de perfil
│   ├── ChatBotActivity.java         # Asistente virtual
│   └── HistorialActivity.java       # Historial de actividad
│
├── 📋 Fragments/
│   ├── FormularioPaso1Fragment.java # Información personal
│   ├── FormularioPaso2Fragment.java # Sabores y clima
│   ├── FormularioPaso3Fragment.java # Vacaciones y actividades
│   ├── FormularioPaso4Fragment.java # Viaje y presupuesto
│   ├── FormularioPaso5Fragment.java # Planificación y deportes
│   └── ResumenFormularioFragment.java # Resumen final
│
└── 🔧 Utils/
    └── MyApplication.java           # Configuración global
```

### 🔄 Flujo de Datos

```
1️⃣ Usuario inicia app → SplashActivity
2️⃣ Verificación de autenticación (Firebase Auth)
3️⃣ Si no está logueado → LoginActivity
4️⃣ Si está logueado → Verificación de preferencias
5️⃣ Sin preferencias → FormularioActivity (6 pasos)
6️⃣ Con preferencias → MapsActivity (pantalla principal)
7️⃣ Navegación libre entre Mapas, Perfil, ChatBot, Historial
```

### 💾 Modelo de Datos

**Estructura en Firestore:**
```javascript
usuarios/{userId}/
└── preferencias/
    └── datos/
        ├── nombreUsuario: String
        ├── edad: Integer
        ├── sexo: String
        ├── sabores: Array<String>
        ├── clima: String
        ├── vacaciones: Array<String>
        ├── actividades: Array<String>
        ├── estacion: String
        ├── transporte: String
        ├── presupuesto: String
        ├── planificacion: String
        └── deportes: Array<String>
```

## 🛠️ Tecnologías Utilizadas

### Backend
- **Firebase Authentication** - Gestión de usuarios
- **Cloud Firestore** - Base de datos NoSQL en tiempo real
- **Firebase SDK** - Integración completa con servicios de Google

### Frontend
- **Android SDK** - Desarrollo nativo
- **Java 8+** - Lenguaje principal
- **Material Design** - Diseño de interfaz
- **Fragment Architecture** - Navegación modular
- **Google Maps SDK** - Mapas interactivos

### Herramientas de Desarrollo
- **Android Studio** - IDE principal
- **Gradle** - Sistema de construcción
- **Firebase Console** - Administración de backend
- **Git** - Control de versiones

## 🚀 Instalación y Configuración

### Prerrequisitos
- Android Studio 4.0+
- Android SDK 23+ (Android 6.0)
- Cuenta de Firebase
- Google Play Services

### Configuración Local

1. **Clonar el repositorio:**
```bash
git clone https://github.com/LucianoUBB/TesisPlanificaTuViaje.git
cd TesisPlanificaTuViaje
```

2. **Configurar Firebase:**
   - Crear proyecto en [Firebase Console](https://console.firebase.google.com/)
   - Añadir aplicación Android
   - Descargar `google-services.json`
   - Colocar en `app/google-services.json`

3. **Configurar Google Maps API:**
   - Copiar archivo de ejemplo: `cp google_maps_api.xml.example app/src/main/res/values/google_maps_api.xml`
   - Crear API Key en [Google Cloud Console](https://console.cloud.google.com/)
   - Reemplazar `YOUR_API_KEY_HERE` con tu API Key real
   - ⚠️ **IMPORTANTE**: El archivo con tu API Key real NO debe subirse a Git

4. **Habilitar servicios Firebase:**
   - Authentication (Email/Password)
   - Cloud Firestore Database
   - Google Maps API

5. **Compilar y ejecutar:**
```bash
./gradlew build
./gradlew installDebug
```

## 🎯 Casos de Uso

### Usuario Nuevo
1. Registro con email y contraseña
2. Completar formulario de 6 pasos
3. Guardar preferencias en Firebase
4. Acceder a vista de mapas principal

### Usuario Existente
1. Login automático con sesión persistente
2. Navegación directa a mapas
3. Edición de preferencias desde perfil
4. Consulta de historial de actividad

### Administración de Perfil
1. Visualización de datos personales
2. Edición protegida de preferencias
3. Campos críticos bloqueados (ej: edad)
4. Actualización en tiempo real

## 📊 Características de Validación

### Validaciones del Formulario
- **Campos obligatorios**: Nombre, edad, sexo, preferencias básicas
- **Rangos de edad**: 16-100 años
- **Validación de email**: Formato válido requerido
- **Campos personalizados**: Validación de texto cuando se selecciona "Otro"
- **Selección mínima**: Al menos una opción en campos múltiples

### Manejo de Errores
- **Conectividad**: Funcionamiento offline con sincronización posterior
- **Autenticación**: Redirección automática al login si expira sesión
- **Firebase**: Manejo específico de errores de permisos y conexión
- **Validación UX**: Mensajes claros y contextuales

## 🔮 Roadmap Futuro

### Próximas Implementaciones

#### 🤖 Chatbot con IA
- Integración con GPT/Gemini API
- Recomendaciones personalizadas basadas en preferencias
- Consultas en lenguaje natural
- Historial de conversaciones

#### 🌐 API Backend
- Servidor Node.js/Python
- Procesamiento de recomendaciones
- Integración con APIs de viajes
- Sistema de notificaciones push

#### 💻 Dashboard Web
- Panel de administración
- Analytics de usuarios
- Gestión de contenido
- Reportes y estadísticas

#### 📱 Funcionalidades Móviles
- Recomendaciones de destinos
- Planificación automática de itinerarios
- Integración con booking APIs
- Compartir viajes en redes sociales

## 👨‍💻 Desarrollo y Contribución

### Estructura de Desarrollo
- **Patrón**: Activity + Fragment Architecture
- **Estado**: Gestión centralizada en Activities
- **Navegación**: Bottom Navigation con 4 pantallas principales
- **Persistencia**: Firebase Firestore con cache offline

### Estándares de Código
- Logging detallado con tags específicos
- Validación robusta en cada paso
- Manejo de errores comprehensivo
- Documentación inline completa

## 📄 Licencia

Este proyecto es desarrollado como tesis académica. Todos los derechos reservados.

---

## 📞 Contacto

**Desarrollador**: Luciano  
**Universidad**: UBB  
**Proyecto**: Tesis de Grado  
**GitHub**: [@LucianoUBB](https://github.com/LucianoUBB)

---

### 🙏 Agradecimientos

- Universidad del Bío-Bío por el apoyo académico
- Firebase por la infraestructura de backend
- Google por las APIs de Maps y servicios asociados
- Comunidad Android por la documentación y recursos

---

*Desarrollado con ❤️ para hacer la planificación de viajes más inteligente y personalizada*
