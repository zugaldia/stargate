package com.canonical;

import java.util.List;
import org.freedesktop.dbus.Struct;
import org.freedesktop.dbus.annotations.Position;

/**
 * Auto-generated class.
 */
public class ItemsPropertiesUpdatedRemovedPropsStruct extends Struct {
    @Position(0)
    private final int member0;
    @Position(1)
    private final List<String> member1;

    public ItemsPropertiesUpdatedRemovedPropsStruct(int member0, List<String> member1) {
        this.member0 = member0;
        this.member1 = member1;
    }

    public int getMember0() {
        return member0;
    }

    public List<String> getMember1() {
        return member1;
    }

}
