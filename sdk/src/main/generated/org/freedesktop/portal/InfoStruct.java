package org.freedesktop.portal;

import java.util.List;
import java.util.Map;
import org.freedesktop.dbus.Struct;
import org.freedesktop.dbus.annotations.DBusMemberName;
import org.freedesktop.dbus.annotations.Position;

/**
 * Auto-generated class.
 */
public class InfoStruct extends Struct {
    @Position(0)
    private final List<Byte> path;
    @Position(1)
    private final Map<String, List<String>> apps;

    public InfoStruct(List<Byte> path, Map<String, List<String>> apps) {
      this.path = path;
      this.apps = apps;
    }

    @DBusMemberName("Path")
    public List<Byte> getPathFromBus() {
        return path;
    }

    public Map<String, List<String>> getApps() {
        return apps;
    }

}
