package org.freedesktop.portal;

import java.util.List;
import java.util.Map;
import org.freedesktop.dbus.DBusPath;
import org.freedesktop.dbus.annotations.DBusBoundProperty;
import org.freedesktop.dbus.annotations.DBusProperty.Access;
import org.freedesktop.dbus.exceptions.DBusException;
import org.freedesktop.dbus.interfaces.DBusInterface;
import org.freedesktop.dbus.messages.DBusSignal;
import org.freedesktop.dbus.types.UInt32;
import org.freedesktop.dbus.types.UInt64;
import org.freedesktop.dbus.types.Variant;

/**
 * Auto-generated class.
 */
public interface GlobalShortcuts extends DBusInterface {

    @DBusBoundProperty
    UInt32 getversion();

    DBusPath CreateSession(Map<String, Variant<?>> options);

    DBusPath BindShortcuts(DBusPath sessionHandle, List<BindShortcutsShortcutsStruct> shortcuts, String parentWindow, Map<String, Variant<?>> options);

    DBusPath ListShortcuts(DBusPath sessionHandle, Map<String, Variant<?>> options);

    void ConfigureShortcuts(DBusPath sessionHandle, String parentWindow, Map<String, Variant<?>> options);

    public static class Activated extends DBusSignal {

        private final DBusPath sessionHandle;
        private final String shortcutId;
        private final UInt64 timestamp;
        private final Map<String, Variant<?>> options;

        public Activated(String path, DBusPath sessionHandle, String shortcutId, UInt64 timestamp, Map<String, Variant<?>> options) throws DBusException {
                super(path, sessionHandle, shortcutId, timestamp, options);        this.sessionHandle = sessionHandle;
                this.shortcutId = shortcutId;
                this.timestamp = timestamp;
                this.options = options;
        }

        public DBusPath getSessionHandle() {
            return sessionHandle;
        }

        public String getShortcutId() {
            return shortcutId;
        }

        public UInt64 getTimestamp() {
            return timestamp;
        }

        public Map<String, Variant<?>> getOptions() {
            return options;
        }

    }

    public static class Deactivated extends DBusSignal {

        private final DBusPath sessionHandle;
        private final String shortcutId;
        private final UInt64 timestamp;
        private final Map<String, Variant<?>> options;

        public Deactivated(String path, DBusPath sessionHandle, String shortcutId, UInt64 timestamp, Map<String, Variant<?>> options) throws DBusException {
                super(path, sessionHandle, shortcutId, timestamp, options);        this.sessionHandle = sessionHandle;
                this.shortcutId = shortcutId;
                this.timestamp = timestamp;
                this.options = options;
        }

        public DBusPath getSessionHandle() {
            return sessionHandle;
        }

        public String getShortcutId() {
            return shortcutId;
        }

        public UInt64 getTimestamp() {
            return timestamp;
        }

        public Map<String, Variant<?>> getOptions() {
            return options;
        }

    }

}
