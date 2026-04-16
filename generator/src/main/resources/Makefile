clean:
	rm -f *xml

download-kde: clean
	wget https://github.com/KDE/kstatusnotifieritem/raw/refs/heads/master/src/org.kde.StatusNotifierItem.xml
	wget https://github.com/KDE/kstatusnotifieritem/raw/refs/heads/master/src/org.kde.StatusNotifierWatcher.xml
	wget https://github.com/KDE/kstatusnotifieritem/raw/refs/heads/master/src/libdbusmenu-qt/com.canonical.dbusmenu.xml

# Currently unused
download-ubuntu: clean
	wget https://github.com/ubuntu/gnome-shell-extension-appindicator/raw/refs/heads/master/interfaces-xml/StatusNotifierItem.xml
	wget https://github.com/ubuntu/gnome-shell-extension-appindicator/raw/refs/heads/master/interfaces-xml/StatusNotifierWatcher.xml
	wget https://github.com/ubuntu/gnome-shell-extension-appindicator/raw/refs/heads/master/interfaces-xml/DBusMenu.xml
