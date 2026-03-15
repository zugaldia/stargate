package org.freedesktop.portal;

import java.util.Map;
import org.freedesktop.dbus.Struct;
import org.freedesktop.dbus.annotations.Position;
import org.freedesktop.dbus.types.Variant;

/**
 * Auto-generated class.
 */
public class AddNamedFullStruct extends Struct {
    @Position(0)
    private final String docId;
    @Position(1)
    private final Map<String, Variant<?>> extraOut;

    public AddNamedFullStruct(String docId, Map<String, Variant<?>> extraOut) {
      this.docId = docId;
      this.extraOut = extraOut;
    }

    public String getDocId() {
        return docId;
    }

    public Map<String, Variant<?>> getExtraOut() {
        return extraOut;
    }

}
