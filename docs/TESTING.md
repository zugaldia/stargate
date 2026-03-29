# Testing

## Global Shortcuts

The Global Shortcuts portal persists shortcut registrations in `dconf`. To reset state between test runs:

```bash
# List current shortcuts for the portal
dconf dump /org/gnome/settings-daemon/global-shortcuts/

# Reset shortcuts registered by Stargate (the trailing slash is required)
dconf reset -f /org/gnome/settings-daemon/global-shortcuts/com.zugaldia.Stargate/

# Restart the portal service to pick up changes
systemctl --user restart xdg-desktop-portal.service
```

When running the application as a Flatpak, prepend `flatpak-spawn --host` to each command, for example:

```bash
flatpak-spawn --host dconf dump /org/gnome/settings-daemon/global-shortcuts/
flatpak-spawn --host dconf reset -f /org/gnome/settings-daemon/global-shortcuts/com.zugaldia.Stargate/
flatpak-spawn --host systemctl --user restart xdg-desktop-portal.service
```

Replace `com.zugaldia.Stargate` with your custom application ID.
