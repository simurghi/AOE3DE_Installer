# Age Of Empires 3 DE: Installer

![Aoe3DEInstaller](https://github.com/user-attachments/assets/aeb63f4d-3b1f-432c-946e-2035ff3e8a75)

This is the java installer to a work-in-progress comprehensive balance overhaul for Age of Empires 3: Definitive Edition.

The included mod manager allows you to install and manage AOE3:DE Overhauled from outside of the game, without the need to restart after enabling the mod.. This application requires Java 8 to be installed to run and works on Linux and Windows (MacOS is not a supported). If you are running this on a linux distro with a unique desktop environment/standalone window manager like BSPWM or Xmonad, you will need to install wmname from your package manager and add "wmname LG3D" to your config file (or run it via the terminal) or else the application will not display anything due to limitations with Java Swing. 

<b>Installation:</b>

<b><i>AUTOMATED:</i></b> 

Double click to run the "Installer.jar" file (this is an executable java archive).  
Left click on the menu button to see all 4 options. Be sure to
view the "READ ME" option first. Click "Install Mod" to copy over files, and if you're
done, click "Remove Mod" to go back to regular DE.

NOTE: The Installer requires an updated version of Java on your system to work.
This program was built with OpenJDK 16, but requires Java 8 at the minimum. If
you don't have Java installed, go to oracle.com/java or use your system package
manager on Linux. 

NOTE 2: This Installer currently only supports default Steam installations on
Linux and Windows. If you're using a non-standard install, the Microsoft
Store/Game Pass, you need to do the manual method in the meantime. 

<b><i>MANUAL:</i></b>

Copy the mod directory (./mod-files/AOE3DE-Overhauled) to your Age of Empires 3 DE's mods directory to
install the mod.


Linux: this should be $HOME/.local/share/Steam/steamapps/compatdata/933110/pfx/drive_c/users/steamuser/Games/Age of Empires 3 DE/(16 Digit Unique Numeric ID)/mods/local

Windows: %USERPROFILE%\Games\Age of Empires 3 DE\ (16 Digit Unique Numeric ID)\mods\local 


<b>License:</b>

This program is licensed under GPL 3.0 as free software. This excludes the recursive file copying algorithms written by Mkyong, as those are separately licensed under MIT. Finally, the asset provided (specifically the wallpaper provided in the main application menu) is property of Microsoft.
