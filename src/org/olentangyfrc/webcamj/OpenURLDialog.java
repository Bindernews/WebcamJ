package org.olentangyfrc.webcamj;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.ds.ipcam.IpCamMode;

public class OpenURLDialog extends JDialog implements ActionListener, DocumentListener {
	private static final long serialVersionUID = 1L;
	
	private final JPanel contentPanel = new JPanel();
	private JTextField textField;
	private JTextField statusBar;
	private WebcamJ parent;
	
	public OpenURLDialog(WebcamJ parent) {
		super(parent);
		this.parent = parent;
		initialize();
	}

	private void initialize() {
		setTitle("Enter URL");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setResizable(false);
		setBounds(100, 100, 366, 250);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(3, 3, 3, 3));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 2));
		{
			textField = new JTextField();
			textField.getDocument().addDocumentListener(this);
			textField.setAlignmentY(Component.TOP_ALIGNMENT);
			contentPanel.add(textField);
			textField.setColumns(30);
		}
		{
			statusBar = new JTextField(20);
			statusBar.setAutoscrolls(false);
			statusBar.setEditable(false);
			statusBar.setForeground(Color.RED);
			contentPanel.add(statusBar, BorderLayout.SOUTH);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				okButton.addActionListener(this);
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				cancelButton.addActionListener(this);
				buttonPane.add(cancelButton);
			}
		}
		pack();
	}
	
	protected URL checkURL() {
		try {
			// make sure the url is valid
			URL theurl = new URL(textField.getText());
			statusBar.setForeground(Color.GREEN);
			statusBar.setText("URL Ok");
			return theurl;
		} catch (MalformedURLException e) {
			statusBar.setForeground(Color.RED);
			statusBar.setText("Bad URL");
			return null;
		}
	}
	
	public void actionPerformed(ActionEvent e) {
		if ("OK".equals(e.getActionCommand())) {
			try {
				URL theurl = checkURL(); 
				if (theurl != null) {
					Webcam cam = CameraManager.createIPCamera(theurl, IpCamMode.PUSH);
					parent.getCameraPanel().setWebcam(cam);
					// destroy the window if no exception is thrown
					dispose();
				}
			} catch (Exception ex) {
				statusBar.setForeground(Color.RED);
				statusBar.setText(ex.getClass().getSimpleName() + ": " + ex.getMessage());
			}
		} else if ("Cancel".equals(e.getActionCommand())) {
			dispose();
		}
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		// unimplemented
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		checkURL();
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		checkURL();
	}
}
