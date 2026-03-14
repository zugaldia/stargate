package org.freedesktop.portal;

import java.util.Map;
import org.freedesktop.dbus.DBusPath;
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
public interface Location extends DBusInterface {

    DBusPath CreateSession(Map<String, Variant<?>> options);

    DBusPath Start(DBusPath sessionHandle, String parentWindow, Map<String, Variant<?>> options);

    @DBusBoundProperty
    UInt32 getversion();

    public static class LocationUpdated extends DBusSignal {

        private final DBusPath sessionHandle;
        private final Map<String, Variant<?>> location;

        public LocationUpdated(String path, DBusPath sessionHandle, Map<String, Variant<?>> location) throws DBusException {
                super(path, sessionHandle, location);        this.sessionHandle = sessionHandle;
                this.location = location;
        }

        public DBusPath getSessionHandle() {
            return sessionHandle;
        }

        public Map<String, Variant<?>> getLocation() {
            return location;
        }

    }

}
