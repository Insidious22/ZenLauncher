# ZenLauncher 🧘‍♂️📱 (WIP / experimento)

> **Una vaina hecho frankenstein a ver que sale si me gusta o no.**  
> Este repo es un laboratorio: acá pruebo ideas de UI/UX, rendimiento y sensaciones de “launcher minimalista” sin prometer nada serio… todavía. :contentReference[oaicite:1]{index=1}

**ZenLauncher** es un launcher para Android hecho en **Kotlin** (100%). :contentReference[oaicite:2]{index=2}  
Enfoque: **minimalismo**, fluidez y experimentar con interacciones “Zen” (animaciones, navegación, layout, etc). :contentReference[oaicite:3]{index=3}

---

## 🧠 Qué es este proyecto (en una línea)
Un **launcher minimalista** en Kotlin, construido como **experimento** para probar ideas sin miedo a romper cosas.

---

## ✅ Estado actual
🚧 **WIP** (Work in progress). No hay releases publicadas todavía. :contentReference[oaicite:4]{index=4}

Lo que significa:
- Puede cambiar sin aviso
- Puede tener features incompletas
- Puede tener decisiones raras porque es “prueba y error”

---

## 🎯 Objetivos del experimento
- **UI limpia y rápida** (sin saturación visual).
- **Acciones directas**: menos taps, más intención.
- **Sensación “Zen”**: transiciones, estados, micro-interacciones.
- Mantener el proyecto **simple de compilar y tocar**.

---

## 🧩 Tech stack (lo visible desde el repo)
- **Kotlin** :contentReference[oaicite:5]{index=5}  
- **Gradle (Kotlin DSL)** (`*.kts`) :contentReference[oaicite:6]{index=6}  
- Android app module (`/app`) :contentReference[oaicite:7]{index=7}

> Si luego quieres, puedo actualizar esta sección con librerías exactas (Compose, Room, etc.) cuando GitHub deje ver el contenido o me pegues tu `app/build.gradle.kts`.

---

## 🗂️ Estructura del repo
Estructura base (lo que se ve públicamente en la raíz): :contentReference[oaicite:8]{index=8}

- `.idea/` — configuración del IDE (Android Studio / IntelliJ)
- `app/` — módulo principal Android
- `gradle/` — wrapper / configuración de Gradle
- `build.gradle.kts` — build script raíz
- `settings.gradle.kts` — settings del proyecto
- `gradle.properties` — propiedades de compilación
- `LICENSE` — licencia MIT

---

## 🛠️ Requisitos
- **Android Studio** (recomendado: estable más reciente)
- **JDK 17** (o el que Android Studio te configure por defecto)
- Android SDK instalado desde el SDK Manager

---

## 🚀 Cómo compilar / correr

### Opción A: Android Studio (la fácil)
1. Clona el repo:
   ```bash
   git clone https://github.com/Insidious22/ZenLauncher.git
   cd ZenLauncher
2. Abre el proyecto en Android Studio

3. Deja que sincronice Gradle

4. Conecta un dispositivo o abre un emulador

5. Run y ya

simon esto se lo pedi a la IA

###Opción B: Gradle por consola
Desde la raíz del repo:


Compilar debug:
./gradlew assembleDebug

Instalar en un dispositivo conectado (ADB):
./gradlew installDebug


En Windows (PowerShell):

.\gradlew.bat assembleDebug


🏠 Ponerlo como launcher por defecto (Android)
Cuando lo instales:
Presiona Home
Android te preguntará qué launcher usar
Selecciona ZenLauncher
Marca “Siempre” si quieres dejarlo fijo
Si no te aparece:
Ajustes → Apps → Apps predeterminadas → App de inicio (Home) → ZenLauncher


🧪 Roadmap (ideas / cosas que podría meter)
Esto es intencionalmente flexible. Algunas ideas típicas para este tipo de launcher:

 Home minimal (apps esenciales / favoritos)

 Búsqueda rápida (apps, ajustes, contactos…)

 Categorías / tabs (si no arruinan el minimalismo)

 Gestos (swipe, long press, etc.)

 Personalización ligera (sin volverse un monstruo)

