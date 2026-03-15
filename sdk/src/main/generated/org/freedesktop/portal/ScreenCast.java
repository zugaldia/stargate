package org.freedesktop.portal;

import java.util.Map;
import org.freedesktop.dbus.DBusPath;
import org.freedesktop.dbus.FileDescriptor;
import org.freedesktop.dbus.annotations.DBusBoundProperty;
import org.freedesktop.dbus.annotations.DBusProperty.Access;
import org.freedesktop.dbus.interfaces.DBusInterface;
import org.freedesktop.dbus.types.UInt32;
import org.freedesktop.dbus.types.Variant;

/**
 * Auto-generated class.
 */
public interface ScreenCast extends DBusInterface {

    DBusPath CreateSession(Map<String, Variant<?>> options);

    DBusPath SelectSources(DBusPath sessionHandle, Map<String, Variant<?>> options);

    DBusPath Start(DBusPath sessionHandle, String parentWindow, Map<String, Variant<?>> options);

    FileDescriptor OpenPipeWireRemote(DBusPath sessionHandle, Map<String, Variant<?>> options);

    @DBusBoundProperty
    UInt32 getAvailableSourceTypes();

    @DBusBoundProperty
    UInt32 getAvailableCursorModes();

    @DBusBoundProperty
    UInt32 getVersion();

}
