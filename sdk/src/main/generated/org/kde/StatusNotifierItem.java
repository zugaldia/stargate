package org.kde;

import java.util.List;
import org.freedesktop.dbus.DBusPath;
import org.freedesktop.dbus.TypeRef;
import org.freedesktop.dbus.annotations.DBusBoundProperty;
import org.freedesktop.dbus.annotations.DBusProperty.Access;
import org.freedesktop.dbus.exceptions.DBusException;
import org.freedesktop.dbus.interfaces.DBusInterface;
import org.freedesktop.dbus.messages.DBusSignal;

/**
 * Auto-generated class.
 */
public interface StatusNotifierItem extends DBusInterface {

    @DBusBoundProperty
    String getCategory();

    @DBusBoundProperty
    String getId();

    @DBusBoundProperty
    String getTitle();

    @DBusBoundProperty
    String getStatus();

    @DBusBoundProperty
    int getWindowId();

    @DBusBoundProperty
    String getIconThemePath();

    @DBusBoundProperty
    DBusPath getMenu();

    @DBusBoundProperty
    boolean isItemIsMenu();

    @DBusBoundProperty
    String getIconName();

    @DBusBoundProperty(type = PropertyIconPixmapType.class)
    List<PropertyIconPixmapStruct> getIconPixmap();

    @DBusBoundProperty
    String getOverlayIconName();

    @DBusBoundProperty(type = PropertyOverlayIconPixmapType.class)
    List<PropertyOverlayIconPixmapStruct> getOverlayIconPixmap();

    @DBusBoundProperty
    String getAttentionIconName();

    @DBusBoundProperty(type = PropertyAttentionIconPixmapType.class)
    List<PropertyAttentionIconPixmapStruct> getAttentionIconPixmap();

    @DBusBoundProperty
    String getAttentionMovieName();

    @DBusBoundProperty(type = PropertyToolTipStruct.class)
    PropertyToolTipStruct getToolTip();

    void ProvideXdgActivationToken(String token);

    void ContextMenu(int x, int y);

    void Activate(int x, int y);

    void SecondaryActivate(int x, int y);

    void Scroll(int delta, String orientation);

    public static interface PropertyIconPixmapType extends TypeRef<List<PropertyIconPixmapStruct>> {

    }

    public static interface PropertyOverlayIconPixmapType extends TypeRef<List<PropertyOverlayIconPixmapStruct>> {

    }

    public static interface PropertyAttentionIconPixmapType extends TypeRef<List<PropertyAttentionIconPixmapStruct>> {

    }

    public static class NewTitle extends DBusSignal {

        public NewTitle(String path) throws DBusException {
            super(path);
        
        }

    }

    public static class NewIcon extends DBusSignal {

        public NewIcon(String path) throws DBusException {
            super(path);
        
        }

    }

    public static class NewAttentionIcon extends DBusSignal {

        public NewAttentionIcon(String path) throws DBusException {
            super(path);
        
        }

    }

    public static class NewOverlayIcon extends DBusSignal {

        public NewOverlayIcon(String path) throws DBusException {
            super(path);
        
        }

    }

    public static class NewMenu extends DBusSignal {

        public NewMenu(String path) throws DBusException {
            super(path);
        
        }

    }

    public static class NewToolTip extends DBusSignal {

        public NewToolTip(String path) throws DBusException {
            super(path);
        
        }

    }

    public static class NewStatus extends DBusSignal {

        private final String status;

        public NewStatus(String path, String status) throws DBusException {
            super(path, status);
            this.status = status;
        }

        public String getStatus() {
            return status;
        }

    }

}
