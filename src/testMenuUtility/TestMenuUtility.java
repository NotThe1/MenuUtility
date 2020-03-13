package testMenuUtility;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.prefs.Preferences;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;

import appLogger.AppLogger;
import menuUtility.MenuUtility;

public class TestMenuUtility {
	private TestMenuUtilityAdapter adapterForTest = new TestMenuUtilityAdapter();
	private AppLogger log = AppLogger.getInstance();
	private JFrame frame;
	private String activeFilePath;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TestMenuUtility window = new TestMenuUtility();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}// main

	private void loadFile(File subjectFile) {

		// closeFile();

		log.info("Loading File:");
		log.infof("       Path : %s%n", subjectFile.getAbsoluteFile());
		log.infof("       Size : %,d bytes  [%1$#X]%n%n", subjectFile.getTotalSpace());

		return;
	}// loadFile

	/////////////////////////////////////////////

	private void doFileOpen() {
		JFileChooser chooser = new JFileChooser(activeFilePath);
		if (chooser.showOpenDialog(frame) != JFileChooser.APPROVE_OPTION) {
			return; // just get out
		} // if open
		MenuUtility.addFileItem(mnuFile, chooser.getSelectedFile(),adapterForTest);
		log.infof("Added file: %s%n", chooser.getSelectedFile());
	}// doFileOpen

	private void doFileClose() {
		log.info("** Do File Close  **");
	}// doFileSave

	private void doFileSave() {
		log.info("[HexEditor.doFileSave]");
		}// doFileSave

	private void doFileSaveAs() {
		log.info("** [doFileSaveAs] **");
		JFileChooser chooser = new JFileChooser(activeFilePath);
		if (chooser.showOpenDialog(frame) != JFileChooser.APPROVE_OPTION) {
			return; // just get out
		} // if open

		MenuUtility.addFileItem(mnuFile, chooser.getSelectedFile(),adapterForTest);
		doFileSave();
	}// doFileSaveAs

	private void doFileExit() {
		appClose();
	}// doFileExit

	/////////////////////////////////////////////////////////////////////////

	private Preferences getPreferences() {
		return Preferences.userNodeForPackage(TestMenuUtility.class).node(this.getClass().getSimpleName());
	}// getPreferences

	private void appClose() {
		Preferences myPrefs = getPreferences();
		Dimension dim = frame.getSize();
		myPrefs.putInt("Height", dim.height);
		myPrefs.putInt("Width", dim.width);
		Point point = frame.getLocation();
		myPrefs.putInt("LocX", point.x);
		myPrefs.putInt("LocY", point.y);
		myPrefs.putInt("DividerLocationMain", splitPane.getDividerLocation());
		myPrefs.put("CurrentPath", activeFilePath);

		MenuUtility.saveRecentFileList(myPrefs, mnuFile);
		
		System.exit(0);
	}// appClose

	private void appInit() {
		log.setDoc(textPane.getStyledDocument());
		Preferences myPrefs = getPreferences();
		frame.setSize(myPrefs.getInt("Width", 761), myPrefs.getInt("Height", 693));
		frame.setLocation(myPrefs.getInt("LocX", 100), myPrefs.getInt("LocY", 100));
		splitPane.setDividerLocation(myPrefs.getInt("DividerLocationMain", 500));
		activeFilePath = myPrefs.get("CurrentPath", DEFAULT_DIRECTORY);

		MenuUtility.loadRecentFileList(myPrefs, mnuFile,adapterForTest);

	}// appInit

	/**
	 * Create the application.
	 */
	public TestMenuUtility() {
		initialize();
		appInit();
	}// Constructor

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

		mnuFile = new JMenu("File");
		menuBar.add(mnuFile);

		mnuFileOpen = new JMenuItem("Open...");
		mnuFileOpen.setIcon(new ImageIcon(TestMenuUtility.class.getResource("/resources/open.png")));
		mnuFileOpen.setName(MNU_FILE_OPEN);
		mnuFileOpen.addActionListener(adapterForTest);
		mnuFile.add(mnuFileOpen);

		JSeparator separator99 = new JSeparator();
		mnuFile.add(separator99);

		mnuFileSave = new JMenuItem("Save...");
		mnuFileSave.setIcon(new ImageIcon(TestMenuUtility.class.getResource("/resources/save.png")));
		mnuFileSave.setName(MNU_FILE_SAVE);
		mnuFileSave.addActionListener(adapterForTest);

		mnuFileClose = new JMenuItem("Close");
		mnuFileClose.setName(MNU_FILE_CLOSE);
		mnuFileClose.addActionListener(adapterForTest);
		mnuFileClose.setIcon(new ImageIcon(TestMenuUtility.class.getResource("/resources/close.png")));
		mnuFile.add(mnuFileClose);

		separator_1 = new JSeparator();
		mnuFile.add(separator_1);
		mnuFile.add(mnuFileSave);

		mnuFileSaveAs = new JMenuItem("Save As...");
		mnuFileSaveAs.setIcon(new ImageIcon(TestMenuUtility.class.getResource("/resources/saveAs.png")));
		mnuFileSaveAs.setName(MNU_FILE_SAVE_AS);
		mnuFileSaveAs.addActionListener(adapterForTest);
		mnuFile.add(mnuFileSaveAs);

		JSeparator separatorFileStart = new JSeparator();
		separatorFileStart.setName(MenuUtility.RECENT_FILES_START);
		mnuFile.add(separatorFileStart);

		JSeparator separatorFileEnd = new JSeparator();
		separatorFileEnd.setName(MenuUtility.RECENT_FILES_END);
		separatorFileEnd.setVisible(false);
		mnuFile.add(separatorFileEnd);

		mnuRemoveRecentFiles = new JMenuItem("Remove Recent Files");
		mnuRemoveRecentFiles.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				MenuUtility.clearList(mnuFile);
			}// action Performed
		});// addActionListener

		mnuFile.add(mnuRemoveRecentFiles);

		mnuFileExit = new JMenuItem("Exit");
		mnuFileExit.setName(MNU_FILE_EXIT);
		mnuFileExit.addActionListener(adapterForTest);
		mnuFile.add(mnuFileExit);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
		frame.getContentPane().setLayout(gridBagLayout);

		splitPane = new JSplitPane();
		GridBagConstraints gbc_splitPane = new GridBagConstraints();
		gbc_splitPane.fill = GridBagConstraints.BOTH;
		gbc_splitPane.gridx = 0;
		gbc_splitPane.gridy = 0;
		frame.getContentPane().add(splitPane, gbc_splitPane);

		scrollPane = new JScrollPane();
		splitPane.setRightComponent(scrollPane);

		textPane = new JTextPane();
		scrollPane.setViewportView(textPane);

	}// initialize

	///////////////////////////////////////////////////////////////////////////////////////////
	class TestMenuUtilityAdapter implements ActionListener {// ,
		// ListSelectionListener
		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			String name = ((Component) actionEvent.getSource()).getName();

			if (name == null) {
				loadFile(new File(actionEvent.getActionCommand()));
			} else {
				switch (name) {
				case MNU_FILE_OPEN:
					doFileOpen();
					break;
				case MNU_FILE_CLOSE:
					doFileClose();
					break;
				case MNU_FILE_SAVE:
					doFileSave();
					break;
				case MNU_FILE_SAVE_AS:
					doFileSaveAs();
					break;
				case MNU_FILE_EXIT:
					doFileExit();
					break;

				default:
					log.special(actionEvent.getActionCommand());
				}// switch
			} // if
		}// actionPerformed

	}// class AdapterAction
		///////////////////////////////////////////////////////////////////////////////////////////

	private JMenuItem mnuFileExit;
	private JMenuItem mnuFileOpen;
	private JMenuItem mnuFileSave;
	private JMenuItem mnuFileSaveAs;

	private JMenuItem mnuRemoveRecentFiles;
	private JMenu mnuFile;
	private JMenuItem mnuFileClose;
	private JSeparator separator_1;

	private static final String MNU_FILE_OPEN = "mnuFileOpen";
	private static final String MNU_FILE_CLOSE = "mnuFileclose";
	private static final String MNU_FILE_SAVE = "mnuFileSave";
	private static final String MNU_FILE_SAVE_AS = "mnuFileSaveAs";
	private static final String MNU_FILE_EXIT = "mnuFileExit";

	private static final String DEFAULT_DIRECTORY = ".";

	private JSplitPane splitPane;
	private JScrollPane scrollPane;
	private JTextPane textPane;

}// class TestMenuUtility
