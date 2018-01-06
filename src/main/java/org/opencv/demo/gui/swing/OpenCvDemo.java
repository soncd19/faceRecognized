package org.opencv.demo.gui.swing;

import org.opencv.demo.core.RecognizerType;
import org.opencv.demo.gui.Utils;
import org.opencv.demo.misc.Constants;
import org.opencv.demo.misc.SwingLogger;
import org.opencv.demo.core.DetectorsManager;
import org.opencv.core.Core;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.*;
import java.awt.event.KeyEvent;


public class OpenCvDemo extends JFrame {

    static final long serialVersionUID = 0;
    private CameraPanel cameraPanel;

    private JTextArea consoleLog;

    private SwingLogger logger = new SwingLogger(this);
    private DetectorsManager detectorsManager = new DetectorsManager(logger);

    public OpenCvDemo() throws Exception {
        super(Constants.OPENCV_FACE_RECOGNIZED);

        UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");

        setSize(800, 800);
        Utils.centerFrame(this);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // menus
        setJMenuBar(createMenuBar());

        // main panels
        consoleLog = new JTextArea(Constants.OPENCVDEMO_COMPLETE);
        consoleLog.setEditable(false);
        cameraPanel = new CameraPanel(logger, detectorsManager);
        cameraPanel.startCamera();

        JScrollPane consoleScrollPane = new JScrollPane(consoleLog, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        consoleScrollPane.setWheelScrollingEnabled(true);

        // Tao 1 divider chia giao dien thanh 2 phan la camera va phan in log
        JSplitPane spDivider = new JSplitPane(JSplitPane.VERTICAL_SPLIT, cameraPanel, consoleScrollPane);
        spDivider.setDividerLocation(600);
        spDivider.setOneTouchExpandable(true);
        getContentPane().add(spDivider, BorderLayout.CENTER);

        setVisible(true);

        //Khoi tao mac dinh nhan dien khuon mat khi camera duoc bat len
        setDetectorsDefault();
    }

    private JMenuBar createMenuBar() throws Exception {

        JMenuBar menuBar = new JMenuBar();

        // Khoi tao thanh menu thoat
        JMenu menu = new JMenu(Constants.EXIT);
        menuBar.add(menu);
        menu.setMnemonic(KeyEvent.VK_F);
        menu.addMenuListener(menuExitListener);

        menu = new JMenu(Constants.FACE_RECOGNIZED);
        menuBar.add(menu);
        menu.setMnemonic(KeyEvent.VK_R);
        JMenu recognizerSubMenu = new JMenu(Constants.RECOGNIZED_TYPE);
        ButtonGroup buttonGroup = new ButtonGroup();
        for (RecognizerType type : RecognizerType.values()) {
            JRadioButtonMenuItem menuItem = new JRadioButtonMenuItem(type.toString());
            menuItem.addActionListener(e -> changeRecognizer(type));
            recognizerSubMenu.add(menuItem);
            buttonGroup.add(menuItem);
        }

        menu.add(recognizerSubMenu);
        menu.add(new JSeparator());

        final JMenuItem recItem = new JMenuItem(Constants.START);
        recItem.setMnemonic(KeyEvent.VK_S);

        recItem.addActionListener(event -> {

            boolean isActive = detectorsManager.changeRecognizerStatus();
            recItem.setText(isActive ? Constants.STOP : Constants.START);

            int counter = isActive ? 1 : 0;
            detectorsManager.setColors(Constants.DEFAULT_FACE_CLASSIFIER, counter);
        });

        menu.add(recItem);
        menuBar.add(menu);

        menu = new JMenu(Constants.CAPTURE_NEW_USER);
        menuBar.add(menu);
        menu.addMenuListener(menuNewUserListener);

        return menuBar;
    }

    /**
     * Khi an vao menu nay se hien thi giao dien de chup anh nguoi dung moi
     */
    private MenuListener menuNewUserListener = new MenuListener() {
        @Override
        public void menuSelected(MenuEvent menuEvent) {
            if (checkForClassifier()) {
                UserCaptureDialog userCaptureDialog = new UserCaptureDialog(detectorsManager);
                userCaptureDialog.setVisible(true);
            }
        }

        @Override
        public void menuDeselected(MenuEvent menuEvent) {
        }

        @Override
        public void menuCanceled(MenuEvent menuEvent) {
        }
    };

    private MenuListener menuExitListener = new MenuListener() {
        @Override
        public void menuSelected(MenuEvent menuEvent) {
            isExit();
        }

        @Override
        public void menuDeselected(MenuEvent menuEvent) {
        }

        @Override
        public void menuCanceled(MenuEvent menuEvent) {
        }
    };


    /**
     * Ham khoi tao mac dinh phat hien khuon mat khi giao dien load len
     */
    private void setDetectorsDefault() {
        detectorsManager.clear();
        detectorsManager.addDetector(Constants.DEFAULT_FACE_CLASSIFIER, 0);
    }

    /**
     * Dialog thong bao khi chup 1 nguoi dung moi
     *
     * @return
     */
    private boolean checkForClassifier() {

        if (!detectorsManager.hasDetector(Constants.DEFAULT_FACE_CLASSIFIER)
                || detectorsManager.getDetectors().size() != 1) {
            int resp = JOptionPane.showConfirmDialog(this, Constants.DIALOG_MESSAGE,
                    Constants.SET_FACE_CLASSIFIER, JOptionPane.YES_NO_OPTION);

            if (resp == JOptionPane.YES_OPTION) {
                detectorsManager.clear();
                detectorsManager.addDetector(Constants.DEFAULT_FACE_CLASSIFIER, 3);
            } else {
                return false;
            }
        }
        return true;
    }

    /**
     * Dialog thong bao thoat chuong trinh
     *
     * @return
     */
    private boolean isExit() {
        int resp = JOptionPane.showConfirmDialog(this, Constants.DIALOG_EXIT_MESSAGE,
                Constants.DIALOG_EXIT_BAR, JOptionPane.YES_NO_OPTION);

        if (resp == JOptionPane.YES_OPTION) {
            System.exit(0);
        } else {
            return false;
        }
        return false;
    }

    private void changeRecognizer(RecognizerType recognizerType) {
        detectorsManager.changeRecognizer(recognizerType);
    }

    public void consoleLog(String message) {
        consoleLog.append("\n" + message);
        consoleLog.repaint();
    }

    public static void main(String[] args) throws Exception {
        System.out.println(System.getProperty("java.library.path"));
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        new OpenCvDemo();
    }
}
