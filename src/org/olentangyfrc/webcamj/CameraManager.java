package org.olentangyfrc.webcamj;

import java.io.IOException;
import java.net.URL;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamCompositeDriver;
import com.github.sarxos.webcam.WebcamDevice;
import com.github.sarxos.webcam.ds.buildin.WebcamDefaultDriver;
import com.github.sarxos.webcam.ds.ipcam.IpCamDevice;
import com.github.sarxos.webcam.ds.ipcam.IpCamDeviceRegistry;
import com.github.sarxos.webcam.ds.ipcam.IpCamDriver;
import com.github.sarxos.webcam.ds.ipcam.IpCamMode;

public class CameraManager {

	private static boolean initialized = false;
	
	static {
		initialize();
	}
	
	public static void initialize() {
		if (initialized)
			return;
		initialized = true;
		// allows us to use both built-in webcams and IP cameras
		WebcamCompositeDriver wcd = new WebcamCompositeDriver();
		wcd.add(new WebcamDefaultDriver());
		wcd.add(new IpCamDriver());
		Webcam.setDriver(wcd);
	}
	
	public static Webcam createIPCamera(URL url, IpCamMode mode) throws IOException {
		// check that we can actually connect
		url.openConnection().connect();
		// create the webcam device
		IpCamDevice ipcd = new IpCamDevice(url.toString(), url, IpCamMode.PUSH);
		ipcd.setFailOnError(true);
		ipcd.client.getParams().setIntParameter("http.socket.timeout", 100);
		ipcd.getImage();
		IpCamDeviceRegistry.register(ipcd);
		return findWebcam(ipcd);
	}
	
	public static Webcam findWebcam(WebcamDevice wd) {
		for(Webcam cam : Webcam.getWebcams()) {
			if (cam.getDevice() == wd) {
				return cam;
			}
		}
		return null;
	}
		
}
