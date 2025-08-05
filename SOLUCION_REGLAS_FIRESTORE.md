# 🔧 SOLUCIÓN URGENTE: Error de Permisos Firestore

## 🚨 **PROBLEMA CONFIRMADO:** 
Error `PERMISSION_DENIED` - Las reglas de Firestore están bloqueando el acceso.

## ✅ **SOLUCIÓN PASO A PASO:**

### **Paso 1: Ir a Firebase Console**
1. Abre: https://console.firebase.google.com/
2. Haz clic en tu proyecto: **"planifica-tu-viaje-a6f96"**

### **Paso 2: Acceder a Firestore Database**
1. En el menú lateral izquierdo, busca **"Firestore Database"**
2. Haz clic en **"Firestore Database"**

### **Paso 3: Ir a Rules (Reglas)**
1. En la parte superior, verás pestañas: "Data", "Rules", "Indexes", "Usage"
2. Haz clic en la pestaña **"Rules"**

### **Paso 4: Cambiar las Reglas**
Reemplaza TODO el contenido con estas reglas para desarrollo:

```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    // ⚠️ REGLAS ABIERTAS PARA DESARROLLO
    // 🔒 CAMBIAR EN PRODUCCIÓN
    match /{document=**} {
      allow read, write: if true;
    }
  }
}
```

### **Paso 5: Publicar las Reglas**
1. Haz clic en el botón **"Publish"** (color azul)
2. Confirma los cambios

### **Paso 6: Verificar**
- Las reglas deberían aparecer como "Active" 
- Debería mostrar la fecha/hora de la última actualización

## 🔍 **¿Cómo Saber si Funcionó?**

Después de cambiar las reglas:
1. **Prueba la app** registrando un nuevo usuario
2. **Completa el formulario**
3. **Busca en Logcat** estos mensajes:
   - `✅ Preferencias guardadas exitosamente`
   - En lugar de: `🚫 PERMISSION_DENIED`

## 📱 **Testing Inmediato:**

### **Antes del cambio:** (Error actual)
```
❌ PERMISSION_DENIED - Las reglas de Firestore no permiten escribir
🔧 Solución: Cambiar reglas en Firebase Console
```

### **Después del cambio:** (Funcionando)
```
✅ Preferencias guardadas exitosamente
🗺️ Navegando a MapsActivity
```

## 🎯 **Reglas Actuales vs Reglas Necesarias:**

### **❌ Reglas Restrictivas (Actuales):**
```javascript
// Probablemente tienes algo como:
match /{document=**} {
  allow read, write: if false; // O condiciones muy estrictas
}
```

### **✅ Reglas Abiertas (Necesarias para desarrollo):**
```javascript
match /{document=**} {
  allow read, write: if true; // Permite todo
}
```

## 🛡️ **Para Producción (Más Tarde):**

Cuando la app esté lista para producción, usa estas reglas más seguras:

```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /usuarios/{userId} {
      allow read, write: if request.auth != null && request.auth.uid == userId;
      
      match /{document=**} {
        allow read, write: if request.auth != null && request.auth.uid == userId;
      }
    }
  }
}
```

---

## 🚀 **¡ACCIÓN REQUERIDA!**

**Ve ahora mismo a Firebase Console y cambia las reglas.** 
En 2 minutos tendrás la app funcionando perfectamente.

Tu proyecto: https://console.firebase.google.com/project/planifica-tu-viaje-a6f96/firestore/rules
