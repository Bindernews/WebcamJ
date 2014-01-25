package org.olentangyfrc.webcamj;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamDiscoveryEvent;
import com.github.sarxos.webcam.WebcamDiscoveryListener;

public class WebcamJ extends JFrame implements WebcamDiscoveryListener {

	private static final long serialVersionUID = -4281858780174576605L;

	private JPanel contentPane;
	private SuperWebcamPanel camera;
	private JMenuBar menubar;
	private JMenu webcamMenu;
	private JMenu optionsMenu;
	
	private Aimer aimer;

	public WebcamJ() {
		addWindowListener(myWindowListener);
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
				createWebcamJMI(cam);
			}

			optionsMenu = new JMenu("Options");
			jmi = optionsMenu.add("Add URL...");
			jmi.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					OpenURLDialog ourd = new OpenURLDialog(WebcamJ.this);
					ourd.setVisible(true);
				}
			});

			menubar = new JMenuBar();
			menubar.add(webcamMenu);
			menubar.add(optionsMenu);
			setJMenuBar(menubar);
		}

		camera = new SuperWebcamPanel(Webcam.getDefault());
		aimer = new Aimer(camera);
		contentPane.add(camera, BorderLayout.CENTER);
		Webcam.addDiscoveryListener(this);

		pack();
	}

	protected JMenuItem createWebcamJMI(Webcam cam) {
		JMenuItem jmi = webcamMenu.add(cam.getName());
		jmi.setName("webcam");
		jmi.addActionListener(webcamMenuItemListener);
		return jmi;
	}

	/**
	 * This action listener handles ActionEvents for all the webcam menu items.
	 */
	protected final ActionListener webcamMenuItemListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			JMenuItem jmi = (JMenuItem) e.getSource();
			for (Webcam cam : Webcam.getWebcams()) {
				if (cam.getName().equals(jmi.getText()))
					camera.setWebcam(cam);
			}
		}
	};
	
	protected final WindowAdapter myWindowListener = new WindowAdapter() {
		public void windowClosing(WindowEvent e) {
			camera.setWebcam(null);
			try {
				aimer.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	};

	@Override
	public void webcamGone(WebcamDiscoveryEvent e) {
		for (int i = 0; i < webcamMenu.getItemCount(); i++) {
			JMenuItem jmi = webcamMenu.getItem(i);
			if (jmi.getText().equals(e.getWebcam().getName())) {
				webcamMenu.remove(i);
				break;
			}
		}
	}

	@Override
	public void webcamFound(WebcamDiscoveryEvent e) {
		createWebcamJMI(e.getWebcam());
	}
	
	public SuperWebcamPanel getCameraPanel() {
		return camera;
	}

	public static void main(String[] args) {
		CameraManager.initialize();
		final WebcamJ window = new WebcamJ();
		window.setVisible(true);
	}
}
