package application;

import java.net.URL;

import javax.swing.JFrame;

import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.OpenCVFrameConverter.ToOrgOpenCvCoreMat;
import org.bytedeco.javacv.VideoInputFrameGrabber;
import org.opencv.core.Core;
import org.opencv.core.Mat;

import javafx.application.Application;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;

public class Main extends Application {
	int previousSound = 0;
	AudioClip clip = new AudioClip(getClass().getResource("/BB-9E-SFX/recording1.wav").toString());

	@Override
	public void start(Stage primaryStage) {

		startMotionDetector();
	}

	public static void main(String[] args) {
		launch(args);
	}

	public void startMotionDetector() {
		CanvasFrame canvas = new CanvasFrame("Old Mat"), canvas2 = new CanvasFrame("New Mat");
		canvas2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
						if (Core.norm(mat) > 8000) {
							if (!clip.isPlaying()) {
								new Thread(new Runnable() {

									@Override
									public void run() {
										playRandomSound();
									}
								}).start();
							}
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

	public void playRandomSound() {
		int i = (int) Math.floor(Math.random() * 19 + 1);
		while (i == previousSound)
			i = (int) Math.floor(Math.random() * 19 + 1);

		previousSound = i;

		System.out.println("i = " + i);
		final URL url = getClass().getResource("/BB-9E-SFX/recording" + i + ".wav");
		System.out.println(url);
		final String path = url.toString();

		clip = new AudioClip(path);
		clip.play(clip.getVolume(), -1.0, clip.getRate(), clip.getPan(), clip.getPriority());
	}
}
