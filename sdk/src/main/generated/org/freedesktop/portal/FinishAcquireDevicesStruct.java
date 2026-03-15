package org.freedesktop.portal;

import java.util.List;
import org.freedesktop.dbus.Struct;
import org.freedesktop.dbus.annotations.Position;

/**
 * Auto-generated class.
 */
public class FinishAcquireDevicesStruct extends Struct {
    @Position(0)
    private final List<FinishAcquireDevicesStructResultsStruct> results;
    @Position(1)
    private final boolean finished;

    public FinishAcquireDevicesStruct(List<FinishAcquireDevicesStructResultsStruct> results, boolean finished) {
      this.results = results;
      this.finished = finished;
    }

    public List<FinishAcquireDevicesStructResultsStruct> getResults() {
        return results;
    }

    public boolean getFinished() {
        return finished;
    }

}
