package com.github.yqy7.misctools;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.UnaryOperator;

import javax.swing.*;

public class MiscGui {
    private JPanel rootPanel;
    private JTextArea inputArea;
    private JTextArea outputArea;
    private JButton javaMatrixButton;

    public MiscGui() {
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

        javaMatrixButton.addActionListener(e -> convert(s -> {
            s = s.replace("[", "{");
            s = s.replace("]", "}");
            return s;
        }));

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
