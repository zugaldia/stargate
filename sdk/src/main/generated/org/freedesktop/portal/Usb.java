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
import org.freedesktop.dbus.types.Variant;

/**
 * Auto-generated class.
 */
public interface Usb extends DBusInterface {

    DBusPath CreateSession(Map<String, Variant<?>> options);

    List<EnumerateDevicesDevicesStruct> EnumerateDevices(Map<String, Variant<?>> options);

    DBusPath AcquireDevices(String parentWindow, List<AcquireDevicesDevicesStruct> devices, Map<String, Variant<?>> options);

    FinishAcquireDevicesStruct FinishAcquireDevices(DBusPath handle, Map<String, Variant<?>> options);

    void ReleaseDevices(List<String> devices, Map<String, Variant<?>> options);

    @DBusBoundProperty
    UInt32 getVersion();

    public static class DeviceEvents extends DBusSignal {

        private final DBusPath sessionHandle;
        private final List<DeviceEventsEventsStruct> events;

        public DeviceEvents(String path, DBusPath sessionHandle, List<DeviceEventsEventsStruct> events) throws DBusException {
            super(path, sessionHandle, events);
            this.sessionHandle = sessionHandle;
            this.events = events;
        }

        public DBusPath getSessionHandle() {
            return sessionHandle;
        }

        public List<DeviceEventsEventsStruct> getEvents() {
            return events;
        }

    }

}
