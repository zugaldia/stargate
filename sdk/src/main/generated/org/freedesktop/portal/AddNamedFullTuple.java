package org.freedesktop.portal;

import org.freedesktop.dbus.Tuple;
import org.freedesktop.dbus.annotations.Position;

/**
 * Auto-generated class.
 */
public class AddNamedFullTuple<A, B> extends Tuple {
    @Position(0)
    private A docId;
    @Position(1)
    private B extraOut;

    public AddNamedFullTuple(A docId, B extraOut) {
        this.docId = docId;
        this.extraOut = extraOut;
    }

    public A getDocId() {
        return docId;
    }

    public void setDocId(A docId) {
        this.docId = docId;
    }

    public B getExtraOut() {
        return extraOut;
    }

    public void setExtraOut(B extraOut) {
        this.extraOut = extraOut;
    }

}
