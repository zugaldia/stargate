package org.freedesktop.portal;

import java.util.Map;
import org.freedesktop.dbus.exceptions.DBusException;
import org.freedesktop.dbus.interfaces.DBusInterface;
import org.freedesktop.dbus.messages.DBusSignal;
import org.freedesktop.dbus.types.UInt32;
import org.freedesktop.dbus.types.Variant;

/**
 * Auto-generated class.
 */
public interface Request extends DBusInterface {

    void Close();

    public static class Response extends DBusSignal {

        private final UInt32 response;
        private final Map<String, Variant<?>> results;

        public Response(String path, UInt32 response, Map<String, Variant<?>> results) throws DBusException {
                super(path, response, results);        this.response = response;
                this.results = results;
        }

        public UInt32 getResponse() {
            return response;
        }

        public Map<String, Variant<?>> getResults() {
            return results;
        }

    }

}
