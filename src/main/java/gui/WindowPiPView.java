package gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;

public class WindowPiPView {
    private JFrame pipFrame;
    private JLabel displayLabel;
    private Timer updateTimer;
    private LinkedList<BufferedImage> screenshotQueue;
    private int maxScreenshots = 10;
    private String targetWindowId; // This will be used for window region selection

    public WindowPiPView() {
        pipFrame = new JFrame("PiP View");
        displayLabel = new JLabel();
        pipFrame.add(displayLabel);
        pipFrame.setSize(300, 300);
        pipFrame.setAlwaysOnTop(true);

        screenshotQueue = new LinkedList<>();

        updateTimer = new Timer(1000, e -> captureAndUpdateWindowContent());
        updateTimer.start();
    }

    public void setTargetWindowId(String windowId) {
        this.targetWindowId = windowId; // Store the window ID for reference
        // Note: The actual capturing logic will use slurp for region selection
    }

    public void showPiPWindow() {
        pipFrame.setVisible(true);
    }



    private void updateContent(BufferedImage content) {
        if (content != null) {
            displayLabel.setIcon(new ImageIcon(content));
        }
    }

    private void captureAndUpdateWindowContent() {
        try {
            // Capture window content using grim, with slurp for region selection
            String cmd = "grim -g \"$(slurp)\" screenshot.png";
            ProcessBuilder processBuilder = new ProcessBuilder("/bin/bash", "-c", cmd);
            Process captureProcess = processBuilder.start();
            captureProcess.waitFor();

            InputStream inputStream = new FileInputStream("screenshot.png");
            BufferedImage capturedImage = ImageIO.read(inputStream);

            screenshotQueue.offer(capturedImage);

            while (screenshotQueue.size() > maxScreenshots) {
                BufferedImage removedScreenshot = screenshotQueue.poll();
                if (removedScreenshot != null) {
                    removedScreenshot.flush();
                }
            }

            updateContent(capturedImage);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
