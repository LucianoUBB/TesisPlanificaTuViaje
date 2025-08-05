# 🔐 Sistema de Sesión Persistente - Diagrama de Flujo

## 📱 Flujo de Autenticación y Navegación

```
📱 INICIO DE APP
       ↓
   SplashActivity
       ↓
¿Usuario logueado? (Firebase Auth)
   ↙        ↘
  NO         SÍ
   ↓          ↓
LoginActivity  ¿Tiene preferencias?
   ↓          ↙        ↘
✅ Login    NO         SÍ
   ↓          ↓          ↓
¿Tiene prefs? FormularioActivity MapsActivity
↙      ↘         ↓         ↓
NO     SÍ     ✅ Completa  ✅ Navega
↓      ↓     formulario    libremente
Formulario Maps     ↓         ↓
Activity Activity   MapsActivity PerfilActivity
                              ↓
                        🚪 Cerrar Sesión
                              ↓
                         LoginActivity
```

## 🛡️ Verificaciones de Seguridad

### 🔍 **En cada Activity:**
- **SplashActivity**: Verifica estado inicial del usuario
- **LoginActivity**: Verifica sesión existente en onCreate()
- **MapsActivity**: Verifica sesión en onCreate() y onResume()
- **FormularioActivity**: Verifica sesión al iniciar
- **PerfilActivity**: Maneja cierre de sesión completo
- **ChatBotActivity**: Verifica sesión antes de permitir chat

### 🔄 **Flujo de Cierre de Sesión:**
1. Usuario presiona "Cerrar Sesión" en PerfilActivity
2. Confirmación con AlertDialog
3. `mAuth.signOut()` - limpia sesión de Firebase
4. Intent con flags `NEW_TASK | CLEAR_TASK` - limpia stack
5. Redirige a LoginActivity
6. Usuario debe autenticarse nuevamente

### ✅ **Beneficios de la Implementación:**
- **Persistencia**: Usuario permanece logueado entre sesiones
- **Seguridad**: Verificación constante de estado de sesión
- **UX Mejorada**: No necesita login repetido
- **Navegación Inteligente**: Redirige según estado del usuario
- **Limpieza Completa**: Cierre de sesión elimina toda la pila de actividades

## 🎯 **Estados de Usuario:**

### 👤 **Usuario No Autenticado:**
- SplashActivity → LoginActivity
- Debe autenticarse para continuar

### 🆕 **Usuario Nuevo (Autenticado sin Preferencias):**
- SplashActivity → FormularioActivity
- Debe completar formulario antes de Maps

### ✅ **Usuario Completo (Autenticado con Preferencias):**
- SplashActivity → MapsActivity
- Acceso completo a todas las funciones

### 🔄 **Usuario Returning:**
- Mantiene sesión automáticamente
- Va directo a su estado correspondiente
