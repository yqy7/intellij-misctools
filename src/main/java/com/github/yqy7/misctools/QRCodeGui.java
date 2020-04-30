package com.github.yqy7.misctools;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import javax.swing.*;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

public class QRCodeGui {
    private static final int imgWidth = 200;
    private static final int imgHeight = 200;
    private BufferedImage bufferedImage;

    private JPanel rootPanel;
    private JTextArea inputField;
    private JButton genQRCodeButton;
    private JPanel imgPanel;

    public QRCodeGui() {
        genQRCodeButton.addActionListener(e -> {
            String inputText = inputField.getText();
            if (inputText.isEmpty()) {
                bufferedImage = null;
                imgPanel.updateUI();
                return;
            }

            try {
                Map<EncodeHintType, Object> hints = new HashMap<>();
                hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
                BitMatrix bitMatrix = new MultiFormatWriter().encode(inputText, BarcodeFormat.QR_CODE, imgWidth, imgHeight, hints);
                bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
                imgPanel.updateUI();
            } catch (WriterException writerException) {
                writerException.printStackTrace();
            }
        });

    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    private void createUIComponents() {
        imgPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                if (bufferedImage == null) {
                    return;
                }

                g.drawImage(bufferedImage, (getWidth() - imgWidth) / 2, 0,null);
            }
        };
    }
}
