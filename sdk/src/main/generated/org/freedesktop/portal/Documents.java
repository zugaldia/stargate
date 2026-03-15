package org.freedesktop.portal;

import java.util.List;
import java.util.Map;
import org.freedesktop.dbus.FileDescriptor;
import org.freedesktop.dbus.annotations.DBusBoundProperty;
import org.freedesktop.dbus.annotations.DBusProperty.Access;
import org.freedesktop.dbus.interfaces.DBusInterface;
import org.freedesktop.dbus.types.UInt32;
import org.freedesktop.dbus.types.Variant;

/**
 * Auto-generated class.
 */
public interface Documents extends DBusInterface {

    @DBusBoundProperty
    UInt32 getVersion();

    List<Byte> GetMountPoint();

    String Add(FileDescriptor oPathFd, boolean reuseExisting, boolean persistent);

    String AddNamed(FileDescriptor oPathParentFd, List<Byte> filename, boolean reuseExisting, boolean persistent);

    AddFullTuple<List<String>, Map<String, Variant<?>>> AddFull(List<FileDescriptor> oPathFds, UInt32 flags, String appId, List<String> permissions);

    AddNamedFullTuple<String, Map<String, Variant<?>>> AddNamedFull(FileDescriptor oPathFd, List<Byte> filename, UInt32 flags, String appId, List<String> permissions);

    void GrantPermissions(String docId, String appId, List<String> permissions);

    void RevokePermissions(String docId, String appId, List<String> permissions);

    void Delete(String docId);

    String Lookup(List<Byte> filename);

    InfoTuple<List<Byte>, Map<String, List<String>>> Info(String docId);

    Map<String, List<Byte>> List(String appId);

    Map<String, List<Byte>> GetHostPaths(List<String> docIds);

}
