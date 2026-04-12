package org.kde;

import java.util.List;
import org.freedesktop.dbus.Struct;
import org.freedesktop.dbus.annotations.Position;

/**
 * Auto-generated class.
 */
public class PropertyToolTipStructStruct extends Struct {
    @Position(0)
    private final int member0;
    @Position(1)
    private final int member1;
    @Position(2)
    private final List<Byte> member2;

    public PropertyToolTipStructStruct(int member0, int member1, List<Byte> member2) {
        this.member0 = member0;
        this.member1 = member1;
        this.member2 = member2;
    }

    public int getMember0() {
        return member0;
    }

    public int getMember1() {
        return member1;
    }

    public List<Byte> getMember2() {
        return member2;
    }

}
