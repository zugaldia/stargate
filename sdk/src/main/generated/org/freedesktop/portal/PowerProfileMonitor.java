package org.freedesktop.portal;

import org.freedesktop.dbus.annotations.DBusBoundProperty;
import org.freedesktop.dbus.annotations.DBusProperty.Access;
import org.freedesktop.dbus.interfaces.DBusInterface;
import org.freedesktop.dbus.types.UInt32;

/**
 * Auto-generated class.
 */
public interface PowerProfileMonitor extends DBusInterface {

    @DBusBoundProperty
    UInt32 getversion();

}
