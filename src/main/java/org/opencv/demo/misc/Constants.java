package org.opencv.demo.misc;

import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.demo.core.RecognizedFace;

public class Constants {

    public static final String OPENCV_FACE_RECOGNIZED = "OpenCV With Face Recognized";
    public static final String OPENCVDEMO = "OpenCv";
    public static final float VERSION = 0.1f;
    public static final String OPENCVDEMO_COMPLETE = OPENCVDEMO + " v" + VERSION;

    public static final int MINIMUM_TRAIN_SET_SIZE = 10;

    public static final String TRAINING_FACES_PATH = "faces";
    public static int TRAIN_FACE_IMAGE_HEIGHT = 140;
    public static int TRAIN_FACE_IMAGE_WIDTH = 140;
    public static Size TRAIN_FACE_IMAGE_SIZE = new Size( (double) TRAIN_FACE_IMAGE_WIDTH, (double)TRAIN_FACE_IMAGE_HEIGHT);
    public static double FACE_RECOGNITION_THRESHOLD = 1000;

    public static int MAX_IMAGES_NUMBER_FOR_TRAINING = 50;
    public static String DEFAULT_FACE_CLASSIFIER = "/data/haarcascades/haarcascade_frontalface_alt.xml";

    public static Scalar WHITE = new Scalar(255,255,255);
    public static Scalar BLACK = new Scalar(0,0,0);

    public static float RECOGNIZED_NAME_FONT_SIZE = 0.5f;
    public static final String NOT_RECOGNIZED_FACE = "unknown";
    public static final RecognizedFace UNKNOWN_FACE = new RecognizedFace(NOT_RECOGNIZED_FACE, 0d);

    public static final String EXIT = "Thoát";
    public static final String OK = "Ok";
    public static final String FACE_RECOGNIZED = "Face Recognized";
    public static final String STOP = "Stop";
    public static final String START = "Start";
    public static final String CAPTURE_NEW_USER = "Chụp người dùng mới";
    public static final String RECOGNIZED_TYPE = "Recognized type";
    public static final String DIALOG_MESSAGE = "Chức năng này cần phải thiết lập face classifier mặc định.\n" +
            "Bạn có muốn thiết lập?";
    public static final String SET_FACE_CLASSIFIER = "Thiết lập face classifier mặc định.";
    public static final String DIALOG_EXIT_MESSAGE = "Bạn có muốn thoát chương trình?";
    public static final String DIALOG_EXIT_BAR = "Thoát chương trình";
}
