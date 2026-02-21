package org.freedesktop.portal;

import java.util.List;
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
public interface Notification extends DBusInterface {

    void AddNotification(String id, Map<String, Variant<?>> notification);

    void RemoveNotification(String id);

    @DBusBoundProperty
    UInt32 getversion();

    public static class ActionInvoked extends DBusSignal {

        private final String id;
        private final String action;
        private final List<Variant<?>> parameter;

        public ActionInvoked(String path, String id, String action, List<Variant<?>> parameter) throws DBusException {
                super(path, id, action, parameter);        this.id = id;
                this.action = action;
                this.parameter = parameter;
        }

        public String getId() {
            return id;
        }

        public String getAction() {
            return action;
        }

        public List<Variant<?>> getParameter() {
            return parameter;
        }

    }

}
