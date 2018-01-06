package org.opencv.demo.core;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.demo.misc.Constants;
import org.opencv.demo.misc.Loggable;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.*;

public class DetectorsManager {

    private Scalar[] colors;
    private Loggable logger;
    private Map<String, ElementsDetector> detectors = new HashMap<>();
    private RecognizerManager recognizerManager;

    private boolean isRecActive = false;

    public DetectorsManager(RecognizerManager recognizerManager, Loggable logger) {
        this.recognizerManager = recognizerManager;
        this.logger = logger;
        init();
    }

    public DetectorsManager(Loggable logger) throws Exception {
        this.logger = logger;
        this.recognizerManager = new RecognizerManager();
        init();
    }

    public void init() {

        // Khai bao mau sac cho khung phat hien
        colors = new Scalar[]{
                new Scalar(0, 255, 0),
                new Scalar(0, 0, 255),
        };
    }

    public Mat detect(Mat capturedImage) {

        List<DetectedElement> detectedElements;

        // Vong lap vo han phat hien khuon mat
        for (ElementsDetector detector : detectors.values()) {

            // Lay ra phan tu detect trong mang detecttors va detect image truyen vao tu camera
            detectedElements = detector.detectElements(capturedImage);
            for (DetectedElement detectedElement : detectedElements) {

                // Lay hinh anh da duoc chuyen doi sau khi detect
                capturedImage = detectedElement.getTransformedImage();

                // Kiem tra xem co dang trong qua trinh nhan dien khuon mat hay khong
                if (isRecActive && detectedElement != null && detectedElement.getDetectedImageElement() != null) {

                    assert (detectors.size() == 1 && detectors.containsKey(Constants.DEFAULT_FACE_CLASSIFIER));

                    // Bat dau nhan dien khuon mat
                    RecognizedFace recognizedFace = recognizerManager.recognizeFace(detectedElement.getDetectedImageElement());
                    String name;

                    //Neu khong Nhan dien duoc khuon mat thi tra ve unknown
                    if (recognizedFace == Constants.UNKNOWN_FACE) {
                        name = recognizedFace.getName();
                    } else {
                        int percentage = (int) (100 * (Constants.FACE_RECOGNITION_THRESHOLD - recognizedFace.getConfidence()) / Constants.FACE_RECOGNITION_THRESHOLD);
                        name = recognizedFace.getName() + "  [" + percentage + "%]";
                    }

                    // Viet ten khuon mat phat hien duoc len khung phat hien
                    Point position = detectedElement.getPosition();
                    position.y -= 11;
                    position.x -= 1;
                    Imgproc.putText(capturedImage, name, position, Core.FONT_HERSHEY_TRIPLEX, Constants.RECOGNIZED_NAME_FONT_SIZE, Constants.BLACK);

                    position.y += 1;
                    position.x += 1;
                    Imgproc.putText(capturedImage, name, position, Core.FONT_HERSHEY_TRIPLEX, Constants.RECOGNIZED_NAME_FONT_SIZE, colors[1]);
                }
            }
        }

        return capturedImage;
    }

    /**
     * Them 1 loai detecttor(o day lay doc tu haarcascades)
     * @param detectorName
     * @param counter
     */
    public void addDetector(String detectorName, int counter) {
        ElementsDetector detector = new ElementsDetector(detectorName, logger, colors[counter]);
        detectors.put(detectorName, detector);
    }

    /**
     * Set mau sac cho khung nhan dien
     * @param detectorName
     * @param counter
     */
    public void setColors(String detectorName, int counter) {
        ElementsDetector detector = detectors.get(detectorName);
        detector.setColor(colors[counter]);
    }

    /**
     * Thay doi kieu nhan dien khuon mat
     * @param recognizerType
     */
    public void changeRecognizer(RecognizerType recognizerType) {
        if (isRecActive) {
            //changeRecognizerStatus();
            recognizerManager.changeRecognizer(RecognizerFactory.getRecognizer(recognizerType));
            //changeRecognizerStatus();
        } else {
            recognizerManager.changeRecognizer(RecognizerFactory.getRecognizer(recognizerType));
        }
    }

    public boolean hasDetector(String detectorName) {
        return detectors.values().stream().anyMatch(d -> d.getDetectorName().equals(detectorName));
    }

    public ElementsDetector getDetector(String name) {
        return detectors.get(name);
    }

    public Collection<ElementsDetector> getDetectors() {
        return detectors.values();
    }

    public void clear() {
        detectors.clear();
    }

    public boolean changeRecognizerStatus() {
        isRecActive = !isRecActive;
        return isRecActive;
    }
}
