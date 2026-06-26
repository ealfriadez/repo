# Bookstore AI - Frontend Angular

Frontend moderno desarrollado con Angular 18+ que consume los endpoints de IA del backend Spring Boot.

## 🚀 Características

- **Angular 18+** con standalone components
- **Signals** para manejo de estado reactivo
- **Angular Material** para UI components
- **Chart.js** para visualización de datos
- **TypeScript** con tipado estricto
- **Responsive design** para móviles y desktop

## 📱 Funcionalidades

### Chat con IA
- Interface conversacional para consultar el catálogo
- Respuestas estructuradas con libros y autores
- Ejemplos de preguntas predefinidos
- Estados de loading y manejo de errores

### Reportes y Gráficos
- Generación de reportes dinámicos con prompts
- Gráfico de barras: libros por autor
- Gráfico de torta: distribución por géneros
- Métricas en tiempo real (total libros, autores, promedios)
- Tabla detallada por autor

## 🛠️ Tecnologías Utilizadas

- **Angular 18.2.0**
- **Angular Material 21.2.2**
- **Chart.js 4.4.7**
- **TypeScript 5.6.3**
- **RxJS** para programación reactiva
- **SCSS** para estilos avanzados

## 📦 Instalación y Ejecución

### Prerrequisitos
- Node.js 18+ 
- npm 9+
- Angular CLI 18+

### Instalación
```bash
# Instalar dependencias
npm install

# Instalar Angular CLI globalmente (si no está instalado)
npm install -g @angular/cli
```

### Desarrollo
```bash
# Ejecutar servidor de desarrollo
ng serve

# Abrir automáticamente en el navegador
ng serve --open

# Ejecutar en puerto específico
ng serve --port 4201
```

### Compilación
```bash
# Build para producción
ng build

# Build con optimizaciones
ng build --configuration production
```

## 🔗 Endpoints Consumidos

### Chat IA
```typescript
POST http://localhost:8084/api/ai/chat
Content-Type: application/json

{
  "message": "¿Qué libros de terror tenemos?"
}
```

### Reportes
```typescript
POST http://localhost:8084/api/ai/report
Content-Type: application/json

{
  "message": "¿Cuántos libros tiene cada autor?"
}
```

## 🏗️ Estructura del Proyecto

```
src/
├── app/
│   ├── components/
│   │   ├── chat/           # Componente de chat
│   │   └── reports/        # Componente de reportes
│   ├── services/
│   │   └── ai.service.ts   # Servicio para API calls
│   ├── models/
│   │   └── ai.models.ts    # Interfaces TypeScript
│   ├── app.ts              # Componente principal
│   ├── app.config.ts       # Configuración de la app
│   └── app.routes.ts       # Rutas de navegación
├── styles.scss             # Estilos globales
├── index.html              # HTML principal
└── main.ts                 # Bootstrap de la aplicación
```

## 🎨 Componentes Principales

### ChatComponent
- **Signals**: `message`, `response`, `loading`, `error`
- **Funciones**: `sendMessage()`, `setExampleMessage()`, `clearResponse()`
- **UI**: Input con validación, ejemplos clickeables, cards de resultados

### ReportsComponent
- **Signals**: `prompt`, `reportResponse`, `loading`, `error`
- **Computed**: `averageBooksPerAuthor()`
- **Gráficos**: Chart.js con barras y torta
- **UI**: Tabs, métricas, tabla detallada

### AiService
- **Métodos**: `chat()`, `generateReport()`
- **Manejo de errores** con RxJS operators
- **Tipado** completo con interfaces

## 🔧 Configuración

### CORS
El backend debe tener configurado CORS para permitir requests desde `http://localhost:4200`:

```java
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:4200")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*");
    }
}
```

### Variables de Entorno
```typescript
// src/environments/environment.ts
export const environment = {
  production: false,
  apiUrl: 'http://localhost:8084/api'
};
```

## 📱 Responsive Design

- **Desktop**: Layout completo con sidebar y gráficos grandes
- **Tablet**: Adaptación de columnas y tamaños de gráficos  
- **Mobile**: Stack vertical, navegación colapsada, gráficos optimizados

## 🎯 Ejemplos de Uso

### Chat
```typescript
// Preguntas de ejemplo
"¿Qué libros de terror tenemos?"
"¿Cuánto cuesta Cien años de soledad?"
"¿Qué autores peruanos tenemos?"
```

### Reportes
```typescript
// Prompts de ejemplo
"¿Cuántos libros tiene cada autor?"
"Dame el reporte de libros de terror por autor"
"Muéstrame el catálogo completo agrupado por autor"
```

## 🚀 Despliegue

### Build de Producción
```bash
ng build --configuration production
```

### Servidor Web
Los archivos generados en `dist/frontend/` pueden servirse con cualquier servidor web:
- **Nginx**
- **Apache**
- **Netlify**
- **Vercel**
- **GitHub Pages**

## 🤝 Contribución

1. Fork del proyecto
2. Crear feature branch (`git checkout -b feature/nueva-funcionalidad`)
3. Commit cambios (`git commit -m 'Agregar nueva funcionalidad'`)
4. Push al branch (`git push origin feature/nueva-funcionalidad`)
5. Crear Pull Request

## 📄 Licencia

Este proyecto está bajo la Licencia MIT - ver el archivo [LICENSE](../LICENSE) para detalles.