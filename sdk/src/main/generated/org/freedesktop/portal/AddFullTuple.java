package org.freedesktop.portal;

import org.freedesktop.dbus.Tuple;
import org.freedesktop.dbus.annotations.Position;

/**
 * Auto-generated class.
 */
public class AddFullTuple<A, B> extends Tuple {
    @Position(0)
    private A docIds;
    @Position(1)
    private B extraOut;

    public AddFullTuple(A docIds, B extraOut) {
    this.docIds = docIds;
    this.extraOut = extraOut;
    }

}
