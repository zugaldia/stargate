package org.freedesktop.portal;

import java.util.Map;
import org.freedesktop.dbus.annotations.DBusBoundProperty;
import org.freedesktop.dbus.annotations.DBusProperty.Access;
import org.freedesktop.dbus.exceptions.DBusException;
import org.freedesktop.dbus.interfaces.DBusInterface;
import org.freedesktop.dbus.messages.DBusSignal;
import org.freedesktop.dbus.types.UInt32;
import org.freedesktop.dbus.types.Variant;

/**
 * Auto-generated class.
 */
public interface NetworkMonitor extends DBusInterface {

    boolean GetAvailable();

    boolean GetMetered();

    UInt32 GetConnectivity();

    Map<String, Variant<?>> GetStatus();

    boolean CanReach(String hostname, UInt32 port);

    @DBusBoundProperty
    UInt32 getVersion();

    public static class changed extends DBusSignal {

        public changed(String path) throws DBusException {
            super(path);
        
        }

    }

}
