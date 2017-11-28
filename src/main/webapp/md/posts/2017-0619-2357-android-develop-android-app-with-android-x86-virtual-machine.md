Google suggests using x86 Android emulator to get the best developing user experience.

However, if you like me only have a desktop running Windows and unfortunately with AMD CPU. 
And for some reason has trouble with the Genymotion.
Then the combination of VirtualBox + Android-x86 could be a good option to get rid of the painful ARM emulator.

I believe it won't be a trouble for everyone to download the latest Android-x86 livecd iso and VirtualBox installation file.
So, I only list the steps I took to make a comfortable developing environment for myself.

#### - Create separate partition for GRUB and root file system

Firstly, the Android-x86 installation stuck at the step of GRUB installation.
I managed to work around this problem by creating 2 partitions.
The first partition is for GRUB installation. 100MB is good enough for GRUB.
The second partition is for hosting Android-x86 system. I make it as large as 2GB.
After the installation is done, I got around 850MB left.

#### - Add customized video mode to get a 'portrait' display

By default, Android-x86 runs in landscape mode. Unlike the regular emulator comes with Android SDK.
VirtualBox does not support screen rotation. So, it won't be feasible to change display 'portrait / landscape' mode 
on the fly.
However, we may create new GRUB menu item for a 'portrait' display.

- Run command to add custom video mode to the virtual machine which named as "Android"
```	
VBoxManage setextradata "Android" "CustomVideoMode1" "480x800x16"
```

- Boot Android-x86 into Debug mode and edit the **/mnt/grub/menu.lst**.
Copy and paste the first menu item (including the title, kernel and initrd lines). 
On the 'title' line, append '480x800'. On the 'kernel' line, append 'UVESA_MODE=480x800 DPI=240' 

#### - Disable virtual network adapter to skip device configuration

After the first reboot completed, Android OS would request Google Account login to complete the initial device configuration.
As an testing emulator, logon Google Account is unnecessary. To avoid it attempting to connect Google service, 
I simply disconnect the virtual network adapter.

#### - Connect adb to Android-x86 virtual machine via network

Well, the last step is establishing adb connection to the running Android-x86 virtual machine.
So, I could install / debug my Android App.

- Firstly, press ALT-F1 to enter console after the Android home screen be shown.
- Secondly, run command  **netcfg** to get current IP address. For instance, in my case, the IP address is 192.168.56.101.
- Thirdly, press ALT-F7 to return to the Android home screen.
- At last, run below command in Window Command Prompt window for creating adb connection via network.
```
C:\> adb connect 192.168.56.101
```
- After all of the above steps, we may find the Android-x86 virtual machine in device list of **"adb devices"** command

All right, that's it. Hope it works for you too.

<!--eof-->