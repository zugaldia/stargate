package org.freedesktop.portal;

import java.util.Map;
import org.freedesktop.dbus.DBusPath;
import org.freedesktop.dbus.FileDescriptor;
import org.freedesktop.dbus.annotations.DBusBoundProperty;
import org.freedesktop.dbus.annotations.DBusProperty.Access;
import org.freedesktop.dbus.interfaces.DBusInterface;
import org.freedesktop.dbus.types.UInt32;
import org.freedesktop.dbus.types.Variant;

/**
 * Auto-generated class.
 */
public interface RemoteDesktop extends DBusInterface {

    DBusPath CreateSession(Map<String, Variant<?>> options);

    DBusPath SelectDevices(DBusPath sessionHandle, Map<String, Variant<?>> options);

    DBusPath Start(DBusPath sessionHandle, String parentWindow, Map<String, Variant<?>> options);

    void NotifyPointerMotion(DBusPath sessionHandle, Map<String, Variant<?>> options, double dx, double dy);

    void NotifyPointerMotionAbsolute(DBusPath sessionHandle, Map<String, Variant<?>> options, UInt32 stream, double x, double y);

    void NotifyPointerButton(DBusPath sessionHandle, Map<String, Variant<?>> options, int button, UInt32 state);

    void NotifyPointerAxis(DBusPath sessionHandle, Map<String, Variant<?>> options, double dx, double dy);

    void NotifyPointerAxisDiscrete(DBusPath sessionHandle, Map<String, Variant<?>> options, UInt32 axis, int steps);

    void NotifyKeyboardKeycode(DBusPath sessionHandle, Map<String, Variant<?>> options, int keycode, UInt32 state);

    void NotifyKeyboardKeysym(DBusPath sessionHandle, Map<String, Variant<?>> options, int keysym, UInt32 state);

    void NotifyTouchDown(DBusPath sessionHandle, Map<String, Variant<?>> options, UInt32 stream, UInt32 slot, double x, double y);

    void NotifyTouchMotion(DBusPath sessionHandle, Map<String, Variant<?>> options, UInt32 stream, UInt32 slot, double x, double y);

    void NotifyTouchUp(DBusPath sessionHandle, Map<String, Variant<?>> options, UInt32 slot);

    FileDescriptor ConnectToEIS(DBusPath sessionHandle, Map<String, Variant<?>> options);

    @DBusBoundProperty
    UInt32 getAvailableDeviceTypes();

    @DBusBoundProperty
    UInt32 getversion();

}
