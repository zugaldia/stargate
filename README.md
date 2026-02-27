[![Maven Central](https://img.shields.io/maven-central/v/com.github.zugaldia/stargate)](https://central.sonatype.com/artifact/com.github.zugaldia/stargate)
[![Build](https://github.com/zugaldia/stargate/actions/workflows/build.yml/badge.svg)](https://github.com/zugaldia/stargate/actions/workflows/build.yml)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
![JVM](https://img.shields.io/badge/JVM-22-blue)
![Platform](https://img.shields.io/badge/platform-Linux-lightgrey)

# Stargate

A Kotlin library that provides JVM applications with access to
[XDG Desktop Portals](https://flatpak.github.io/xdg-desktop-portal/docs) on Linux.

XDG Desktop Portals originated from the Flatpak project but have since become a widely adopted Linux desktop standard,
including Snaps. They are even used outside of sandboxes to provide a standardized API to common desktop features,
such as taking screenshots, recording, and accessing files, that work consistently across desktop environments, like
GNOME and KDE, and across display servers including X11 and Wayland.

# Getting Started

Add the dependency to your project (requires JDK 22+):

**Gradle (Kotlin DSL)**
```kotlin
dependencies {
    implementation("com.github.zugaldia:stargate:0.1.0")
}
```

Check out the [included app](./app/src/main/kotlin/com/zugaldia/stargate/app) for working examples.

# Supported Portals

| Portal                | Low Level | High Level | Example |
|-----------------------|-----------|------------|---------|
| Account               | ❌         | ❌          | ❌       |
| Background            | ❌         | ❌          | ❌       |
| Camera                | ❌         | ❌          | ❌       |
| Clipboard             | ❌         | ❌          | ❌       |
| Documents             | ❌         | ❌          | ❌       |
| Dynamic Launcher      | ❌         | ❌          | ❌       |
| Email                 | ❌         | ❌          | ❌       |
| File Chooser          | ❌         | ❌          | ❌       |
| File Transfer         | ❌         | ❌          | ❌       |
| Game Mode             | ❌         | ❌          | ❌       |
| Global Shortcuts      | ✅         | ✅          | ✅       |
| Inhibit               | ❌         | ❌          | ❌       |
| Input Capture         | ❌         | ❌          | ❌       |
| Location              | ❌         | ❌          | ❌       |
| Memory Monitor        | ❌         | ❌          | ❌       |
| Network Monitor       | ❌         | ❌          | ❌       |
| Notification          | ❌         | ❌          | ❌       |
| OpenURI               | ❌         | ❌          | ❌       |
| Power Profile Monitor | ❌         | ❌          | ❌       |
| Print                 | ❌         | ❌          | ❌       |
| Proxy Resolver        | ❌         | ❌          | ❌       |
| Realtime              | ❌         | ❌          | ❌       |
| Remote Desktop        | ✅         | ✅          | ✅       |
| Request               | ✅         | ❌          | ❌       |
| ScreenCast            | ❌         | ❌          | ❌       |
| Screenshot            | ❌         | ❌          | ❌       |
| Secret                | ❌         | ❌          | ❌       |
| Session               | ✅         | ❌          | ❌       |
| Settings              | ✅         | ✅          | ✅       |
| Trash                 | ❌         | ❌          | ❌       |
| Usb                   | ❌         | ❌          | ❌       |
| Wallpaper             | ❌         | ❌          | ❌       |

# Projects Using Stargate

- [Speed of Sound](https://github.com/zugaldia/speedofsound) — Voice typing for the Linux desktop.

If your application, project, or CLI depends on Stargate, feel free to open a PR to add it to this list.

# Built With

Stargate stands on the shoulders of these excellent open source projects:

- [dbus-java](https://github.com/hypfvieh/dbus-java) — A native Java implementation of the D-Bus protocol,
  enabling JVM applications to communicate over the Linux desktop bus.
