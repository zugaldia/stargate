[![Maven Central](https://img.shields.io/maven-central/v/com.github.zugaldia/stargate)](https://central.sonatype.com/artifact/com.github.zugaldia/stargate)
[![Build](https://github.com/zugaldia/stargate/actions/workflows/build.yml/badge.svg)](https://github.com/zugaldia/stargate/actions/workflows/build.yml)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
![JVM](https://img.shields.io/badge/JVM-25-blue)
![Platform](https://img.shields.io/badge/platform-Linux-lightgrey)

# Stargate

A Kotlin library that provides JVM applications with access to
[XDG Desktop Portals](https://flatpak.github.io/xdg-desktop-portal/docs) on Linux.

XDG Desktop Portals originated from the Flatpak project but have since become a widely adopted Linux desktop standard,
including Snaps. They are even used outside of sandboxes to provide a standardized API to common desktop features,
such as taking screenshots, recording, and accessing files, that work consistently across desktop environments, like
GNOME and KDE, and across display servers including X11 and Wayland.

# Getting Started

Add the dependency to your project (requires JDK 25+):

**Gradle (Kotlin DSL)**
```kotlin
dependencies {
    implementation("com.github.zugaldia:stargate:0.2.0")
}
```

Check out the [included app](./app/src/main/kotlin/com/zugaldia/stargate/app) for working examples.

# Supported Portals

Portal definitions are based on [XDG Desktop Portal](https://github.com/flatpak/xdg-desktop-portal) version [1.21.0](https://github.com/flatpak/xdg-desktop-portal/releases/tag/1.21.0).

| Portal                    | Low Level | High Level | Example |
|---------------------------|-----------|------------|---------|
| Registry (host)           | ✅         | ✅          | ✅       |
| Account                   | ✅         | ❌          | ❌       |
| Background                | ✅         | ❌          | ❌       |
| Camera                    | ✅         | ❌          | ❌       |
| Clipboard                 | ✅         | ❌          | ❌       |
| Documents                 | ✅         | ❌          | ❌       |
| Dynamic Launcher          | ✅         | ❌          | ❌       |
| Email                     | ✅         | ❌          | ❌       |
| File Chooser              | ✅         | ❌          | ❌       |
| File Transfer             | ✅         | ❌          | ❌       |
| Game Mode                 | ✅         | ❌          | ❌       |
| Global Shortcuts          | ✅         | ✅          | ✅       |
| Inhibit                   | ✅         | ❌          | ❌       |
| Input Capture             | ✅         | ❌          | ❌       |
| Location                  | ✅         | ❌          | ❌       |
| Memory Monitor            | ✅         | ❌          | ❌       |
| Network Monitor           | ✅         | ❌          | ❌       |
| Notification              | ✅         | ✅          | ✅       |
| OpenURI                   | ✅         | ✅          | ✅       |
| Power Profile Monitor     | ✅         | ❌          | ❌       |
| Print                     | ✅         | ❌          | ❌       |
| Proxy Resolver            | ✅         | ❌          | ❌       |
| Realtime                  | ✅         | ❌          | ❌       |
| Remote Desktop            | ✅         | ✅          | ✅       |
| Request                   | ✅         | ❌          | ❌       |
| ScreenCast                | ✅         | ❌          | ❌       |
| Screenshot                | ✅         | ❌          | ❌       |
| Secret                    | ✅         | ❌          | ❌       |
| Session                   | ✅         | ❌          | ❌       |
| Settings                  | ✅         | ✅          | ✅       |
| Trash                     | ✅         | ❌          | ❌       |
| Usb                       | ✅         | ❌          | ❌       |
| Wallpaper                 | ✅         | ❌          | ❌       |

## Portal Availability and Sandboxing

Both sandboxed (Flatpak and Snap) and unsandboxed applications can use portal interfaces.
However, unsandboxed applications running directly on the host may need additional setup for some portals to work.

XDG Desktop Portals rely on an Application ID for permission checks and to show the requesting app's name in dialogs.
Sandboxed applications get their identity automatically from the sandbox. Unsandboxed applications must register
their Application ID explicitly using the
[Registry portal](https://flatpak.github.io/xdg-desktop-portal/docs/doc-org.freedesktop.host.portal.Registry.html)
before making any portal calls. Without registration some portals like Global Shortcuts will not work and others may
have degraded functionality.

This library provides access to the Registry portal through `DesktopPortal.registry` and includes a `isSandboxed()`
utility function to detect if the application is running inside a sandbox. See the
[demo application](./app/src/main/kotlin/com/zugaldia/stargate/app/App.kt) for a working example.
This demo application can run both as an unsandboxed app (`make run`) and as a sandboxed Flatpak (`make flatpak-run`).

# Projects Using Stargate

- [Speed of Sound](https://github.com/zugaldia/speedofsound) — Voice typing for the Linux desktop.

If your application, project, or CLI depends on Stargate, feel free to open a PR to add it to this list.

# Built With

Stargate stands on the shoulders of these excellent open source projects:

- [dbus-java](https://github.com/hypfvieh/dbus-java) — A native Java implementation of the D-Bus protocol,
  enabling JVM applications to communicate over the Linux desktop bus.
- [Java-GI](https://github.com/jwharm/java-gi) — GTK/GNOME bindings for Java, enabling access to native libraries
  via the modern Panama framework, aka the Foreign Function & Memory API.
