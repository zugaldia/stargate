# Contributing

These instructions assume a standard Ubuntu installation. The specific instructions might vary from distribution to distribution.

## Requirements

Install the following dependencies:

```bash
sudo apt install git make openjdk-25-jdk
```

## Clone the Repository

This project uses git submodules. To clone with all submodules included:

```bash
git clone --recurse-submodules https://github.com/zugaldia/stargate.git
```

If you've already cloned without `--recurse-submodules`, initialize them with:

```bash
git submodule update --init --recursive
```

## Desktop File

GNOME requires a `.desktop` file to be installed for portal features like notifications to work. Install it with:

```bash
make install-desktop
```
