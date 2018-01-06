package org.opencv.demo.core;

import org.opencv.face.Face;
import org.opencv.face.FaceRecognizer;

public class RecognizerFactory {

    public static FaceRecognizer getRecognizer(RecognizerType recognizerType) {

        switch (recognizerType) {
            case EIGEN:
                return Face.createEigenFaceRecognizer();
            case FISHER:
                return Face.createFisherFaceRecognizer();
            case LBPH:
                return Face.createLBPHFaceRecognizer();
        }

        throw new IllegalArgumentException("Recognizer " + recognizerType + " not found.");
    }
}
