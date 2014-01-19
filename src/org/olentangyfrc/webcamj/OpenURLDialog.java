package org.olentangyfrc.webcamj;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.github.sarxos.webcam.ds.ipcam.IpCamDeviceRegistry;
import com.github.sarxos.webcam.ds.ipcam.IpCamMode;

import java.awt.Insets;
import java.awt.Point;

public class OpenURLDialog extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	private final JPanel contentPanel = new JPanel();
	private JTextField textField;	
	private JTextField statusBar;
	
	public OpenURLDialog(JFrame parent) {
		super(parent);
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
	
	public void actionPerformed(ActionEvent e) {
		if ("OK".equals(e.getActionCommand())) {
			try {
				// make sure the url is valid
				URL theurl = new URL(textField.getText());
				// check that we can actually connect
				InputStream stream = theurl.openStream();
				stream.close();
				// create the webcam device
				IpCamDeviceRegistry.register(textField.getText(), theurl, IpCamMode.PULL);
				// destroy the window
				dispose();
			} catch (MalformedURLException ex) {
				statusBar.setText("Invalid URL");
			} catch (IOException ex) {
				statusBar.setText("IOException: " + ex.getMessage());
			}
		} else if ("Cancel".equals(e.getActionCommand())) {
			dispose();
		}
	}
}
