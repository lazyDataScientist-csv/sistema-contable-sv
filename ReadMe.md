# 📊 Sistema Contable & Auditoría — README.md

**Sistema Contable & Auditoría** es una plataforma web financiera diseñada para automatizar el ciclo contable completo bajo la normativa mercantil, tributaria y laboral de El Salvador. Integra un backend en **Java 17 (Spring Boot)**, base de datos relacional **PostgreSQL en la nube (Supabase)** y una interfaz reactiva en **Bootstrap 5.3** para gestionar el **Libro Diario**, **Libro Mayor (Cuentas T)**, **Estados Financieros**, **Calculadora de Planilla (ISSS/AFP/Renta)** y **Catálogo de Cuentas**.

---

## 1. 🔑 Manual de Accesos y Credenciales

> **⚡ NOTA PARA EL EVALUADOR:**  
> **No es necesario instalar ningún programa ni motor de base de datos local para evaluar este sistema.** Tanto el servidor web como la base de datos en Supabase están desplegados y activos en la nube las 24 horas.

* 🌐 **Enlace a la Aplicación en la Nube:** `https://sistema-contable-sv.onrender.com` *AVISO : LA PAGINA TARDARA PROBABLEMENTE DE 40S A 90S EN CARGAR DEBIDO AL HOST GRATIUTO USADO PERO FUNCIONA CORRECTAMENTE.*
* 💻 **Enlace en Entorno Local:** `http://localhost:8080/`
* 🎥 **Video Explicativo del Sistema:** `https://drive.google.com/file/d/1VP8_kw3xoWYgkD4ay7GN24ZZKLiMaeOD/view?usp=drive_link`

### Credenciales de Acceso Rápido para Evaluación
En la pantalla de bienvenida puedes presionar el botón **"Acceso Rápido Evaluador (1 Clic)"** o ingresar manualmente con los siguientes datos:
* **Perfil Activo:** `Docente / Evaluador (Demo)`
* **Usuario:** `evaluador` (`@evaluador`)
* **Contraseña:** `123456`
* **Cambio de Cuenta / Cierre de Sesión:** En la esquina superior derecha se encuentra el botón **`Cambiar Usuario / Salir`** para cerrar la sesión actual o ingresar con otro usuario[cite: 3].

---

## 2. 👥 Tabla de Roles

### A. Roles de Usuario dentro del Sistema (Multi-Tenancy)

| Rol del Sistema | Identificador / Cuenta | Permisos y Alcance dentro de la Aplicación |
| :--- | :--- | :--- |
| **Docente / Evaluador (Demo)** | `@evaluador`[cite: 3] | Acceso inmediato en 1 clic. Permite registrar partidas en el Libro Diario, usar la Calculadora de IVA (13%), auditar Cuentas T, consultar Estados Financieros, calcular planillas laborales, filtrar por **`Vista / Historial`** y exportar el reporte consolidado con **`Descargar Respaldo (.HTML)`**[cite: 3]. |
| **Contador / Usuario Registrado** | `@usuario_propio` | Espacio contable privado y aislado mediante `usuarioId` con contraseña encriptada en `SHA-256`. Permite registrar asientos, ejecutar anulaciones en el mismo día (`ejecutarReversionMismoDia`) y generar pólizas de ajuste (`prepararReclasificacion`). |
| **Administrador de Base de Datos (Cloud)** | `postgres` (Supabase) | Administración del esquema relacional en la nube (`usuarios`, `catalogo_cuentas`, `transacciones`, `detalles_transaccion`) y conexión mediante pooler transaccional con Spring Boot[cite: 3]. |

### B. Tabla de Roles del Equipo de Desarrollo

| Integrante | Rol en el Proyecto | Responsabilidades y Módulos Desarrollados |
| :--- | :--- | :--- |
| **Joseph** | Desarrollador Full-Stack & Arquitecto de Base de Datos | Diseño e implementación de la API REST en Java Spring Boot[cite: 3], modelado de entidades JPA y persistencia en PostgreSQL (Supabase), interfaz Neo-Brutalista en Bootstrap 5.3, motor de Estados Financieros, calculadora de planilla salvadoreña y módulo de auditoría para reversión (`ejecutarReversionMismoDia`)[cite: 2] y reclasificación de partidas (`prepararReclasificacion`)[cite: 1]. |

---

## 3. 🛠️ Manual de Instalación

### Opción A: Ejecución Directa en la Nube (Recomendada)
Para probar y calificar el sistema **no se requiere instalación**. Basta con abrir el enlace web proporcionado en la sección de accesos desde cualquier navegador.

### Opción B: Instalación y Ejecución en Entorno Local
Si deseas compilar y ejecutar el proyecto localmente, **no necesitas instalar PostgreSQL en tu computadora**, ya que el sistema se conecta automáticamente al servidor en la nube de Supabase.

#### Requisitos previos:
* **Java Development Kit (JDK):** Versión 21 o superior.
* **IDE o Gestor de Construcción:** Apache NetBeans, IntelliJ IDEA, VS Code o Apache Maven desde terminal.
* **Conexión a Internet:** Requerida para sincronizar los datos con Supabase.

#### Pasos de instalación paso a paso:

1. **Clonar el repositorio desde GitHub:**
   ```bash
   git clone https://github.com/lazyDataScientist-csv/sistema-contable-sv
   cd contable
