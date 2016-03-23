package com.jeffpeng.jmod.validator;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.jeffpeng.jmod.JMODContainer;
import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.primitives.JMODInfo;
import com.jeffpeng.jmod.primitives.ModCreationException;
import com.jeffpeng.jmod.util.LoaderUtil;

public class Validator extends JPanel implements ActionListener {

	private static Process p;

	private static JFrame frame;
	private static final long serialVersionUID = 1L;
	private JMODInfo configdata;
	private File jmodfile;
	public static Object threadNotifier = new Object();

	public static boolean isValidator = false;

	private JTextArea notifier;
	private JFileChooser fileChooser;
	private JButton setButton, validateButton;

	private static Validator instance;

	public Validator() {
		super(new BorderLayout());

		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();

		setButton = new JButton("Set a JMOD...");
		setButton.addActionListener(this);
		setButton.setEnabled(false);

		validateButton = new JButton("Validate now");
		validateButton.addActionListener(this);
		unset();

		JPanel buttons = new JPanel();
		buttons.add(setButton);
		buttons.add(validateButton);

		notifier = new JTextArea(30, 160);
		notifier.setFont(new Font("Monospaced", Font.PLAIN, 15));
		notifier.setMargin(new Insets(5, 5, 5, 5));
		notifier.setEditable(false);
		notifier.setText("Welcome to JMOD's .jmod validator for Version @VERSION@\n\n");
		notifier.append("Please keep in mind that this only checks syntax and the first step of the API.\n");
		notifier.append("It does not validate actual items, block, recipe or if mods are present\n");
		notifier.setLineWrap(true);
		JScrollPane notifierPane = new JScrollPane(notifier);

		fileChooser = new JFileChooser();
		fileChooser.setFileHidingEnabled(false);
		try {
			fileChooser.setCurrentDirectory(new File(Validator.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()));
		} catch (URISyntaxException e) {

		}

		fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

		add(notifierPane, BorderLayout.CENTER);
		add(buttons, BorderLayout.PAGE_END);

		OutputStream notifierStream = new OutputStream() {

			@Override
			public void write(int b) throws IOException {
				//
				notifier.append(String.valueOf((char) b));
				// scrolls the text area to the end of data
				notifier.setCaretPosition(notifier.getDocument().getLength());
			}
		};

		PrintStream ps = new PrintStream(notifierStream);
		System.setOut(ps);
		System.setErr(ps);

		int x = (int) ((dimension.getWidth() - this.getWidth()) / 2);
		int y = (int) ((dimension.getHeight() - this.getHeight()) / 2);
		this.setLocation(x, y);

	}

	private void init() {

		try {
			setupForge();
		} catch (IOException e) {
			notifier.append("Forge Setup failed horribly :(\n");
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {

		isValidator = true;
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				// Turn off metal's use of bold fonts
				UIManager.put("swing.boldMetal", Boolean.FALSE);
				instance = createAndShowGUI();

				Thread t = new Thread(new Runnable() {
					@Override
					public void run() {
						instance.init();
					}
				});
				t.start();

			}
		});
	}

	private static Validator createAndShowGUI() {
		frame = new JFrame("JMOD Validator");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Validator validator = new Validator();
		frame.add(validator);
		frame.pack();
		frame.setVisible(true);
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				if (p != null && p.isAlive()) {
					p.destroyForcibly();
				}
			}
		});

		return validator;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == setButton) {
			notifier.setText("");
			int ret = fileChooser.showOpenDialog(Validator.this);

			if (ret == JFileChooser.APPROVE_OPTION) {
				jmodfile = fileChooser.getSelectedFile();
				notifier.append("Using file " + jmodfile + "\n");

				if (jmodfile.getName().endsWith(".jmod")) {

					Path entry = jmodfile.toPath();

					String rawjson = LoaderUtil.loadModJson(entry);
					if (rawjson == null) {
						notifier.append("Failed to load mod declaration from " + entry.toString() + ".");
						return;
					}
					notifier.append("Found mod declaration.\n");

					configdata = LoaderUtil.parseModJson(rawjson);
					if (configdata == null) {
						notifier.append("Failed to parse JSON from " + entry.toString() + "   Probably the JSON is malformed.");
						return;
					}
					notifier.append("Mod declaration is JSON conform.\n");

					if (!LoaderUtil.infoDataSanity(configdata, entry.getFileName().toString())) {
						return;
					}
					notifier.append("Mod declaration seems to be in order.\n");

					printmodinfo(configdata);

					notifier.append("\n" + configdata.name + "(" + configdata.modid + ") seems to be a JMOD. Now go on and validate it.");
					validateButton.setText("Validate " + configdata.name);
					validateButton.setEnabled(true);

				} else {
					notifier.append("Both packed and directory versions of JMODs -must- end with .jmod\n\n");
					unset();
				}

			}
			notifier.setCaretPosition(notifier.getDocument().getLength());

			// Handle save button action.
		}

		if (e.getSource() == validateButton) {

			notifier.append("\n\n***********************************************************************\n");
			notifier.append("Validating " + configdata.name + "... \n");
			JMODContainer newmod = null;
			try {
				newmod = new JMODContainer(new JMODRepresentation(configdata, !Files.isDirectory(jmodfile.toPath())), jmodfile);
			} catch (ModCreationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			newmod.getMod().runScripts();
			
			

		}
	}

	private void unset() {
		validateButton.setEnabled(false);
		validateButton.setText("Set a jmod first");
		jmodfile = null;
	}

	private void printmodinfo(JMODInfo info) {
		notifier.append("\nMod Name    :      " + info.name + "\n");
		notifier.append("Mod ID      :      " + info.modid + "\n");
		notifier.append("Mod Version :      " + info.version + "\n");
		notifier.append("Mod URL     :      " + info.url + "\n\n");
		notifier.append("Mod Credits :      " + info.credits + "\n");
		notifier.append("Mod Authors :\n");
		for (String author : info.authors) {
			notifier.append("     " + author + "\n");
		}

		notifier.append("\nScripts     :\n");
		for (String script : info.scripts) {
			notifier.append("     " + script + "\n");
		}
	}

	private void setupForge() throws IOException, InterruptedException {
		String userhome = System.getProperty("user.home");
		File jmodfolder = new File(userhome + "/.jmodvalidator");
		File gradlefolder = new File(userhome + "/.jmodvalidator/gradle");
		File wrapperfolder = new File(userhome + "/.jmodvalidator/gradle/wrapper");
		File gradlescript = new File(userhome + "/.jmodvalidator/gradlew");
		File gradlebat = new File(userhome + "/.jmodvalidator/gradlew.bat");
		File gradlewrapperjar = new File(userhome + "/.jmodvalidator/gradle/wrapper/gradle-wrapper.jar");
		File gradlewrapperprops = new File(userhome + "/.jmodvalidator/gradle/wrapper/gradle-wrapper.properties");
		File jmodsrc = new File(userhome + "/.jmodvalidator/src");
		File jmodmain = new File(userhome + "/.jmodvalidator/src/main");
		File jmodresources = new File(userhome + "/.jmodvalidator/src/main/resources");
		File jmodmeta = new File(userhome + "/.jmodvalidator/src/main/resources/META-INF");
		File jmodat = new File(userhome + "/.jmodvalidator/src/main/resources/META-INF/jmod_at.cfg");
		File buildgradle = new File(userhome + "/.jmodvalidator/build.gradle");
		File forgejar = new File(userhome + "/.jmodvalidator/build/dirtyArtifacts/forgeSrc-1.7.10-10.13.4.1558-1.7.10.jar");

		if (!wrapperfolder.exists()) {
			if (!gradlefolder.exists()) {
				if (!jmodfolder.exists()) {
					jmodfolder.mkdir();
				}
				gradlefolder.mkdir();
			}
			wrapperfolder.mkdir();
		}

		if (!jmodmeta.exists()) {
			if (!jmodresources.exists()) {
				if (!jmodmain.exists()) {
					if (!jmodsrc.exists()) {
						jmodsrc.mkdir();
					}
					jmodmain.mkdir();
				}
				jmodresources.mkdir();
			}
			jmodmeta.mkdir();
		}

		testAndLoad(gradlescript, new URL("https://raw.githubusercontent.com/jeffpengMC/JMOD/master/gradlew"));
		testAndLoad(gradlebat, new URL("https://raw.githubusercontent.com/jeffpengMC/JMOD/master/gradlew.bat"));
		testAndLoad(gradlewrapperjar, new URL("https://github.com/jeffpengMC/JMOD/raw/master/gradle/wrapper/gradle-wrapper.jar"));
		testAndLoad(gradlewrapperprops, new URL("https://raw.githubusercontent.com/jeffpengMC/JMOD/master/gradle/wrapper/gradle-wrapper.properties"));
		testAndLoad(jmodat, new URL("https://raw.githubusercontent.com/jeffpengMC/JMOD/master/src/main/resources/META-INF/jmod_at.cfg"));
		testAndLoad(buildgradle, new URL("https://raw.githubusercontent.com/jeffpengMC/JMOD/master/build.gradle"));

		if (!isWindoze()) {
			Runtime.getRuntime().exec("chmod +x " + gradlescript);
		}

		notifier.append("All Gradle files present\n");
		notifier.append("\nTesting for forgeSrc...\n");

		if (!forgejar.exists()) {
			notifier.append("\nBuilding ForgeSrc ... this will take a couple of minutes.\nDon't worry, you only need to do this once.\n");

			ProcessBuilder pb;
			Process p;

			if (isWindoze()) {
				pb = new ProcessBuilder(gradlebat.toString(), "setupDecompWorkspace");

			} else {
				pb = new ProcessBuilder(gradlescript.toString(), "setupDecompWorkspace");
			}

			pb.directory(jmodfolder);
			p = pb.start();
			notifier.append("\n");
			while (p.isAlive()) {
				notifier.append(".");
				Thread.sleep(100);
			}
			notifier.append("\nWhew. Did it!\n");
		}
		notifier.append("\nFound forgeSrc... all set.\n");

		String libpath = "/home/sven/.gradle/caches/modules-2/files-2.1";
		File mcJar = new File(jmodfolder + "/build/dirtyArtifacts/forgeSrc-1.7.10-10.13.4.1558-1.7.10.jar");
		List<File> libs = new ArrayList<>();

		try {
			JarLoader.addFile(mcJar);
			Files.walk(Paths.get(libpath)).forEach(filePath -> {
				if (filePath.toString().endsWith(".jar") || filePath.toString().endsWith(".zip"))
					libs.add(filePath.toFile());
			});
		} catch (IOException e1) {
		}

		notifier.append("\nLoading " + (libs.size() + 1) + " JARs into the classpath.\n\n");

		for (File lib : libs)
			JarLoader.addFile(lib);

		notifier.append("\nDone!\n\n");
		setButton.setEnabled(true);

	}

	private void testAndLoad(File file, URL url) throws IOException {
		if (!file.exists()) {
			notifier.append("Loading " + file + " from " + url + " ... ");
			ReadableByteChannel rbc = Channels.newChannel(url.openStream());
			FileOutputStream fos = new FileOutputStream(file);
			fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
			notifier.append("done! \n");
			fos.close();

		}
	}

	private boolean isWindoze() {
		String OS = System.getProperty("os.name");
		if (OS.startsWith("Windows"))
			return true;
		return false;
	}

	public static void msg(String msg) {
		if (isValidator)
			instance.notifier.append(msg + "\n");
	}

}
