package org.freedesktop.portal;

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
public interface Clipboard extends DBusInterface {

    void RequestClipboard(DBusPath sessionHandle, Map<String, Variant<?>> options);

    void SetSelection(DBusPath sessionHandle, Map<String, Variant<?>> options);

    FileDescriptor SelectionWrite(DBusPath sessionHandle, UInt32 serial);

    void SelectionWriteDone(DBusPath sessionHandle, UInt32 serial, boolean success);

    FileDescriptor SelectionRead(DBusPath sessionHandle, String mimeType);

    @DBusBoundProperty(name = "version")
    UInt32 getVersion();

    public static class SelectionOwnerChanged extends DBusSignal {

        private final DBusPath sessionHandle;
        private final Map<String, Variant<?>> options;

        public SelectionOwnerChanged(String path, DBusPath sessionHandle, Map<String, Variant<?>> options) throws DBusException {
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

    public static class SelectionTransfer extends DBusSignal {

        private final DBusPath sessionHandle;
        private final String mimeType;
        private final UInt32 serial;

        public SelectionTransfer(String path, DBusPath sessionHandle, String mimeType, UInt32 serial) throws DBusException {
            super(path, sessionHandle, mimeType, serial);
                this.sessionHandle = sessionHandle;
                this.mimeType = mimeType;
                this.serial = serial;
        }

        public DBusPath getSessionHandle() {
                return sessionHandle;
        }

        public String getMimeType() {
                return mimeType;
        }

        public UInt32 getSerialFromBus() {
                return serial;
        }

    }

}
