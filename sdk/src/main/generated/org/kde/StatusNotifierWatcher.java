package org.kde;

import java.util.List;
import org.freedesktop.dbus.TypeRef;
import org.freedesktop.dbus.annotations.DBusBoundProperty;
import org.freedesktop.dbus.annotations.DBusProperty.Access;
import org.freedesktop.dbus.exceptions.DBusException;
import org.freedesktop.dbus.interfaces.DBusInterface;
import org.freedesktop.dbus.messages.DBusSignal;

/**
 * Auto-generated class.
 */
public interface StatusNotifierWatcher extends DBusInterface {

    void RegisterStatusNotifierItem(String service);

    void RegisterStatusNotifierHost(String service);

    @DBusBoundProperty(type = PropertyRegisteredStatusNotifierItemsType.class)
    List<String> getRegisteredStatusNotifierItems();

    @DBusBoundProperty
    boolean isIsStatusNotifierHostRegistered();

    @DBusBoundProperty
    int getProtocolVersion();

    public static interface PropertyRegisteredStatusNotifierItemsType extends TypeRef<List<String>> {

    }

    public static class StatusNotifierItemRegistered extends DBusSignal {

        private final String arg0;

        public StatusNotifierItemRegistered(String path, String arg0) throws DBusException {
            super(path, arg0);
            this.arg0 = arg0;
        }

        public String getArg0() {
            return arg0;
        }

    }

    public static class StatusNotifierItemUnregistered extends DBusSignal {

        private final String arg0;

        public StatusNotifierItemUnregistered(String path, String arg0) throws DBusException {
            super(path, arg0);
            this.arg0 = arg0;
        }

        public String getArg0() {
            return arg0;
        }

    }

    public static class StatusNotifierHostRegistered extends DBusSignal {

        public StatusNotifierHostRegistered(String path) throws DBusException {
            super(path);
        
        }

    }

    public static class StatusNotifierHostUnregistered extends DBusSignal {

        public StatusNotifierHostUnregistered(String path) throws DBusException {
            super(path);
        
        }

    }

}
