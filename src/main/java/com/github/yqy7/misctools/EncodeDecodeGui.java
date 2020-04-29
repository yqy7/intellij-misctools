package com.github.yqy7.misctools;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.function.UnaryOperator;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.intellij.icons.AllIcons;

public class EncodeDecodeGui {
    private JPanel rootPanel;

    private JTextArea inputArea;
    private JTextArea outputArea;
    private JButton base64EncodeButton;
    private JButton base64DecodeButton;
    private JButton MD5_32Button;
    private JButton MD5_16Button;
    private JButton SHA1Button;
    private JButton exchangeButton;
    private JLabel outputLengthLabel;
    private JButton SHA256Button;

    public EncodeDecodeGui() {
        outputArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                outputLengthLabel.setText(String.valueOf(outputArea.getText().length()));
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                outputLengthLabel.setText(String.valueOf(outputArea.getText().length()));
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                outputLengthLabel.setText(String.valueOf(outputArea.getText().length()));
            }
        });

        outputArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    outputArea.selectAll();

                    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                    StringSelection stringSelection = new StringSelection(outputArea.getText());
                    clipboard.setContents(stringSelection, null);
                }
            }
        });

        exchangeButton.setIcon(AllIcons.Ide.UpDown);
        exchangeButton.setMinimumSize(new Dimension(30,30));
        exchangeButton.setPreferredSize(new Dimension(30,30));
        exchangeButton.addActionListener(e -> {
            String text = inputArea.getText();
            inputArea.setText(outputArea.getText());
            outputArea.setText(text);
        });

        base64EncodeButton.addActionListener(e -> {
            convert(Base64.getEncoder()::encode);
        });
        base64DecodeButton.addActionListener(e -> {
            convert(Base64.getDecoder()::decode);
        });
        MD5_32Button.addActionListener(e -> {
            convert(this::md5_32);
        });
        MD5_16Button.addActionListener(e -> {
            convert(this::md5_16);
        });
        SHA1Button.addActionListener(e -> {
            convert(this::sha1);
        });
        SHA256Button.addActionListener(e -> {
            convert(this::sha256);
        });

    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    private byte[] md5_32(byte[] bytes) {
        return digest(bytes, "MD5").getBytes();
    }

    private byte[] md5_16(byte[] bytes) {
        return digest(bytes, "MD5").substring(8, 24).getBytes();
    }

    private byte[] sha1(byte[] bytes) {
        return digest(bytes, "SHA-1").getBytes();
    }

    private byte[] sha256(byte[] bytes) {
        return digest(bytes, "SHA-256").getBytes();
    }

    private String digest(byte[] bytes, String algorithm) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            byte[] digestBytes = messageDigest.digest(bytes);
            return getHexString(digestBytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    private String getHexString(byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            stringBuilder.append(Util.addPrefixZero(Integer.toHexString(Byte.toUnsignedInt(bytes[i])), 2));
        }
        return stringBuilder.toString();
    }

    private void convert(UnaryOperator<byte[]> converter) {
        String inputFieldText = inputArea.getText();
        String outputFieldText = "";
        try {
            if (!inputFieldText.isEmpty()) {
                outputFieldText = new String(converter.apply(inputFieldText.getBytes()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        outputArea.setText(outputFieldText);
    }

}
