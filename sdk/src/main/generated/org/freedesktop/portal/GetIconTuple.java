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

    public void setIconV(A arg) {
        iconV = arg;
    }

    public A getIconV() {
        return iconV;
    }

    public void setIconFormat(B arg) {
        iconFormat = arg;
    }

    public B getIconFormat() {
        return iconFormat;
    }

    public void setIconSize(C arg) {
        iconSize = arg;
    }

    public C getIconSize() {
        return iconSize;
    }

}
