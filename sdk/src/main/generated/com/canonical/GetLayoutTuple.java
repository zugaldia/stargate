package com.canonical;

import org.freedesktop.dbus.Tuple;
import org.freedesktop.dbus.annotations.Position;

/**
 * Auto-generated class.
 */
public class GetLayoutTuple<A, B> extends Tuple {
    @Position(0)
    private A revision;
    @Position(1)
    private B layout;

    public GetLayoutTuple(A revision, B layout) {
        this.revision = revision;
        this.layout = layout;
    }

    public A getRevision() {
        return revision;
    }

    public void setRevision(A revision) {
        this.revision = revision;
    }

    public B getLayout() {
        return layout;
    }

    public void setLayout(B layout) {
        this.layout = layout;
    }

}
