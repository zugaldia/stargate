package org.freedesktop.portal;

import java.util.List;
import java.util.Map;
import org.freedesktop.dbus.DBusPath;
import org.freedesktop.dbus.FileDescriptor;
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
public interface InputCapture extends DBusInterface {

    DBusPath CreateSession(String parentWindow, Map<String, Variant<?>> options);

    DBusPath GetZones(DBusPath sessionHandle, Map<String, Variant<?>> options);

    DBusPath SetPointerBarriers(DBusPath sessionHandle, Map<String, Variant<?>> options, List<Map<String, Variant<?>>> barriers, UInt32 zoneSet);

    void Enable(DBusPath sessionHandle, Map<String, Variant<?>> options);

    void Disable(DBusPath sessionHandle, Map<String, Variant<?>> options);

    void Release(DBusPath sessionHandle, Map<String, Variant<?>> options);

    FileDescriptor ConnectToEIS(DBusPath sessionHandle, Map<String, Variant<?>> options);

    @DBusBoundProperty
    UInt32 getSupportedCapabilities();

    @DBusBoundProperty(name = "version")
    UInt32 getVersion();

    public static class Disabled extends DBusSignal {

        private final DBusPath sessionHandle;
        private final Map<String, Variant<?>> options;

        public Disabled(String path, DBusPath sessionHandle, Map<String, Variant<?>> options) throws DBusException {
            super(path, sessionHandle, options);
                this.sessionHandle = sessionHandle;
                this.options = options;
        }

        public DBusPath getSessionHandle() {
                return sessionHandle;
        }

        public Map<String, Variant<?>> getOptions() {
                return options;
        }

    }

    public static class Activated extends DBusSignal {

        private final DBusPath sessionHandle;
        private final Map<String, Variant<?>> options;

        public Activated(String path, DBusPath sessionHandle, Map<String, Variant<?>> options) throws DBusException {
            super(path, sessionHandle, options);
                this.sessionHandle = sessionHandle;
                this.options = options;
        }

        public DBusPath getSessionHandle() {
                return sessionHandle;
        }

        public Map<String, Variant<?>> getOptions() {
                return options;
        }

    }

    public static class Deactivated extends DBusSignal {

        private final DBusPath sessionHandle;
        private final Map<String, Variant<?>> options;

        public Deactivated(String path, DBusPath sessionHandle, Map<String, Variant<?>> options) throws DBusException {
            super(path, sessionHandle, options);
                this.sessionHandle = sessionHandle;
                this.options = options;
        }

        public DBusPath getSessionHandle() {
                return sessionHandle;
        }

        public Map<String, Variant<?>> getOptions() {
                return options;
        }

    }

    public static class ZonesChanged extends DBusSignal {

        private final DBusPath sessionHandle;
        private final Map<String, Variant<?>> options;

        public ZonesChanged(String path, DBusPath sessionHandle, Map<String, Variant<?>> options) throws DBusException {
            super(path, sessionHandle, options);
                this.sessionHandle = sessionHandle;
                this.options = options;
        }

        public DBusPath getSessionHandle() {
                return sessionHandle;
        }

        public Map<String, Variant<?>> getOptions() {
                return options;
        }

    }

}
