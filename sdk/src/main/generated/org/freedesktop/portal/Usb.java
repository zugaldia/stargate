package org.freedesktop.portal;

import java.util.List;
import java.util.Map;
import org.freedesktop.dbus.DBusPath;
import org.freedesktop.dbus.annotations.DBusBoundProperty;
import org.freedesktop.dbus.annotations.DBusProperty.Access;
import org.freedesktop.dbus.interfaces.DBusInterface;
import org.freedesktop.dbus.types.UInt32;
import org.freedesktop.dbus.types.Variant;

/**
 * Auto-generated class.
 */
public interface Usb extends DBusInterface {

    DBusPath CreateSession(Map<String, Variant<?>> options);

    List<EnumerateDevicesDevicesStruct> EnumerateDevices(Map<String, Variant<?>> options);

    DBusPath AcquireDevices(String parentWindow, List<AcquireDevicesDevicesStruct> devices, Map<String, Variant<?>> options);

    FinishAcquireDevicesTuple<List<FinishAcquireDevicesResultsStruct>, Boolean> FinishAcquireDevices(DBusPath handle, Map<String, Variant<?>> options);

    void ReleaseDevices(List<String> devices, Map<String, Variant<?>> options);

    @DBusBoundProperty
    UInt32 getversion();

}
