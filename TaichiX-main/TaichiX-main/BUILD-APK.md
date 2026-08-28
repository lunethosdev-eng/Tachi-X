# TaichiX — compilación Android

Este ZIP contiene un proyecto fuente Android/Capacitor listo para compilar. No contiene un APK precompilado.

## Compilación local

Requisitos: Node.js 20+, JDK 21 completo (incluye `javac`), Android SDK con API 35 y Build Tools, y conexión a Internet para Gradle/NPM.

```bash
npm ci
npm run build
npx cap sync android
cd android
./gradlew assembleDebug
```

El APK se genera en:

```text
android/app/build/outputs/apk/debug/app-debug.apk
```

## Qué incluye

- Proyecto Android generado por Capacitor 7.
- `AndroidManifest.xml` con Internet y Foreground Service.
- `ServerNativePlugin.java` para gestionar procesos, descargas de Paper/PocketMine, consola y túnel.
- `TaichiXServerService.java` para mantener los procesos en primer plano.
- Frontend compilado dentro de `android/app/src/main/assets/public/`.
- Gradle Wrapper (`android/gradlew` + `android/gradle/wrapper/`).

## Runtime Java Android descargado en el primer uso

En el primer inicio de un servidor Java, `ServerNative.ensureJavaRuntime()` descarga `jre17-arm64.tar.xz` desde el release público de OpenJDK 17 para Android, lo descomprime en `files/runtime/java`, marca los binarios ejecutables y después `startServer()` ejecuta ese `bin/java` con Paper y los argumentos de RAM. Si ya existe, no vuelve a descargarlo.

El paquete publicado es para **aarch64 / arm64-v8a**. Las arquitecturas x86, x86_64 y arm32 requieren runtimes separados. El runtime se ejecuta desde `filesDir` privado de la aplicación y no desde almacenamiento externo, respetando las restricciones modernas de Android.

El runtime no va dentro del APK: la descarga posterior puede ocupar decenas de MB y no cuenta dentro del límite del APK de GitHub. El proyecto de runtime declara licencia GPL-2.0; conserva sus avisos y revisa las obligaciones de redistribución antes de una publicación comercial. Fuente: https://github.com/itsaky/openjdk-17-android

## Compatibilidad de Minecraft

Java 17 cubre Paper/Minecraft 1.17–1.20.4. Las versiones que exigen Java 21 necesitan un runtime Android Java 21 compatible; no se debe sustituir por un JRE Linux de Temurin. La base queda preparada para añadir el URL verificado de Java 21 cuando se seleccione ese runtime.

La descarga del servidor y del agente de túnel son posteriores a la instalación y no cuentan dentro del tamaño del APK.

## Tamaño

El ZIP de fuentes está optimizado excluyendo `node_modules`, caches y builds temporales. La variante está limitada a `arm64-v8a`, con `minifyEnabled true` en release y sin runtime empaquetado, para mantener el APK por debajo de 22 MB en GitHub Actions.
