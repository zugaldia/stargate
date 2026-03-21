package org.freedesktop.portal;

import org.freedesktop.dbus.Tuple;
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

}
