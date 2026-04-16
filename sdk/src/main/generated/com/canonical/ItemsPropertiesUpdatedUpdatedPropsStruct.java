package com.canonical;

import java.util.Map;
import org.freedesktop.dbus.Struct;
import org.freedesktop.dbus.annotations.Position;
import org.freedesktop.dbus.types.Variant;

/**
 * Auto-generated class.
 */
public class ItemsPropertiesUpdatedUpdatedPropsStruct extends Struct {
    @Position(0)
    private final int member0;
    @Position(1)
    private final Map<String, Variant<?>> member1;

    public ItemsPropertiesUpdatedUpdatedPropsStruct(int member0, Map<String, Variant<?>> member1) {
        this.member0 = member0;
        this.member1 = member1;
    }

    public int getMember0() {
        return member0;
    }

    public Map<String, Variant<?>> getMember1() {
        return member1;
    }

}
