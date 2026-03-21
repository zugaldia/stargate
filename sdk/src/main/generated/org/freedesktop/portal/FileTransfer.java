package org.freedesktop.portal;

import java.util.List;
import java.util.Map;
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
public interface FileTransfer extends DBusInterface {

    String StartTransfer(Map<String, Variant<?>> options);

    void AddFiles(String key, List<FileDescriptor> fds, Map<String, Variant<?>> options);

    List<String> RetrieveFiles(String key, Map<String, Variant<?>> options);

    void StopTransfer(String key);

    @DBusBoundProperty
    UInt32 getVersion();

    public static class TransferClosed extends DBusSignal {

        private final String key;

        public TransferClosed(String path, String key) throws DBusException {
            super(path, key);
            this.key = key;
        }

        public String getKey() {
            return key;
        }

    }

}
