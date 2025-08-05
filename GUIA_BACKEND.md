# 📋 Guía de Configuración Backend para Tesis

## 🎯 Estructura Recomendada para tu Backend (PT2025)

### 📁 Endpoints Necesarios para la App Android:

```javascript
// 👤 USUARIOS
POST /api/usuarios/registro
POST /api/usuarios/login
GET  /api/usuarios/:id

// 🎯 RECOMENDACIONES  
GET  /api/recomendaciones?userId=:id
POST /api/recomendaciones

// 🤖 CHATBOT
POST /api/chatbot/mensaje

// 📍 LUGARES Y RUTAS
GET  /api/lugares?tipo=:tipo&ubicacion=:ubicacion
GET  /api/rutas/:userId

// 🔧 UTILIDADES
GET  /api/health (para probar conexión)
```

## 🚀 Configuración según Tecnología:

### Si usas **Node.js + Express**:
```javascript
// server.js o app.js
const express = require('express');
const cors = require('cors');
const app = express();

// Middleware
app.use(cors()); // Permitir requests desde Android
app.use(express.json());

// Puerto
const PORT = process.env.PORT || 3000;

// Rutas básicas
app.get('/api/health', (req, res) => {
    res.json({ status: 'OK', message: 'Backend funcionando' });
});

// Tu webhook existente
app.post('/webhook', (req, res) => {
    // Tu código de webhook aquí
});

app.listen(PORT, () => {
    console.log(`🚀 Servidor corriendo en puerto ${PORT}`);
});
```

### Si usas **Python Flask**:
```python
# app.py
from flask import Flask, jsonify, request
from flask_cors import CORS

app = Flask(__name__)
CORS(app)  # Permitir requests desde Android

@app.route('/api/health')
def health():
    return jsonify({"status": "OK", "message": "Backend funcionando"})

# Tu webhook existente
@app.route('/webhook', methods=['POST'])
def webhook():
    # Tu código de webhook aquí
    pass

if __name__ == '__main__':
    app.run(debug=True, port=3000)
```

## 🏫 Para subir al Servidor Universitario:

### 1. Preparar el código:
- Asegurar que todas las dependencias estén en `package.json` o `requirements.txt`
- Configurar variables de entorno para la URL
- Probar localmente primero

### 2. Configuración de CORS:
```javascript
// Para Node.js
app.use(cors({
    origin: ['http://localhost:3000', 'https://tu-dominio-universidad.edu.ar'],
    methods: ['GET', 'POST', 'PUT', 'DELETE'],
    allowedHeaders: ['Content-Type', 'Authorization']
}));
```

### 3. Variables de entorno:
```bash
# .env
PORT=3000
NODE_ENV=production
DATABASE_URL=tu_base_de_datos
```

## 📱 URLs para la App Android:

### Desarrollo Local:
- Emulador: `http://10.0.2.2:3000/api/`
- Dispositivo físico: `http://TU_IP_LOCAL:3000/api/`

### Servidor Universidad:
- `https://servidor.universidad.edu.ar:3000/api/`
- O el dominio que te asignen

## 🔧 Próximos Pasos:

1. **Revisar tu backend actual** en `D:\Personal\Estudios\Ramos\2024\Tesis\PT2025`
2. **Identificar la tecnología** que usas
3. **Encontrar/agregar el puerto** de configuración
4. **Probar localmente** antes de subir
5. **Subir al servidor** universitario
6. **Actualizar la URL** en la app Android

## 💡 Tu Webhook:
Si ya tienes un webhook, podría servir para:
- Recibir notificaciones de DialogFlow
- Integrar con servicios externos
- Procesar eventos de la app

¿Podrías contarme más sobre tu webhook y la estructura actual de tu backend?
