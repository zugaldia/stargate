package org.freedesktop.portal;

import org.freedesktop.dbus.Tuple;
import org.freedesktop.dbus.annotations.Position;

/**
 * Auto-generated class.
 */
public class FinishAcquireDevicesTuple<A, B> extends Tuple {
    @Position(0)
    private A results;
    @Position(1)
    private B finished;

    public FinishAcquireDevicesTuple(A results, B finished) {
    this.results = results;
    this.finished = finished;
    }

}
