package org.olentangyfrc.webcamj;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamCompositeDriver;
import com.github.sarxos.webcam.WebcamDiscoveryEvent;
import com.github.sarxos.webcam.WebcamDiscoveryListener;
import com.github.sarxos.webcam.ds.buildin.WebcamDefaultDriver;
import com.github.sarxos.webcam.ds.ipcam.IpCamDriver;

public class WebcamFrame extends JFrame implements MouseListener,
		WebcamDiscoveryListener {

	// allows use to use both built-in webcams and IP cameras
	protected static class MyWebcamDriver extends WebcamCompositeDriver {
		public MyWebcamDriver() {
			add(new WebcamDefaultDriver());
			add(new IpCamDriver());
		}
	}

	static {
		// set our composite driver as main driver
		Webcam.setDriver(new MyWebcamDriver());
	}

	private static final long serialVersionUID = -4281858780174576605L;

	private JPanel contentPane;
	private WebcamJ camera;
	private JMenuBar menubar;
	private JMenu webcamMenu;
	private JMenu optionsMenu;
	
	private WebcamFrame self = this; // this is for things such as ActionListeners

	public WebcamFrame() {
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				camera.setWebcam(null);
			}
		});
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 640, 480);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(1, 1, 1, 1));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		// create the menu bar and menus
		{
			JMenuItem jmi;
			webcamMenu = new JMenu("Webcams");
			for (Webcam cam : Webcam.getWebcams()) {
				jmi = webcamMenu.add(cam.getName());
				jmi.addActionListener(webcamMenuItemListener);
			}

			optionsMenu = new JMenu("Options");
			jmi = optionsMenu.add("Add URL...");
			jmi.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					OpenURLDialog ourd = new OpenURLDialog(self);
					ourd.setVisible(true);
				}
			});

			menubar = new JMenuBar();
			menubar.add(webcamMenu);
			menubar.add(optionsMenu);
			setJMenuBar(menubar);
		}

		camera = new WebcamJ(Webcam.getDefault());
		camera.addMouseListener(this);
		contentPane.add(camera, BorderLayout.CENTER);
		Webcam.addDiscoveryListener(this);

		pack();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		System.out.println("Mouse click: " + e.getX() + "," + e.getY());
	}

	/**
	 * This action listener handles ActionEvents for all the webcam menu items.
	 */
	protected final ActionListener webcamMenuItemListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			JMenuItem jmi = (JMenuItem) e.getSource();
			for (Webcam cam : Webcam.getWebcams()) {
				if (cam.getName() == jmi.getText())
					camera.setWebcam(cam);
			}
		}
	};

	@Override
	public void webcamGone(WebcamDiscoveryEvent e) {
		for (int i = 0; i < webcamMenu.getItemCount(); i++) {
			JMenuItem jmi = webcamMenu.getItem(i);
			if (jmi.getName().equals(e.getWebcam().getName())) {
				webcamMenu.remove(i);
				break;
			}
		}
	}

	@Override
	public void webcamFound(WebcamDiscoveryEvent e) {
		webcamMenu.add(createWebcamJMI(e.getWebcam()));
	}

	protected JMenuItem createWebcamJMI(Webcam cam) {
		JMenuItem jmi = new JMenuItem(cam.getName());
		jmi.setName("webcam");
		jmi.addActionListener(webcamMenuItemListener);
		return jmi;
	}

	// These methods are needed for the MouseListener
	// but they are unused
	public void mouseEntered(MouseEvent arg0) {
	}

	public void mouseExited(MouseEvent arg0) {
	}

	public void mousePressed(MouseEvent arg0) {
	}

	public void mouseReleased(MouseEvent arg0) {
	}
}
