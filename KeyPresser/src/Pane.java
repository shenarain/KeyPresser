import static util.ProccesUtils.Kernel32.OpenProcess;
import static util.ProccesUtils.Kernel32.PROCESS_QUERY_INFORMATION;
import static util.ProccesUtils.Kernel32.PROCESS_VM_READ;
import static util.ProccesUtils.Psapi.GetModuleBaseNameW;

import java.awt.AWTException;
import java.awt.Checkbox;
import java.awt.Color;
import java.awt.Event;
import java.awt.EventQueue;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.ptr.PointerByReference;

import util.ProccesUtils.User32DLL;

public class Pane extends JFrame {

	private JPanel contentPane;
	private JTextField sure;
	private JTextField tus1;
	private JTextField tus2;
	private JLabel status;
	public JButton baslat_1;
	public Checkbox clicker;
	private Robot robot = null;
	public static final int MAX_TITLE_LENGTH = 1024;
	public Timer time = new Timer();
	public int tus = 0;
	private Checkbox onlymc;

	/**
	 * Launch the application.
	 */
	public void run() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Pane frame = new Pane();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});

	}

	/**
	 * Create the frame.
	 */
	public Pane() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("xAIIIeb.png")));
		setTitle("AutoKeyPresser");
		setBounds(100, 100, 324, 150);
		contentPane = new JPanel();
		contentPane.setBorder(null);
		contentPane.setBackground(new Color(100, 149, 237));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		baslat_1 = new JButton("Start");
		baslat_1.setForeground(new Color(0, 0, 0));

		baslat_1.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (!(tus2.getText().equalsIgnoreCase("") || tus1.getText().equalsIgnoreCase("")
						|| sure.getText().equalsIgnoreCase(""))) {
					if (baslat_1.getText().equalsIgnoreCase("Start")) {
						baslat_1.setText("Stop");
						new Thread(() -> {
							while (true) {
								Start();
								if (baslat_1.getText().equalsIgnoreCase("Start")) {
									break;
								}
							}
						}).start();
						status.setText("keys" + Integer.valueOf(tus1.getText()) + "/" + Integer.valueOf(tus2.getText())
								+ " time " + Integer.valueOf(sure.getText()) + "ms ("
								+ Double.valueOf(sure.getText()) / 1000 + "sec)");
					} else if (baslat_1.getText().equalsIgnoreCase("Stop")) {
						baslat_1.setText("Start");
					}
				} else {
					status.setText("* Enter only numeric digits(0-9)");
					JOptionPane.showMessageDialog(contentPane, "* Enter only numeric digits(0-9)", "Alert",
							JOptionPane.WARNING_MESSAGE);
				}

			}
		});

		baslat_1.setBounds(210, 11, 89, 23);
		contentPane.add(baslat_1);

		sure = new JTextField();
		sure.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent ke) {
				if (!baslat_1.getText().equalsIgnoreCase("Stop")) {
					if (ke.getKeyChar() >= '0' && ke.getKeyChar() <= '9') {
						sure.setEditable(true);
					} else {
						status.setText("* Enter only numeric digits(0-9)");
						JOptionPane.showMessageDialog(contentPane, "* Enter only numeric digits(0-9)", "Alert",
								JOptionPane.WARNING_MESSAGE);
						sure.setText("");
						sure.setEditable(true);
					}
				} else {
					sure.setEditable(false);
				}
			}
		});
		sure.setBounds(90, 12, 110, 20);
		contentPane.add(sure);
		sure.setColumns(10);

		JLabel lblNewLabel = new JLabel("time diff(ms):");
		lblNewLabel.setBounds(10, 15, 77, 14);
		contentPane.add(lblNewLabel);

		tus1 = new JTextField();
		tus1.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent ke) {
				String value = tus1.getText();
				if (!baslat_1.getText().equalsIgnoreCase("Stop")) {
					if (ke.getKeyChar() >= '0' && ke.getKeyChar() <= '9' && value.length() < 1) {
						tus1.setEditable(true);
					} else {
						status.setText("* Enter only numeric digits(0-9)");
						JOptionPane.showMessageDialog(contentPane, "* Enter only numeric digits(0-9)", "Alert",
								JOptionPane.WARNING_MESSAGE);
						tus1.setText("");
						tus1.setEditable(true);
					}
				} else {
					tus1.setEditable(false);
				}
			}
		});
		tus1.setBounds(89, 46, 45, 20);
		contentPane.add(tus1);
		tus1.setColumns(10);

		tus2 = new JTextField();
		tus2.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent ke) {
				String value = tus2.getText();
				if (!baslat_1.getText().equalsIgnoreCase("Stop")) {
					if (ke.getKeyChar() >= '0' && ke.getKeyChar() <= '9' && value.length() < 1) {

						tus2.setEditable(true);
					} else {
						status.setText("* Enter only numeric digits(0-9)");
						JOptionPane.showMessageDialog(contentPane, "* Enter only numeric digits(0-9)", "Alert",
								JOptionPane.WARNING_MESSAGE);
						tus2.setText("");
						tus2.setEditable(true);
					}
				} else {
					tus2.setEditable(false);
				}
			}
		});
		tus2.setColumns(10);
		tus2.setBounds(155, 46, 45, 20);
		contentPane.add(tus2);

		JLabel lblKeys = new JLabel("key range");
		lblKeys.setBounds(10, 49, 77, 14);
		contentPane.add(lblKeys);

		status = new JLabel("Status");
		status.setHorizontalAlignment(SwingConstants.CENTER);
		status.setBounds(10, 77, 190, 20);
		contentPane.add(status);

		JLabel bos = new JLabel("<");
		bos.setHorizontalAlignment(SwingConstants.CENTER);
		bos.setBounds(133, 49, 23, 14);
		contentPane.add(bos);

		clicker = new Checkbox("MouseClicker");
		clicker.setBackground(new Color(100, 149, 237));
		clicker.setBounds(210, 45, 110, 23);
		contentPane.add(clicker);

		onlymc = new Checkbox("Only in MC");
		onlymc.setBackground(new Color(100, 149, 237));
		onlymc.setBounds(210, 74, 110, 23);
		contentPane.add(onlymc);

	}

	public void Start() {

		int min = Integer.valueOf(tus1.getText());
		int max = Integer.valueOf(tus2.getText());
		if (time.delay(Integer.valueOf(sure.getText())) && !sure.getText().equalsIgnoreCase("")) {
			if (tus <= max) {
				tus++;
			}
			if (tus > max) {
				tus = min;
			}
			if (onlymc.getState()) {
				if (getActiveWindowTitle().startsWith("Minecraft")
						&& getActiveWindowProcess().equalsIgnoreCase("javaw.exe")) {
					pressKeys(tus);
					if (clicker.getState())
						pressMouse();
				}
			} else {
				pressKeys(tus);
			}

			time.reset();
		}
	}

	public void pressMouse() {

		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}

		if (getActiveWindowTitle().startsWith("Minecraft") && getActiveWindowProcess().equalsIgnoreCase("javaw.exe")) {
			robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		} else {
			robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
		}

	}

	public void pressKeys(int key) {
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
		if (key == 1) {
			robot.keyPress(KeyEvent.VK_1);
		}
		if (key == 2) {
			robot.keyPress(KeyEvent.VK_2);
		}
		if (key == 3) {
			robot.keyPress(KeyEvent.VK_3);
		}
		if (key == 4) {
			robot.keyPress(KeyEvent.VK_4);
		}
		if (key == 5) {
			robot.keyPress(KeyEvent.VK_5);
		}
		if (key == 6) {
			robot.keyPress(KeyEvent.VK_6);
		}
		if (key == 7) {
			robot.keyPress(KeyEvent.VK_7);
		}
		if (key == 8) {
			robot.keyPress(KeyEvent.VK_8);
		}
		if (key == 9) {
			robot.keyPress(KeyEvent.VK_9);
		}
	}

	public static String getActiveWindowTitle() {
		char[] buffer = new char[MAX_TITLE_LENGTH * 2];
		HWND foregroundWindow = User32DLL.GetForegroundWindow();
		User32DLL.GetWindowTextW(foregroundWindow, buffer, MAX_TITLE_LENGTH);
		String title = Native.toString(buffer);
		return title;
	}

	public static String getActiveWindowProcess() {
		char[] buffer = new char[MAX_TITLE_LENGTH * 2];
		PointerByReference pointer = new PointerByReference();
		HWND foregroundWindow = User32DLL.GetForegroundWindow();
		User32DLL.GetWindowThreadProcessId(foregroundWindow, pointer);
		Pointer process = OpenProcess(PROCESS_QUERY_INFORMATION | PROCESS_VM_READ, false, pointer.getValue());
		GetModuleBaseNameW(process, null, buffer, MAX_TITLE_LENGTH);
		String processName = Native.toString(buffer);
		return processName;
	}
}
