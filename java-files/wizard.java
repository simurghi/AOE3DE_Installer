//special thanks to Derek Banas' youtube tutorials, mkyong's recursive file copying, Nambi's image drawing, and Oracle's documentation 
// https://stackoverflow.com/questions/18777893/jframe-background-image for putting on background image. 

import java.awt.event.*; 
import javax.swing.*; 
import java.nio.file.*;
import java.awt.BorderLayout; 
import java.io.IOException;
import java.nio.file.attribute.BasicFileAttributes;
import javax.swing.UIManager.*; 
import java.io.File;
import java.awt.Graphics;
import javax.swing.ImageIcon;
import java.util.Comparator;
import java.util.stream.Stream;

public class wizard extends JFrame implements ActionListener
{
	//class variables to store paths to game files and construct the GUI.
	private String versionNum; 
	private String[] currentPathChildList;
	private JMenu menu; 
	private JMenuItem itemHelp; 
	private JMenuItem installMod;
	private JMenuItem removeMod;
	private JMenuItem exitProgram;
	private JMenuBar jvBar; 
	private JLabel readMeLabel; 
	private JLabel background;  
	private JPanel panel; 
	private File currentPathSubDirs; 
	private Path currentPath; 
	private Path testPath;
	private Path backupPath;
	private Path modPath;
	private Path deleteCheckPath;

	public static void main(String[] args){
	//launches the application windows
		new wizard(); 
	}

	//constructor that launchers helper methods to create the GUI experience.
	public wizard(){
		setDefaultOSDirectory(); 
		setNonOSPaths(); 
		instantiateFields(); 
		windowPropertiesInit(); 
		constructGUI(); 
	}

	public void instantiateFields(){ 
		//labels and instantiates the buttons
		menu = new JMenu("Menu"); 
		versionNum = "1.00a - clean rebuild";
		itemHelp = new JMenuItem("READ ME");
		installMod = new JMenuItem("Install Mod"); 
		removeMod = new JMenuItem("Remove Mod"); 
		exitProgram = new JMenuItem("Exit"); 
		jvBar = new JMenuBar(); 
		background = new JLabel(new ImageIcon("background_wallpaper.jpg")); 
		readMeLabel = new JLabel("<html> This is the setup program for Age of Empires 3 Definitive Edition Overhauled. <br> <br> This DOES NOT work for Microsoft Store/Game Pass copies, only Steam versions. This program WILL NOT work if you move the files from the mod's path or have a non-standard AoE3 install, you will need to do a manual install then. <br> <br> Clicking on \"Install Mod\" will create a backup of your Age Of Empires 3 mods in \"./Age of Empires 3 DE/(16 Digit Numeric ID)/mods/local\" in the \"$HOME/Documents/MODBAK\" folder. <br> <br> Clicking on \"Remove Mod\" will override your data files with the backup created earlier. <br> <br> Default Installation Paths. WIN: C:\\users\\your_username\\Games\\Age of Empires 3 DE\\ LINUX: ~/.local/.share/Steam/steamapps/compatdata/933110/pfx/drive_c/users/steamuser/Games/Age Of Empires 3 DE/ </html>");
		panel = new JPanel();
	}

	public void windowPropertiesInit(){
		//set a layout and basic size and window properties. 
		//helper method to clean up the constructor and set the size and properties of the GUI application.
		panel.setLayout(new BorderLayout()); 
		this.setSize(960,540); 
		this.setLocationRelativeTo(null); 
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		this.setTitle("Age of Empires 3: Overhauled - " + versionNum);
	}

	public void constructGUI(){
		//add the components to each other, then to the panel
		jvBar.add(menu, BorderLayout.PAGE_START); 
		panel.add(jvBar, BorderLayout.PAGE_START); 
		panel.add(background); 
		menu.add(itemHelp); 
		menu.add(installMod); 
		menu.add(removeMod); 
		menu.add(exitProgram);
		itemHelp.addActionListener(this); 
		installMod.addActionListener(this); 
		removeMod.addActionListener(this);
		exitProgram.addActionListener(this);
		this.add(panel);
		this.setSize(960,540); 
		this.setVisible(true);
	}

	private void setNonOSPaths(){
		//set all of the necessary system paths we need for file transfer, OS Specific ones are handled by a different helper method. 
		//we create a lot of backup paths and do a lot of testing since we're handling file deletion/copying. I don't want to delete someone's important documents by accident!
		modPath = Paths.get(FileSystems.getDefault().getPath(".").toAbsolutePath() +"/mod-files");
		deleteCheckPath = Paths.get(currentPath +"/AOE3DE-Overhauled/deletecheck.file");
	}

	private void setDefaultOSDirectory(){
		//method to see what OS the user is on. Due to AoE3 being deprecated on MacOS, it's not a supported platform. And given that the game is no longer sold in retail stores, only Steam is being currently supported for now
		String thisOS = System.getProperty("os.name"); 
		String home = System.getProperty("user.home");
		//There is a unique numerical id, a "common" folder, and a "logs" folder that we will be storing.

		if (thisOS.contains("Windows") || thisOS.contains("windows"))
		{
			System.out.println("User's current OS is a Windows variant, setting default directory");
			currentPathSubDirs = new File(home + "Games/Age of Empires 3 DE");
			currentPathChildList = currentPathSubDirs.list(); 
			currentPath = Paths.get(home + "Games/Age of Empires 3 DE/" + currentPathChildList[0] + "/mods/local");
			//currently currentPath is hardcoded, this can be changed in the future to allow users to point to a specific directory
			testPath = Paths.get(home + "Games/Age of Empires 3 DE/" + currentPathChildList[0] + "mods/local");
			//The backup will be saved in the user's home directory by default
			backupPath = Paths.get(home + "Documents/MODBAK"); 


		}
		else if (thisOS.contains("Linux") || thisOS.contains("linux"))
		{
			System.out.println("User's current OS is a Linux variant, setting default directory"); 
			currentPathSubDirs = new File(home + "/.local/share/Steam/steamapps/compatdata/933110/pfx/drive_c/users/steamuser/Games/Age of Empires 3 DE"); 
			currentPathChildList = currentPathSubDirs.list(); 
			currentPath = Paths.get(home + "/.local/share/Steam/steamapps/compatdata/933110/pfx/drive_c/users/steamuser/Games/Age of Empires 3 DE/" + currentPathChildList[0] + "/mods/local"); 
			testPath = Paths.get(home + "/.local/share/Steam/steamapps/compatdata/933110/pfx/drive_c/users/steamuser/Games/Age of Empires 3 DE/" + currentPathChildList[0] + "/mods/local"); 
			backupPath = Paths.get(home + "/Documents/MODBAK"); 
		}
	}




	public void actionPerformed(ActionEvent e){
		//I've incorporated a lot of Sysouts for debugging purposes in case the user needs them. Feel free to comment them out for clarity's sake. 
		if (e.getSource() == itemHelp)
			//show the read me once the user clicks on the menu button, given that it's one line, a helper method isn't needed.
			JOptionPane.showMessageDialog(null, readMeLabel, "READ ME", JOptionPane.INFORMATION_MESSAGE);

		else if (e.getSource() == installMod)
		{
			//call the helper method to do installation logic for file copying/backup. 
			installModLogic(); 
		}
			
		else if(e.getSource() == removeMod)
		{
			//call the helper method to remove the modded files and restore the originals from backups. 
			removeModLogic(); 
					
		}
		else if(e.getSource() == exitProgram)
			//terminate the program if the user quits. 
			System.exit(0);
	}

	private void installModLogic(){
		//helper method to recursively copy over included files to the game's path. 
		//Since most users of the game will have the steam copy (retail hasn't been sold in over a decade), I'm designing around the default. 
			System.out.println("This current path is:" + currentPath);
			System.out.println("Test path is:" + testPath);
			System.out.println("Mod path is:" + modPath);
			System.out.println("Delete path is:" + deleteCheckPath); 
				//make sure that the user is in the right path to copy the files, and not somewhere else
			if (currentPath.toString().equals(testPath.toString()) == false)
				System.out.println("it didn't work! You're probably not in the right directory."); 

				else if(currentPath.toString().equals(testPath.toString()) == true)
				{
					try
					{
						//backup files before we do anything else
						//if the user has already installed the mod and tries to reinstall it, we don't create a backup
						JOptionPane.showMessageDialog(null, "Installation has begun, this may take a little while. \n Click on OK to proceed. \n If installation fails, run the program in cmd.exe or terminal to see any error messages", "Message", JOptionPane.INFORMATION_MESSAGE); 
						if (Files.exists(deleteCheckPath) && Files.isRegularFile(deleteCheckPath))
							System.out.println("Data files have been modified. Skipping backup.");
						//if the user hasn't installed the mod, then we create a backup
						else if(!Files.exists(deleteCheckPath))
						{
							copyDirectoryFileVisitor(currentPath.toString(), backupPath.toString());
						}
						//in either case, we still copy over the mod install files
							copyDirectoryFileVisitor(modPath.toString(), currentPath.toString()); 
							JOptionPane.showMessageDialog(null, "File Copy has completed", "Message", JOptionPane.INFORMATION_MESSAGE); 
						
					}
					catch(IOException ex)
					{
						System.out.println(ex.toString()); 
					}
				}
	}

	private void removeModLogic(){
			//Helper method to remove the installed files from the mod. 
			//Before we delete anything, a backup of the current files are made to ensure that we have a restore point. 
			System.out.println("Current path is:" + currentPath);
			System.out.println("Test path is:" + testPath);
			System.out.println("Mod path is:" + modPath);
			System.out.println("Delete path is:" + deleteCheckPath); 
			System.out.println();
			//make sure that the user is in the right path to copy the files, and not somewhere else
			if (currentPath.toString().equals(testPath.toString()) == false)
				System.out.println("it didn't work! You're probably in the wrong directory!"); 
			else if(currentPath.toString().equals(testPath.toString()) == true)
			{
				if (Files.exists(deleteCheckPath) && Files.isRegularFile(deleteCheckPath))
					try
					{
						System.out.println("Does the delete file exist: " + Files.exists(deleteCheckPath)); 
						System.out.println("Is the test file a regular file: " +  Files.isRegularFile(deleteCheckPath));
					//delete the modified directory and replace it with the default one
						JOptionPane.showMessageDialog(null, "File Restore has started, this may take a little while. \n Click on OK to proceed.", "Message", JOptionPane.INFORMATION_MESSAGE); 
            					deleteDirectoryJava8(currentPath.toString() + "/AOE3DE-Overhauled");
						System.out.println("done deleting, check your mod path to confirm it's gone"); 
						copyDirectoryFileVisitor(backupPath.toString(), currentPath.toString());
						JOptionPane.showMessageDialog(null, "File Restore has completed", "Message", JOptionPane.INFORMATION_MESSAGE); 

					}
					catch(IOException ex)
					{
						System.out.println(ex.toString()); 
					}
				else if(!Files.exists(deleteCheckPath))
				{
					try
					{
						//since we're trying to delete a file that doesn't exist, we're assuming that the user doesn't have the mod installed and did a misclick. In that case, we back up existing files.
						copyDirectoryFileVisitor(currentPath.toString(), backupPath.toString());

					}
					catch(IOException ex)
					{
						System.out.println(ex.toString()); 

					}

				}


			}
	}


	


	//recursive copy method taken from mkyong.com. See included MIT license file 
	public static void copyDirectoryFileVisitor(String source, String target) throws IOException 
	{

        	TreeCopyFileVisitor fileVisitor = new TreeCopyFileVisitor(source, target);
        	Files.walkFileTree(Paths.get(source), fileVisitor);

    	}
	//recursive delete method taken from mkyong.com. See included MIT license file
	public static void deleteDirectoryJava8(String dir) throws IOException {
      		Path path = Paths.get(dir);
     	 	// read java doc, Files.walk need close the resources.
     	 	// try-with-resources to ensure that the stream's open directories are closed
     	 	try (Stream<Path> walk = Files.walk(path)) {
          		walk.sorted(Comparator.reverseOrder()).forEach(wizard::deleteDirectoryJava8Extract);
      }
  }

  	// extract method to handle exception in lambda taken from mkyong.com. See included MIT license file. 
  	public static void deleteDirectoryJava8Extract(Path path) {
      	try {
          	Files.delete(path);
      	} catch (IOException e) {
          System.err.printf("Unable to delete this path : %s%n%s", path, e);
      	}
  	}
}









