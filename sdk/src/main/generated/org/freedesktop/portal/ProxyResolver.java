package org.freedesktop.portal;

import java.util.List;
import org.freedesktop.dbus.annotations.DBusBoundProperty;
import org.freedesktop.dbus.annotations.DBusProperty.Access;
import org.freedesktop.dbus.interfaces.DBusInterface;
import org.freedesktop.dbus.types.UInt32;

/**
 * Auto-generated class.
 */
public interface ProxyResolver extends DBusInterface {

    List<String> Lookup(String uri);

    @DBusBoundProperty
    UInt32 getVersion();

}
