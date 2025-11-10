# Configuración del Frontend Vue con Auth0

## Cambios Necesarios en el Frontend

### 1. Actualizar URL Base de la API

El frontend debe apuntar a la nueva URL HTTPS a través del Nginx WAF:

**Antes:**
```javascript
// .env o config
VITE_API_URL=http://localhost:8080
```

**Ahora:**
```javascript
// .env o config
VITE_API_URL=https://localhost:8443/uco-challenge
```

### 2. Configurar Axios/Fetch para Certificados Autofirmados

En desarrollo, necesitas configurar el cliente HTTP para aceptar certificados autofirmados:

#### Si usas Axios:
```javascript
// axios.js o api.js
import axios from 'axios';

const apiClient = axios.create({
  baseURL: import.meta.env.VITE_API_URL || 'https://localhost:8443/uco-challenge',
  timeout: 10000,
  // Para desarrollo: aceptar certificados autofirmados
  httpsAgent: process.env.NODE_ENV === 'development' 
    ? new (require('https').Agent)({ rejectUnauthorized: false })
    : undefined
});

// Interceptor para agregar token de Auth0
apiClient.interceptors.request.use((config) => {
  const token = localStorage.getItem('auth_token'); // o donde guardes el token
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export default apiClient;
```

#### Si usas Fetch nativo:
```javascript
// api.js
const API_BASE_URL = import.meta.env.VITE_API_URL || 'https://localhost:8443/uco-challenge';

export async function apiRequest(endpoint, options = {}) {
  const url = `${API_BASE_URL}${endpoint}`;
  
  const config = {
    ...options,
    headers: {
      'Content-Type': 'application/json',
      ...options.headers,
    },
  };

  // Agregar token de Auth0 si existe
  const token = localStorage.getItem('auth_token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }

  // En desarrollo, fetch acepta certificados autofirmados por defecto en navegadores
  // Pero puedes necesitar configurar esto en Node.js si usas SSR
  const response = await fetch(url, config);
  
  if (!response.ok) {
    throw new Error(`HTTP error! status: ${response.status}`);
  }
  
  return response.json();
}
```

### 3. Configurar Auth0

Auth0 ya está configurado en el backend. Solo asegúrate de que el frontend use la misma configuración:

```javascript
// auth0.config.js o main.js
import { createAuth0Client } from '@auth0/auth0-spa-js';

const auth0 = await createAuth0Client({
  domain: 'dev-l7bs34cafn0six34.us.auth0.com',
  clientId: 'TU_CLIENT_ID', // Tu client ID de Auth0
  authorizationParams: {
    redirect_uri: window.location.origin,
    audience: 'https://ucochallenge-api/' // Debe coincidir con el backend
  }
});
```

### 4. Variables de Entorno (.env)

Crea o actualiza tu archivo `.env` en el proyecto Vue:

```env
# .env
VITE_API_URL=https://localhost:8443/uco-challenge
VITE_AUTH0_DOMAIN=dev-l7bs34cafn0six34.us.auth0.com
VITE_AUTH0_CLIENT_ID=tu_client_id_aqui
VITE_AUTH0_AUDIENCE=https://ucochallenge-api/
```

### 5. CORS

El backend ya está configurado para aceptar requests desde:
- `http://localhost:5173` (Vite default)
- `https://localhost:5173`
- `http://localhost:3000`
- `https://localhost:3000`

No necesitas hacer cambios adicionales en CORS.

### 6. Si el Frontend también corre en HTTPS

Si quieres que el frontend Vue también corra en HTTPS (opcional):

```javascript
// vite.config.js
import { defineConfig } from 'vite';
import vue from '@vitejs/plugin-vue';

export default defineConfig({
  plugins: [vue()],
  server: {
    https: {
      // Usar certificados autofirmados para desarrollo
      key: './certs/backend.key',
      cert: './certs/backend.crt',
    },
    port: 5173,
  },
});
```

## Resumen de Cambios

1. ✅ **URL de API**: Cambiar a `https://localhost:8443/uco-challenge`
2. ✅ **Certificados**: Configurar para aceptar autofirmados en desarrollo
3. ✅ **Auth0**: Verificar que la configuración coincida con el backend
4. ✅ **Variables de entorno**: Actualizar `.env`
5. ⚠️ **HTTPS en frontend**: Opcional, solo si quieres

## Pruebas

```javascript
// test-api.js
// Probar conexión desde el frontend
const testConnection = async () => {
  try {
    const response = await fetch('https://localhost:8443/uco-challenge/api/v1/cities');
    const data = await response.json();
    console.log('✅ Conexión exitosa:', data);
  } catch (error) {
    console.error('❌ Error de conexión:', error);
  }
};
```

