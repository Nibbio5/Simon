# Simon
A simon repo for the midterm of Embedded Programming System 2026 course of Unipd.

This project has been realized initially via constraint-layout on compose, and near its end I refactored the gui and made it only with compose.
This has append because when I was near the end of the first screen, a bug that I couldn't fix happened (when the device is in inverse landscape mode the padding is different than the normal landscape mode).

In order to make this project i used Android official libraries and Compose libraries, with the corresponding documentation.

An improvement for the app could be to make the
text, end button and delete button adaptive to the screen.

## Testing

 The testing has been done in a physical and emulated device
- **Huawei p40 lite** as physical device
- **Google pixel 9a** as emulated device with the following settings

**IDE** :  Android Studio Otter 3 Feature Drop 2025.2.3
| Setting | Value |
| :--- | :--- |
| `avd.ini.displayname` | Pixel 9a |
| `avd.ini.encoding` | UTF-8 |
| `AvdId` | Pixel_9a |
| `disk.dataPartition.size` | 6G |
| `fastboot.chosenSnapshotFile` | |
| `fastboot.forceChosenSnapshotBoot` | no |
| `fastboot.forceColdBoot` | no |
| `fastboot.forceFastBoot` | yes |
| `hw.accelerometer` | yes |
| `hw.arc` | false |
| `hw.audioInput` | yes |
| `hw.battery` | yes |
| `hw.camera.back` | virtualscene |
| `hw.camera.front` | emulated |
| `hw.cpu.ncore` | 4 |
| `hw.device.hash2` | MD5:b5808f7769fd026afce59cb3aa3bb46c |
| `hw.device.manufacturer` | Google |
| `hw.device.name` | pixel_9a |
| `hw.dPad` | no |
| `hw.gps` | yes |
| `hw.gpu.enabled` | yes |
| `hw.gpu.mode` | auto |
| `hw.gyroscope` | yes |
| `hw.initialOrientation` | portrait |
| `hw.keyboard` | yes |
| `hw.lcd.density` | 420 |
| `hw.lcd.height` | 2424 |
| `hw.lcd.width` | 1080 |
| `hw.mainKeys` | no |
| `hw.ramSize` | 2048 |
| `hw.sdCard` | yes |
| `hw.sensors.light` | yes |
| `hw.sensors.magnetic_field` | yes |
| `hw.sensors.orientation` | yes |
| `hw.sensors.pressure` | yes |
| `hw.sensors.proximity` | yes |
| `hw.trackBall` | no |
| `image.sysdir.1` | system-images\android-37.0\google_apis_ps16k\x86_64\ |
| `PlayStore.enabled` | false |
| `runtime.network.latency` | none |
| `runtime.network.speed` | full |
| `showDeviceFrame` | yes |
| `skin.dynamic` | yes |
| `tag.display` | Google APIs |
| `tag.displaynames` | Google APIs,16 KB Page Size,AI Glasses Compatible |
| `tag.id` | google_apis |
| `tag.ids` | google_apis,page_size_16kb,ai_glasses_compatible |
| `target` | android-37.0 |
| `vm.heapSize` | 228 |

