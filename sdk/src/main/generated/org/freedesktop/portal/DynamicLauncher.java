package org.freedesktop.portal;

import java.util.Map;
import org.freedesktop.dbus.DBusPath;
import org.freedesktop.dbus.annotations.DBusBoundProperty;
import org.freedesktop.dbus.annotations.DBusProperty.Access;
import org.freedesktop.dbus.interfaces.DBusInterface;
import org.freedesktop.dbus.types.UInt32;
import org.freedesktop.dbus.types.Variant;

/**
 * Auto-generated class.
 */
public interface DynamicLauncher extends DBusInterface {

    void Install(String token, String desktopFileId, String desktopEntry, Map<String, Variant<?>> options);

    DBusPath PrepareInstall(String parentWindow, String name, Variant<?> iconV, Map<String, Variant<?>> options);

    String RequestInstallToken(String name, Variant<?> iconV, Map<String, Variant<?>> options);

    void Uninstall(String desktopFileId, Map<String, Variant<?>> options);

    String GetDesktopEntry(String desktopFileId);

    GetIconTuple<Variant<?>, String, UInt32> GetIcon(String desktopFileId);

    void Launch(String desktopFileId, Map<String, Variant<?>> options);

    @DBusBoundProperty
    UInt32 getSupportedLauncherTypes();

    @DBusBoundProperty
    UInt32 getversion();

}
