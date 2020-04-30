package com.github.yqy7.misctools;

import java.util.function.UnaryOperator;

import javax.swing.*;

public class NumeralSystemGui {
    private JPanel rootPanel;

    private JTextArea inputArea;
    private JTextArea outputArea;
    private JButton integerButton;
    private JButton floatButton;
    private JRadioButton unsetRadioButton;
    private JRadioButton a32BitRadioButton;
    private JRadioButton a64BitRadioButton;

    public NumeralSystemGui() {
        integerButton.addActionListener(e -> {
            convert(s -> {
                StringBuilder stringBuilder = new StringBuilder();
                Long num = Util.decodeLong(s);
                // 二进制
                stringBuilder.append("0b");
                if (unsetRadioButton.isSelected()) {
                    stringBuilder.append(Long.toBinaryString(num));
                } else if (a32BitRadioButton.isSelected()) {
                    stringBuilder.append(Util.addPrefixZero(Long.toBinaryString(num), 32));
                } else {
                    stringBuilder.append(Util.addPrefixZero(Long.toBinaryString(num), 64));
                }
                stringBuilder.append("\n");
                // 八进制
                stringBuilder.append("0");
                stringBuilder.append(Long.toOctalString(num));
                stringBuilder.append("\n");
                // 十进制
                stringBuilder.append(Long.toString(num));
                stringBuilder.append("\n");
                // 十六进制
                stringBuilder.append("0x");
                stringBuilder.append(Long.toHexString(num));
                stringBuilder.append("\n");

                return stringBuilder.toString();
            });
        });

        floatButton.addActionListener(e -> {
            convert(s -> {
                StringBuilder stringBuilder = new StringBuilder();

                if (unsetRadioButton.isSelected() || a64BitRadioButton.isSelected()) {
                    // 64位: 1,11,52
                    double num = Double.parseDouble(s);
                    long rawLongBits = Double.doubleToRawLongBits(num);
                    stringBuilder.append(Util.addPrefixZero(Long.toBinaryString(rawLongBits), 64));
                    stringBuilder.insert(1, ',');
                    stringBuilder.insert(13, ',');

                    appendInfo(stringBuilder, 1023);
                } else {
                    // 32位: 1,8,23
                    float num = Float.parseFloat(s);
                    int rawIntBits = Float.floatToRawIntBits(num);
                    stringBuilder.append(Util.addPrefixZero(Integer.toBinaryString(rawIntBits), 32));
                    stringBuilder.insert(1, ',');
                    stringBuilder.insert(10, ',');

                    appendInfo(stringBuilder, 127);
                }
                return stringBuilder.toString();
            });
        });
    }

    private void appendInfo(StringBuilder stringBuilder, int bias) {
        String numStr = stringBuilder.toString();
        String[] split = numStr.split(",");
        long exponent = Util.decodeLong("0b" + split[1]) - bias;
        stringBuilder.append("\n阶码: ");
        stringBuilder.append(exponent);

        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append("1");
        stringBuilder1.append(split[2]);
        if (exponent >= 0) {
            stringBuilder1.insert((int)exponent + 1, ".");
        } else {
            int t = (int)-exponent;
            for (int i = 0; i < t; i++) {
                stringBuilder1.insert(0, "0");
            }
            stringBuilder1.insert(1, ".");
        }
        stringBuilder.append("\n尾数转换后: ");
        stringBuilder.append(stringBuilder1);
    }

    private void convert(UnaryOperator<String> converter) {
        String inputText = inputArea.getText();
        String outputText = "";
        if (!inputText.isEmpty()) {
            outputText = converter.apply(inputText);
        }
        outputArea.setText(outputText);
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }
}
