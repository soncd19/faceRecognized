package org.opencv.demo.core;

import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.demo.misc.Constants;
import org.opencv.demo.misc.ImageUtils;
import org.opencv.demo.misc.Loggable;
import org.opencv.demo.misc.SwingLogger;
import org.opencv.face.FaceRecognizer;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.File;
import java.io.FilenameFilter;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

public class RecognizerManager {

    private List images;
    private MatOfInt labelsBuffer;
    private FaceRecognizer faceRecognizer;
    Map<Integer, String> idToNameMapping = null;
    private int[] labels;
    private double[] confidence;


    public RecognizerManager() throws Exception {
        //Khoi tao mac dinh dung FISHER de nhan dien khuon mat
        faceRecognizer = RecognizerFactory.getRecognizer(RecognizerType.FISHER);

        processImage();
        trainRecognizer();

        labels = new int[idToNameMapping.size()];
        confidence = new double[idToNameMapping.size()];

    }

    public void trainRecognizer() {
        faceRecognizer.train(images, labelsBuffer);
    }

    public void reTrainRecognizer() throws Exception {
        processImage();
        trainRecognizer();
    }

    private void processImage() throws Exception {
        URL urlImageTrain = ClassLoader.getSystemResource(Constants.TRAINING_FACES_PATH);
        File imageTrain = null;
        try {
            imageTrain = new File(urlImageTrain.toURI());

            File[] imageFiles = ImageUtils.getImagesFiles(imageTrain);
            idToNameMapping = createSummary(imageFiles);

            images = new ArrayList(imageFiles.length);
            labelsBuffer = new MatOfInt(new int[imageFiles.length]);

            int counter = 0;
            for (File image : imageFiles) {

                // doc hinh anh duoc dao tao theo mau xam tao ra 1 Mat
                Mat img = Imgcodecs.imread(image.getAbsolutePath(), Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);

                // Lay id trong bang mapping
                int labelId = ImageUtils.getIdFromImage(image.getName(), idToNameMapping);

                // Dua tat ca anh vao mot List
                images.add(img);
                labelsBuffer.put(counter++, 0, labelId);
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void changeRecognizer(FaceRecognizer faceRecognizer) {
        this.faceRecognizer = faceRecognizer;
        trainRecognizer();
    }

    public RecognizedFace recognizeFace(Mat face) {

        if (face == null) {
            return Constants.UNKNOWN_FACE;
        }

        Mat resizedGrayFace = ImageUtils.toGrayScale(ImageUtils.resizeFace(face));
        faceRecognizer.predict(resizedGrayFace, labels, confidence);

        if (confidence[0] < Constants.FACE_RECOGNITION_THRESHOLD) {
            return new RecognizedFace(idToNameMapping.get(labels[0]), confidence[0]);
        }

        return Constants.UNKNOWN_FACE;
    }

    /**
     * Khoi tao mot map chua cac anh kem id
     * Muc dich la de doc cac file anh ma co ten dau truoc dau "_" giong nhau thi se
     * xem nhu cac anh do cung 1 id. Va id trong ham nay se tu tang theo so luong anh co ten khac nhau
     *
     * @param imagesFiles
     * @return
     */
    private Map<Integer, String> createSummary(File[] imagesFiles) {

        Map<Integer, String> idToNameMapping = new HashMap<>();
        int idCounter = 0;
        for (File imageFile : imagesFiles) {
            String name = imageFile.getName().split("_")[0];
            if (!idToNameMapping.values().contains(name)) {
                idToNameMapping.put(idCounter++, name);
            }
        }

        return idToNameMapping;
    }
}
