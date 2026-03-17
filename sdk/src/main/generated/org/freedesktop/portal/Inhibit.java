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
public interface Inhibit extends DBusInterface {

    DBusPath Inhibit(String window, UInt32 flags, Map<String, Variant<?>> options);

    DBusPath CreateMonitor(String window, Map<String, Variant<?>> options);

    void QueryEndResponse(DBusPath sessionHandle);

    @DBusBoundProperty
    UInt32 getVersion();

    public static class StateChanged extends DBusSignal {

        private final DBusPath sessionHandle;
        private final Map<String, Variant<?>> state;

        public StateChanged(String path, DBusPath sessionHandle, Map<String, Variant<?>> state) throws DBusException {
            super(path, sessionHandle, state);
            this.sessionHandle = sessionHandle;
            this.state = state;
        }

        public DBusPath getSessionHandle() {
            return sessionHandle;
        }

        public Map<String, Variant<?>> getState() {
            return state;
        }

    }

}
