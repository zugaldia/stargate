# Contributing

Contributions are welcome. Please use the [GitHub issues page](https://github.com/zugaldia/stargate/issues)
to guide and track your work.

The use of coding agents during development is accepted. Regardless of how a contribution is created, the author
is ultimately responsible for its quality, correctness, and adherence to best practices and the project's
established patterns.

## Requirements

- **Java 25** — required to build and run the SDK and demo application

On Ubuntu:

```bash
sudo apt install git make openjdk-25-jdk flatpak flatpak-builder
sudo update-java-alternatives -s java-1.25.0-openjdk-amd64
```

On Fedora:

```bash
sudo dnf install git make java-25-openjdk-devel flatpak flatpak-builder
sudo alternatives --set java /usr/lib/jvm/java-25-openjdk/bin/java
```

## Clone the Repository

This project uses git submodules to vendor `xdg-desktop-portal` (`vendor/xdg-desktop-portal/`) and get the latest
XML spec definitions. To clone with all submodules included:

```bash
git clone --recurse-submodules https://github.com/zugaldia/stargate.git
```

If you've already cloned without `--recurse-submodules`, initialize them with:

```bash
git submodule update --init --recursive
```

## Build commands

All commands use the Makefile, which wraps Gradle:

```bash
make build        # Build all modules
make run          # Run the demo application
make clean        # Clean build artifacts
```

### Flatpak

To build and run the application as a Flatpak, install the Flathub repository user-wide:

```bash
flatpak remote-add --if-not-exists --user flathub https://dl.flathub.org/repo/flathub.flatpakrepo
```

Then:

```bash
make flatpak-build     # Build and install the Flatpak locally
make flatpak-run       # Run the installed Flatpak
make flatpak-bundle    # Create a distributable .flatpak bundle
```
