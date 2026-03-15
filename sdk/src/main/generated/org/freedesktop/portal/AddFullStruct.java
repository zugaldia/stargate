package org.freedesktop.portal;

import java.util.List;
import java.util.Map;
import org.freedesktop.dbus.Struct;
import org.freedesktop.dbus.annotations.Position;
import org.freedesktop.dbus.types.Variant;

/**
 * Auto-generated class.
 */
public class AddFullStruct extends Struct {
    @Position(0)
    private final List<String> docIds;
    @Position(1)
    private final Map<String, Variant<?>> extraOut;

    public AddFullStruct(List<String> docIds, Map<String, Variant<?>> extraOut) {
      this.docIds = docIds;
      this.extraOut = extraOut;
    }

    public List<String> getDocIds() {
        return docIds;
    }

    public Map<String, Variant<?>> getExtraOut() {
        return extraOut;
    }

}
