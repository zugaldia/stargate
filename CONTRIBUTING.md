# Contributing

These instructions assume a standard Ubuntu installation. The specific instructions might vary from distribution to distribution.

## Requirements

```bash
sudo apt install git make openjdk-25-jdk flatpak flatpak-builder
```

## Clone the Repository

This project uses git submodules to vendor `xdg-desktop-portal` (`vendor/xdg-desktop-portal/`) and get the latest XML spec definitions. To clone with all submodules included:

```bash
git clone --recurse-submodules https://github.com/zugaldia/stargate.git
```

If you've already cloned without `--recurse-submodules`, initialize them with:

```bash
git submodule update --init --recursive
```
