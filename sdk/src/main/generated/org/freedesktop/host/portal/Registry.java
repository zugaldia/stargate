package org.freedesktop.host.portal;

import java.util.Map;
import org.freedesktop.dbus.annotations.DBusBoundProperty;
import org.freedesktop.dbus.annotations.DBusProperty.Access;
import org.freedesktop.dbus.interfaces.DBusInterface;
import org.freedesktop.dbus.types.UInt32;
import org.freedesktop.dbus.types.Variant;

/**
 * Auto-generated class.
 */
public interface Registry extends DBusInterface {

    void Register(String appId, Map<String, Variant<?>> options);

    @DBusBoundProperty(name = "version")
    UInt32 getVersion();

}
