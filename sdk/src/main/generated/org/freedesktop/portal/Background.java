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
public interface Background extends DBusInterface {

    DBusPath RequestBackground(String parentWindow, Map<String, Variant<?>> options);

    void SetStatus(Map<String, Variant<?>> options);

    @DBusBoundProperty
    UInt32 getversion();

}
