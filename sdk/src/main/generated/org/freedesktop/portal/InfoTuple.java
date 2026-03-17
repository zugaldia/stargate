package org.freedesktop.portal;

import org.freedesktop.dbus.Tuple;
import org.freedesktop.dbus.annotations.DBusMemberName;
import org.freedesktop.dbus.annotations.Position;

/**
 * Auto-generated class.
 */
public class InfoTuple<A, B> extends Tuple {
    @Position(0)
    private A path;
    @Position(1)
    private B apps;

    public InfoTuple(A path, B apps) {
      this.path = path;
      this.apps = apps;
    }

    @DBusMemberName("Path")
    public void setPathFromBus(A arg) {
        path = arg;
    }

    @DBusMemberName("Path")
    public A getPathFromBus() {
        return path;
    }

    public void setApps(B arg) {
        apps = arg;
    }

    public B getApps() {
        return apps;
    }

}
