package org.olentangyfrc.webcamj;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;

public class SuperWebcamPanel extends JPanel {
	
	// this is required because it's a JComponent. Just leave it.
	private static final long serialVersionUID = 5928726857391558813L;
	
	/**
	 * This holds a reference to the webcam.
	 */
	private Webcam webcam;
	
	/**
	 * This is used to actually view the webcam.
	 */
	private WebcamPanel camPanel;
	
	
	public SuperWebcamPanel() {
		initialize(null);
	}
	
	public SuperWebcamPanel(Webcam cam) {
		initialize(cam);
	}
	
	private void initialize(Webcam cam) {
		setLayout(new BorderLayout());
		setWebcam(cam);
	}
	
	public void setWebcam(Webcam thecam) {
		// no point in doing all this work just to use the same webcam
		if (thecam == webcam) {
			return;
		}
		if (thecam == null) {
			if (camPanel != null)
				camPanel.stop();
			webcam = null;
		} else {
			// remove the old camPanel
			if (camPanel != null) {
				camPanel.stop();
				remove(camPanel);
				camPanel = null;
			}
			webcam = thecam;
			// We want to make sure the webcam is at max resolution. We also
			// don't want to change the resolution if we don't have to.
			Dimension[] resolutions = webcam.getViewSizes();
			Dimension maxResolution = resolutions[resolutions.length - 1];
			if (!webcam.getViewSize().equals(maxResolution)) {
				if (webcam.isOpen())
					webcam.close(); // close it so we can change the view size
				webcam.setViewSize(maxResolution);
			}
			
			// now we create the new panel and go from there
			camPanel = new WebcamPanel(webcam);
			camPanel.setFillArea(true);
			add(camPanel, BorderLayout.CENTER);

			repaint();
		}
	}
	
	public Webcam getWebcam() {
		return webcam;
	}
	
	public WebcamPanel getPanel() {
		return camPanel;
	}
}
