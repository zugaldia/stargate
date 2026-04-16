package org.kde;

import java.util.List;
import org.freedesktop.dbus.Struct;
import org.freedesktop.dbus.annotations.Position;

/**
 * Auto-generated class.
 */
public class PropertyToolTipStruct extends Struct {
    @Position(0)
    private final String member0;
    @Position(1)
    private final List<PropertyToolTipStructStruct> member1;
    @Position(2)
    private final String member2;
    @Position(3)
    private final String member3;

    public PropertyToolTipStruct(String member0, List<PropertyToolTipStructStruct> member1, String member2, String member3) {
        this.member0 = member0;
        this.member1 = member1;
        this.member2 = member2;
        this.member3 = member3;
    }

    public String getMember0() {
        return member0;
    }

    public List<PropertyToolTipStructStruct> getMember1() {
        return member1;
    }

    public String getMember2() {
        return member2;
    }

    public String getMember3() {
        return member3;
    }

}
