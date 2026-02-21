package org.freedesktop.portal;

import java.util.List;
import java.util.Map;
import org.freedesktop.dbus.annotations.DBusBoundProperty;
import org.freedesktop.dbus.annotations.DBusProperty.Access;
import org.freedesktop.dbus.exceptions.DBusException;
import org.freedesktop.dbus.interfaces.DBusInterface;
import org.freedesktop.dbus.messages.DBusSignal;
import org.freedesktop.dbus.types.UInt32;
import org.freedesktop.dbus.types.Variant;

/**
 * Auto-generated class.
 */
public interface Settings extends DBusInterface {

    Map<String, Map<String, Variant<?>>> ReadAll(List<String> namespaces);

    Variant<?> Read(String namespace, String key);

    Variant<?> ReadOne(String namespace, String key);

    @DBusBoundProperty
    UInt32 getversion();

    public static class SettingChanged extends DBusSignal {

        private final String namespace;
        private final String key;
        private final Variant<?> value;

        public SettingChanged(String path, String namespace, String key, Variant<?> value) throws DBusException {
                super(path, namespace, key, value);        this.namespace = namespace;
                this.key = key;
                this.value = value;
        }

        public String getNamespace() {
            return namespace;
        }

        public String getKey() {
            return key;
        }

        public Variant<?> getValue() {
            return value;
        }

    }

}
