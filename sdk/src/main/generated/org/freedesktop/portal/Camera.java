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
public interface Camera extends DBusInterface {

    DBusPath AccessCamera(Map<String, Variant<?>> options);

    FileDescriptor OpenPipeWireRemote(Map<String, Variant<?>> options);

    @DBusBoundProperty
    boolean isIsCameraPresent();

    @DBusBoundProperty
    UInt32 getVersion();

}
