package com.github.yqy7.misctools;

import java.util.function.UnaryOperator;

import javax.swing.*;

import com.intellij.openapi.ui.Messages;

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
            Messages.showInfoMessage("功能开发中","Info");
        });
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
