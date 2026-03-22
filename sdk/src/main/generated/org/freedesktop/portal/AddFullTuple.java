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

    public A getDocIds() {
        return docIds;
    }

    public void setDocIds(A docIds) {
        this.docIds = docIds;
    }

    public B getExtraOut() {
        return extraOut;
    }

    public void setExtraOut(B extraOut) {
        this.extraOut = extraOut;
    }

}
