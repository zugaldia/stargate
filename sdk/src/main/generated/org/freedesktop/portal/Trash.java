package org.freedesktop.portal;

import org.freedesktop.dbus.FileDescriptor;
import org.freedesktop.dbus.annotations.DBusBoundProperty;
import org.freedesktop.dbus.annotations.DBusProperty.Access;
import org.freedesktop.dbus.interfaces.DBusInterface;
import org.freedesktop.dbus.types.UInt32;

/**
 * Auto-generated class.
 */
public interface Trash extends DBusInterface {

    UInt32 TrashFile(FileDescriptor fd);

    @DBusBoundProperty
    UInt32 getVersion();

}
