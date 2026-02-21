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
public interface Session extends DBusInterface {

    void Close();

    @DBusBoundProperty
    UInt32 getversion();

    public static class Closed extends DBusSignal {

        private final Map<String, Variant<?>> details;

        public Closed(String path, Map<String, Variant<?>> details) throws DBusException {
                super(path, details);        this.details = details;
        }

        public Map<String, Variant<?>> getDetails() {
            return details;
        }

    }

}
