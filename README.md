# DDD Example - Clean Architecture

Proyecto multi-módulo organizado siguiendo principios de Domain-Driven Design (DDD) y Clean Architecture.

## 📁 Estructura del Proyecto

```
ddd-example/                # Proyecto raíz (Java puro, sin Spring)
├── context/               # Módulo de dominio (Core/Domain)
│   └── Contiene la lógica de negocio pura (entidades, value objects, repositorios)
├── boot/                  # Módulo de aplicación Spring Boot
│   └── Punto de entrada de la aplicación con Spring Boot
└── backoffice/           # Módulo de backoffice
    └── Funcionalidades administrativas
```

## 🏗️ Arquitectura

- **Proyecto raíz**: Configuración Java pura sin dependencias de frameworks
- **context**: Módulo de dominio con lógica de negocio independiente del framework
- **boot**: Módulo de infraestructura con Spring Boot
- **backoffice**: Módulo adicional para funcionalidades de administración

## 🔨 Comandos

```bash
# Construir todo el proyecto
.\gradlew.bat build

# Ejecutar la aplicación boot
.\gradlew.bat :boot:bootRun

# Ejecutar tests
.\gradlew.bat test

# Limpiar el proyecto
.\gradlew.bat clean
```

## 📦 Dependencias entre módulos

- `boot` → depende de `context`
- `backoffice` → puede depender de `context`
- `context` → independiente (sin dependencias de otros módulos)

## ✨ Ventajas de esta estructura

1. **Separación de concerns**: El dominio está aislado de los frameworks
2. **Testabilidad**: Lógica de negocio fácil de testear sin infraestructura
3. **Mantenibilidad**: Cambios en infraestructura no afectan al dominio
4. **Escalabilidad**: Fácil agregar nuevos módulos o puntos de entrada

