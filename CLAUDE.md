# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Stargate is a Kotlin JVM library and GTK4 GUI example application providing access to XDG Desktop Portals on Linux.
It uses D-Bus for portal communication and Java-GI for GTK bindings.

**Requirements**: JDK 25

## Build Commands

```bash
make build      # Build all modules
make check      # Run tests and Detekt linting
make clean      # Clean build artifacts
make run        # Run the GTK application
```

Or with Gradle directly:
```bash
./gradlew build
./gradlew :app:run
./gradlew detekt          # Run Kotlin linting only
```

## Code Generation

Generate D-Bus interface files from system introspection:
```bash
make generate-xml    # Introspect D-Bus to XML
make generate-java   # Generate Java interfaces from XML
```

## Architecture

### Module Structure

- **sdk/**: Core library with D-Bus portal wrappers
- **app/**: GTK4 reference application demonstrating SDK usage
- **generator/**: CLI tool for D-Bus interface code generation
- **buildSrc/**: Gradle convention plugin (Kotlin config, Detekt, JUnit)

### MVVM Pattern (app module)

```
Screen (View) ←signals→ ViewModel ←→ SDK Portal
     ↓                      ↓
  GTK4 UI              State data class
```

- Screens observe ViewModels through GTK signals
- ViewModels hold immutable state updated via `.copy()`
- All D-Bus operations are suspend functions using coroutines

### SDK Portal Structure

Each portal follows this pattern:
```
sdk/src/main/kotlin/com/zugaldia/stargate/sdk/{portal}/
├── {Portal}Portal.kt       # Main D-Bus wrapper (suspend functions, Result<T>)
├── {Portal}Constants.kt    # D-Bus interface/object paths
└── *.kt                    # Enums (DeviceType, InputState, etc.)
```

Entry point: `DesktopPortal` manages the D-Bus session bus connection and lazy-initializes portal instances.

### Key Patterns

- **Result<T>** for error handling on D-Bus calls
- **Flow<T>** for reactive subscriptions (Settings portal changes)
- **callbackFlow** converts D-Bus signals to Kotlin Flow
- **Lazy initialization** for portal instances
- **SupervisorJob** for coroutine scope management
- Avoid double bang (`!!`) usage

## Portal Implementation Status

The README.md is the source of truth for portal status.
BACKLOG.md lists known missing features that need to be implemented next, instead of TODO comments.

## Dependencies

Key libraries (see `gradle/libs.versions.toml`):
- **dbus-java**: D-Bus session bus communication
- **java-gi**: GTK4/Adwaita bindings
- **kotlinx-coroutines**: Async D-Bus operations
- **Detekt**: Kotlin static analysis
