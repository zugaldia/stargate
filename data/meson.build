fs = import('fs')
desktop_file = fs.copyfile('com.zugaldia.Stargate.desktop.in', 'com.zugaldia.Stargate.desktop',
  install: true,
  install_dir: get_option('datadir') / 'applications'
)

desktop_utils = find_program('desktop-file-validate', required: false, disabler: true)
test('Validate desktop file', desktop_utils, args: [desktop_file])

appstream_file = fs.copyfile('com.zugaldia.Stargate.metainfo.xml.in', 'com.zugaldia.Stargate.metainfo.xml',
  install: true,
  install_dir: get_option('datadir') / 'metainfo'
)

appstreamcli = find_program('appstreamcli', required: false, disabler: true)
test('Validate appstream file', appstreamcli,
     args: ['validate', '--no-net', '--explain', appstream_file]
)

subdir('icons')
