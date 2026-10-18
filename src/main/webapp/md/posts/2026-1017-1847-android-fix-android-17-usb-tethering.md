My Pixel 8 USB tethering stopped working on Windows 10 after Android 17 update.
Android 17 introduced several USB stack changes which impact USB tethering.

In windows device manager, Pixel 8 was recognized as a CDC NCM device in the other devices section and with a yellow triangle icon. Windows failed to properly recognize the device.

To fix this issue, we need to manually reinstall the driver.

Use the following steps:

- Connect Pixel 8 and enable **USB tethering**
- Go to **Device Manager** ( prese Win + x and select **Device Manager** )
- Locate **CDC NCM** in the **Other device** section
- Right click the device and select **Update driver**
- Choose **Let me pick from a list of available drivers on my computer**
- Select **Network adapters** from hardware category list
- Select **Microsoft** - **UsbNcm Host Device**
- Confirm and Finish
