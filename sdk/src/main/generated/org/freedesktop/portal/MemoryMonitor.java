package org.freedesktop.portal;

import org.freedesktop.dbus.annotations.DBusBoundProperty;
import org.freedesktop.dbus.annotations.DBusProperty.Access;
import org.freedesktop.dbus.exceptions.DBusException;
import org.freedesktop.dbus.interfaces.DBusInterface;
import org.freedesktop.dbus.messages.DBusSignal;
import org.freedesktop.dbus.types.UInt32;

/**
 * Auto-generated class.
 */
public interface MemoryMonitor extends DBusInterface {

    @DBusBoundProperty
    UInt32 getVersion();

    public static class LowMemoryWarning extends DBusSignal {

        private final byte level;

        public LowMemoryWarning(String path, byte level) throws DBusException {
            super(path, level);
            this.level = level;
        }

        public byte getLevel() {
            return level;
        }

    }

}
