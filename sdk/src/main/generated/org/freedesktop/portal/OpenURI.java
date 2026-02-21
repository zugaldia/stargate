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
public interface OpenURI extends DBusInterface {

    DBusPath OpenURI(String parentWindow, String uri, Map<String, Variant<?>> options);

    DBusPath OpenFile(String parentWindow, FileDescriptor fd, Map<String, Variant<?>> options);

    DBusPath OpenDirectory(String parentWindow, FileDescriptor fd, Map<String, Variant<?>> options);

    @DBusBoundProperty
    UInt32 getversion();

}
