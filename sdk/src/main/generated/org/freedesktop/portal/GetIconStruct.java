package org.freedesktop.portal;

import org.freedesktop.dbus.Struct;
import org.freedesktop.dbus.annotations.Position;
import org.freedesktop.dbus.types.UInt32;
import org.freedesktop.dbus.types.Variant;

/**
 * Auto-generated class.
 */
public class GetIconStruct extends Struct {
    @Position(0)
    private final Variant<?> iconV;
    @Position(1)
    private final String iconFormat;
    @Position(2)
    private final UInt32 iconSize;

    public GetIconStruct(Variant<?> iconV, String iconFormat, UInt32 iconSize) {
      this.iconV = iconV;
      this.iconFormat = iconFormat;
      this.iconSize = iconSize;
    }

    public Variant<?> getIconV() {
        return iconV;
    }

    public String getIconFormat() {
        return iconFormat;
    }

    public UInt32 getIconSize() {
        return iconSize;
    }

}
