import javax.swing.JFrame;

import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.VideoInputFrameGrabber;

public class Hello {
	public static void main(String[] args) {
		CanvasFrame canvas = new CanvasFrame("Web Cam");
		canvas.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		try (FrameGrabber grabber = new VideoInputFrameGrabber(0);) {
			grabber.start();
			Frame img;
			while (true) {
				img = grabber.grab();
				if (img != null) {
					canvas.showImage(img);
				}
				// Thread.sleep(INTERVAL);
			}
		} catch (Exception e) {
		}
	}
}