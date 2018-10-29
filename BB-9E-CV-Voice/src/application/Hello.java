package application;

import javax.swing.JFrame;

import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.OpenCVFrameConverter.ToOrgOpenCvCoreMat;
import org.bytedeco.javacv.VideoInputFrameGrabber;
import org.opencv.core.Core;
import org.opencv.core.Mat;

import javafx.scene.media.AudioClip;

public class Hello {
	public static void main(String[] args) {
		CanvasFrame canvas = new CanvasFrame("Old Mat"), canvas2 = new CanvasFrame("New Mat");
		canvas.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		Mat mat = new Mat(), oldMat = null, newMat = null;
		ToOrgOpenCvCoreMat converter = new ToOrgOpenCvCoreMat();

		try (FrameGrabber grabber = new VideoInputFrameGrabber(0);) {
			grabber.start();
			Frame img;
			while (true) {
				img = grabber.grab();
				if (img != null) {
					newMat = converter.convert(img);
					if (oldMat != null) {
						mat = oldMat;
						canvas2.showImage(converter.convert(newMat));

						Core.absdiff(newMat, oldMat, mat);
						if (Core.norm(mat) > 4000) {
							playRandomSound();
							System.out.println("trigger " + Core.norm(mat));
						}
						canvas.showImage(converter.convert(mat));
					}
					oldMat = newMat.clone();

				}
			}
		} catch (Exception e) {
		}
	}

	public static void playRandomSound() {
		int i = (int) Math.floor(Math.random() * 19 + 1);
		final String path = "BB-9E-SFX/recording" + i + ".wav";
		final AudioClip clip = new AudioClip(path);
		clip.play(clip.getVolume(), -1.0, clip.getRate(), clip.getPan(), clip.getPriority());
	}
}