package org.freedesktop.portal;

import java.util.Map;
import org.freedesktop.dbus.Struct;
import org.freedesktop.dbus.annotations.Position;
import org.freedesktop.dbus.types.Variant;

/**
 * Auto-generated class.
 */
public class DeviceEventsEventsStruct extends Struct {
    @Position(0)
    private final String member0;
    @Position(1)
    private final String member1;
    @Position(2)
    private final Map<String, Variant<?>> member2;

    public DeviceEventsEventsStruct(String member0, String member1, Map<String, Variant<?>> member2) {
      this.member0 = member0;
      this.member1 = member1;
      this.member2 = member2;
    }

    public String getMember0() {
        return member0;
    }

    public String getMember1() {
        return member1;
    }

    public Map<String, Variant<?>> getMember2() {
        return member2;
    }

}
