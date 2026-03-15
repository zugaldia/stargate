package org.freedesktop.portal;

import org.freedesktop.dbus.annotations.DBusBoundProperty;
import org.freedesktop.dbus.annotations.DBusProperty.Access;
import org.freedesktop.dbus.interfaces.DBusInterface;
import org.freedesktop.dbus.types.UInt32;
import org.freedesktop.dbus.types.UInt64;

/**
 * Auto-generated class.
 */
public interface Realtime extends DBusInterface {

    void MakeThreadRealtimeWithPID(UInt64 process, UInt64 thread, UInt32 priority);

    void MakeThreadHighPriorityWithPID(UInt64 process, UInt64 thread, int priority);

    @DBusBoundProperty
    int getMaxRealtimePriority();

    @DBusBoundProperty
    int getMinNiceLevel();

    @DBusBoundProperty
    long getRTTimeUSecMax();

    @DBusBoundProperty
    UInt32 getVersion();

}
