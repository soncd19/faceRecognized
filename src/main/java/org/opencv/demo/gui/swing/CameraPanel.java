package org.opencv.demo.gui.swing;

import org.opencv.demo.misc.ImageUtils;
import org.opencv.demo.misc.Loggable;
import org.opencv.demo.core.DetectorsManager;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

import javax.swing.*;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class CameraPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private BufferedImage imageToDisplay;
    private Loggable logger;
    private DetectorsManager detectorsManager;

    public CameraPanel(Loggable logger, DetectorsManager detectorsManager) {
        this.logger = logger;
        this.detectorsManager = detectorsManager;
    }

    public void startCamera() {
        new PanelUpdater().execute();
        logger.log("Started camera.");
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (imageToDisplay == null) {
            return;
        }
        g.drawImage(this.imageToDisplay, 1, 1, this.imageToDisplay.getWidth(), this.imageToDisplay.getHeight(), null);
    }

    public void updateImage(Mat image) {
        imageToDisplay = ImageUtils.getBufferedImageFromMat(image);
        repaint();
    }

    private class PanelUpdater extends SwingWorker {

        private final VideoCapture camera;
        private Mat capturedImage = new Mat();

        public PanelUpdater() {

            // Khoi dong camera va do phan giai
            camera = new VideoCapture(0);
            camera.set(Videoio.CV_CAP_PROP_FRAME_WIDTH, 352);
            camera.set(Videoio.CV_CAP_PROP_FRAME_HEIGHT, 288);
        }

        @Override
        protected Object doInBackground() throws Exception {

            while (!isCancelled()) {

                // Doc anh tu camera
                camera.read(capturedImage);

                if (!capturedImage.empty()) {

                    // Duoc hinh anh sau khi detect hoac recognized
                    capturedImage = detectorsManager.detect(capturedImage);

                    // Ve lai hinh anh len camera
                    updateImage(capturedImage);
                }
            }
            return null;
        }
    }
}
