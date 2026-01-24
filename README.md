# Stargate

A Kotlin library that provides JVM applications with easy access to
[XDG Desktop Portals](https://flatpak.github.io/xdg-desktop-portal/docs) on Linux.

XDG Desktop Portals originated from the Flatpak project but have since become a widely adopted Linux desktop standard,
including Snaps. They are even used outside of sandboxes to provide a standardized API to common desktop features,
such as taking screenshots, recording, and accessing files, that work consistently across desktop environments, like
GNOME and KDE, and across display servers including X11 and Wayland.

# Getting Started

## Examples

# Supported Portals

| Portal                | Low Level | High Level | Example |
|-----------------------|-----------|------------|---------|
| Account               | ❌         | ❌          | ❌       |
| Background            | ✅         | ❌          | ❌       |
| Camera                | ❌         | ❌          | ❌       |
| Clipboard             | ❌         | ❌          | ❌       |
| Documents             | ❌         | ❌          | ❌       |
| Dynamic Launcher      | ❌         | ❌          | ❌       |
| Email                 | ❌         | ❌          | ❌       |
| File Chooser          | ✅         | ❌          | ❌       |
| File Transfer         | ❌         | ❌          | ❌       |
| Game Mode             | ❌         | ❌          | ❌       |
| Global Shortcuts      | ❌         | ❌          | ❌       |
| Inhibit               | ❌         | ❌          | ❌       |
| Input Capture         | ❌         | ❌          | ❌       |
| Location              | ❌         | ❌          | ❌       |
| Memory Monitor        | ❌         | ❌          | ❌       |
| Network Monitor       | ❌         | ❌          | ❌       |
| Notification          | ✅         | ❌          | ❌       |
| OpenURI               | ✅         | ❌          | ❌       |
| Power Profile Monitor | ❌         | ❌          | ❌       |
| Print                 | ❌         | ❌          | ❌       |
| Proxy Resolver        | ❌         | ❌          | ❌       |
| Realtime              | ❌         | ❌          | ❌       |
| Remote Desktop        | ✅         | ❌          | ❌       |
| Request               | ❌         | ❌          | ❌       |
| ScreenCast            | ❌         | ❌          | ❌       |
| Screenshot            | ❌         | ❌          | ❌       |
| Secret                | ✅         | ❌          | ❌       |
| Session               | ❌         | ❌          | ❌       |
| Settings              | ✅         | ❌          | ❌       |
| Trash                 | ❌         | ❌          | ❌       |
| Usb                   | ❌         | ❌          | ❌       |
| Wallpaper             | ❌         | ❌          | ❌       |
