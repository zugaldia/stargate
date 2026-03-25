package org.freedesktop.portal;

import org.freedesktop.dbus.Tuple;
import org.freedesktop.dbus.annotations.Position;

/**
 * Auto-generated class.
 */
public class GetIconTuple<A, B, C> extends Tuple {
    @Position(0)
    private A iconV;
    @Position(1)
    private B iconFormat;
    @Position(2)
    private C iconSize;

    public GetIconTuple(A iconV, B iconFormat, C iconSize) {
        this.iconV = iconV;
        this.iconFormat = iconFormat;
        this.iconSize = iconSize;
    }

    public A getIconV() {
        return iconV;
    }

    public void setIconV(A iconV) {
        this.iconV = iconV;
    }

    public B getIconFormat() {
        return iconFormat;
    }

    public void setIconFormat(B iconFormat) {
        this.iconFormat = iconFormat;
    }

    public C getIconSize() {
        return iconSize;
    }

    public void setIconSize(C iconSize) {
        this.iconSize = iconSize;
    }

}
