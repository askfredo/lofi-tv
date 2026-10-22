# lofi cafe TV

App para **Android TV / Google TV** inspirada en [lofi.cafe](https://www.lofi.cafe/): radios lofi en vivo con fondos animados en pixel art, controlada con el control remoto.

> Proyecto personal no oficial, sin relación con lofi.cafe. Las radios son transmisiones en vivo de YouTube que se reproducen con el reproductor oficial embebido de YouTube, y los GIFs se cargan desde el servidor de lofi.cafe.

## Controles

| Botón | Acción |
|---|---|
| ▲ ▼ / CH+ CH− | Cambiar de radio (con efecto de estática) |
| ◀ ▶ | Cambiar el GIF de fondo |
| OK / Play-Pausa | Pausar o reanudar y ver el nombre de la radio |
| Atrás | Salir |

Incluye 10 radios (Lofi Girl, Chillhop, Coffee Shop Radio, ChillSynth FM, Space Ambient y otras) y 53 fondos. Recuerda la última radio y el último fondo, y muestra un reloj. Al salir de la app la música se detiene.

## Instalar en la TV

1. Descarga `lofi-cafe-tv.apk` desde [Releases](../../releases/latest).
2. Instálalo de alguna de estas formas:
   - **Con la app Downloader** (en Play Store de la TV): abre el link del APK y acepta la instalación. Hay que permitir "Fuentes desconocidas" para Downloader cuando lo pida.
   - **Con ADB** desde una computadora en la misma red (Depuración USB activada en la TV):
     ```
     adb connect <ip-de-la-tv>:5555
     adb install lofi-cafe-tv.apk
     ```
3. Aparece como **lofi cafe** en la lista de apps.

Requiere Android 8.0 o superior y que la TV tenga Android System WebView actualizado.

## Compilar

JDK 17 y Android SDK 34:

```
./gradlew assembleRelease
```

El APK queda en `app/build/outputs/apk/release/app-release.apk`.

## Cómo funciona

La app es un `WebView` a pantalla completa que carga `app/src/main/assets/index.html`. Esa página usa la [YouTube IFrame API](https://developers.google.com/youtube/iframe_api_reference) para el audio (el video queda oculto detrás del GIF) y muestra los GIFs de `https://media.lofi.cafe/v1/gifs/`. Para cambiar las radios o los fondos, edita las listas `STATIONS` y `GIFS` en ese archivo.

## Licencia

MIT (solo el código de este repo; las radios y los GIFs pertenecen a sus autores).
