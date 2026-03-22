package org.freedesktop.portal;

import org.freedesktop.dbus.FileDescriptor;
import org.freedesktop.dbus.annotations.DBusBoundProperty;
import org.freedesktop.dbus.annotations.DBusProperty.Access;
import org.freedesktop.dbus.interfaces.DBusInterface;
import org.freedesktop.dbus.types.UInt32;

/**
 * Auto-generated class.
 */
public interface GameMode extends DBusInterface {

    int QueryStatus(int pid);

    int RegisterGame(int pid);

    int UnregisterGame(int pid);

    int QueryStatusByPid(int target, int requester);

    int RegisterGameByPid(int target, int requester);

    int UnregisterGameByPid(int target, int requester);

    int QueryStatusByPIDFd(FileDescriptor target, FileDescriptor requester);

    int RegisterGameByPIDFd(FileDescriptor target, FileDescriptor requester);

    int UnregisterGameByPIDFd(FileDescriptor target, FileDescriptor requester);

    @DBusBoundProperty
    boolean isActive();

    @DBusBoundProperty(name = "version")
    UInt32 getVersion();

}
