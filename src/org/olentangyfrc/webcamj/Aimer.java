package org.olentangyfrc.webcamj;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.Closeable;
import java.io.IOException;

public class Aimer implements MouseListener, Closeable {

	private float mouseX, mouseY;
	private SuperWebcamPanel panel;
	
	public Aimer(SuperWebcamPanel panel) {
		this.panel = panel;
		panel.addMouseListener(this);
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		// Gets mouse clicks relative to center of camera
		mouseX = e.getX() - (panel.getWidth() / 2);
		mouseY = e.getY() - (panel.getHeight() / 2);
		// Makes mouse clicks normalized coordinates
		mouseX = (float) mouseX / (panel.getWidth() / 2);
		mouseY = (float) mouseY / (panel.getHeight() / 2);
		System.out.println("Normalized mouse clicks relative to center: "
				+ mouseX + "," + mouseY);
	}
	
	@Override
	public void close() throws IOException {
		// this will close network connections and stuff
		
	}
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	}

}
