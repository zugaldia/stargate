package com.canonical;

import java.util.List;
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
public interface Dbusmenu extends DBusInterface {

    @DBusBoundProperty
    UInt32 getVersion();

    @DBusBoundProperty
    String getStatus();

    GetLayoutTuple<UInt32, GetLayoutLayoutStruct> GetLayout(int parentId, int recursionDepth, List<String> propertyNames);

    List<GetGroupPropertiesPropertiesStruct> GetGroupProperties(List<Integer> ids, List<String> propertyNames);

    Variant<?> GetProperty(int id, String name);

    void Event(int id, String eventId, Variant<?> data, UInt32 timestamp);

    boolean AboutToShow(int id);

    public static class ItemsPropertiesUpdated extends DBusSignal {

        private final List<ItemsPropertiesUpdatedUpdatedPropsStruct> updatedProps;
        private final List<ItemsPropertiesUpdatedRemovedPropsStruct> removedProps;

        public ItemsPropertiesUpdated(String path, List<ItemsPropertiesUpdatedUpdatedPropsStruct> updatedProps, List<ItemsPropertiesUpdatedRemovedPropsStruct> removedProps) throws DBusException {
            super(path, updatedProps, removedProps);
            this.updatedProps = updatedProps;
            this.removedProps = removedProps;
        }

        public List<ItemsPropertiesUpdatedUpdatedPropsStruct> getUpdatedProps() {
            return updatedProps;
        }

        public List<ItemsPropertiesUpdatedRemovedPropsStruct> getRemovedProps() {
            return removedProps;
        }

    }

    public static class LayoutUpdated extends DBusSignal {

        private final UInt32 revision;
        private final int parent;

        public LayoutUpdated(String path, UInt32 revision, int parent) throws DBusException {
            super(path, revision, parent);
            this.revision = revision;
            this.parent = parent;
        }

        public UInt32 getRevision() {
            return revision;
        }

        public int getParent() {
            return parent;
        }

    }

    public static class ItemActivationRequested extends DBusSignal {

        private final int id;
        private final UInt32 timestamp;

        public ItemActivationRequested(String path, int id, UInt32 timestamp) throws DBusException {
            super(path, id, timestamp);
            this.id = id;
            this.timestamp = timestamp;
        }

        public int getId() {
            return id;
        }

        public UInt32 getTimestamp() {
            return timestamp;
        }

    }

}
