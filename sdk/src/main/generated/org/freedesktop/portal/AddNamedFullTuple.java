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

    public void setDocId(A arg) {
        docId = arg;
    }

    public A getDocId() {
        return docId;
    }

    public void setExtraOut(B arg) {
        extraOut = arg;
    }

    public B getExtraOut() {
        return extraOut;
    }

}
