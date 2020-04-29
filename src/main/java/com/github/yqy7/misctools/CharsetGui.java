package com.github.yqy7.misctools;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.function.UnaryOperator;

import javax.swing.*;

import com.intellij.icons.AllIcons;

public class CharsetGui {
    private JTextArea inputArea;
    private JTextArea outputArea;
    private JButton exchangeButton;
    private JPanel rootPanel;
    private JButton encodeUnicodeButton;
    private JButton decodeUnicodeButton;
    private JButton encodeUTF16Button;
    private JButton decodeUTF16Button;
    private JButton encodeUTF8Button;
    private JButton ASCIIButton;
    private JButton URLEncodeButton;
    private JButton URLDecodeButton;

    public CharsetGui() {
        exchangeButton.setIcon(AllIcons.Ide.UpDown);
        exchangeButton.setMinimumSize(new Dimension(30,30));
        exchangeButton.setPreferredSize(new Dimension(30,30));
        exchangeButton.addActionListener(e -> {
            String text = inputArea.getText();
            inputArea.setText(outputArea.getText());
            outputArea.setText(text);
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

        encodeUnicodeButton.addActionListener(e -> {
            convert(s -> {
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < s.codePointCount(0, s.length()); i++) {
                    int codePoint = s.codePointAt(i);
                    stringBuilder.append("\\u");
                    stringBuilder.append(Util.addPrefixZero(Integer.toHexString(codePoint), 4));
                }
                return stringBuilder.toString();
            });
        });

        decodeUnicodeButton.addActionListener(e -> {
            convert(s -> {
                String[] split = s.split("\\\\u");
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < split.length; i++) {
                    String numStr = split[i].trim();
                    if (numStr.isEmpty()) continue;

                    // 这个既可以从codePoint转换，也可以从代理对转换。如 \u2aeab 和 \ud86b\udeab 都可以转换成: 𪺫
                    // Character.toChars 对 codePoint 进行了判断，如果是基本平面的字符转换为1char，如果是扩展平面的字符转换为2char
                    // 如果需要和下面 UTF16 做区分，可以判断是否在基本平面 (即 codePoint < 0x10000)，在基本平面的不符合输入要求
                    stringBuilder.append(Character.toChars(Integer.parseInt(numStr, 16)));
                }
                return stringBuilder.toString();
            });
        });

        encodeUTF16Button.addActionListener(e -> {
            convert(s -> {
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < s.codePointCount(0, s.length()); i++) {
                    int codePoint = s.codePointAt(i);
                    if (Character.isBmpCodePoint(codePoint)) { // 基本平面的字符
                        stringBuilder.append("\\u");
                        stringBuilder.append(Util.addPrefixZero(Integer.toHexString(codePoint), 4));
                    } else { // 代理对
                        // unicode 编码范围 0~0x10FFFF，即32位中实际只有后20位有效
                        // 20位中的前10位 + 0xD800(0b1101 1000 0000 0000‬) => ((codePoint >> 10) & 0x3ff) + 0xd800
                        stringBuilder.append("\\u");
                        stringBuilder.append(Util.addPrefixZero(Integer.toHexString(Character.highSurrogate(codePoint)), 4));
                        // 20位中的后10位 + 0xDC00(0b‭1101 1100 0000 0000‬) => (codePoint & 0x3ff) + 0xdc00
                        stringBuilder.append("\\u");
                        stringBuilder.append(Util.addPrefixZero(Integer.toHexString(Character.lowSurrogate(codePoint)), 4));
                    }
                }
                return stringBuilder.toString();
            });
        });

        decodeUTF16Button.addActionListener(e -> {
            convert(s -> {
                String[] split = s.split("\\\\u");
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < split.length; i++) {
                    String numStr = split[i].trim();
                    if (numStr.isEmpty()) continue;

                    // 只能从代理对转换，看上面 decodeUnicodeButton
                    stringBuilder.append(Integer.parseInt(numStr, 16));
                }

                return stringBuilder.toString();
            });
        });

        encodeUTF8Button.addActionListener(e -> {
            convert(s -> {
                // 转换成 utf-8
                byte[] bytes = s.getBytes(Charset.forName("UTF-8"));
                StringBuilder hexStringBuilder = new StringBuilder();
                StringBuilder binStringBuilder = new StringBuilder();
                for (int i = 0; i < bytes.length; i++) {
                    hexStringBuilder.append(Util.addPrefixZero(Integer.toHexString(Byte.toUnsignedInt(bytes[i])), 2));
                    hexStringBuilder.append(" ");

                    binStringBuilder.append(Util.addPrefixZero(Integer.toBinaryString(Byte.toUnsignedInt(bytes[i])), 8));
                    binStringBuilder.append(" ");
                }

                hexStringBuilder.append("\n");
                hexStringBuilder.append(binStringBuilder);
                return hexStringBuilder.toString();
            });
        });

        ASCIIButton.addActionListener(e -> {
            convert(s -> {
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < s.codePointCount(0, s.length()); i++) {
                    int codePoint = s.codePointAt(i);
                    if (codePoint < '\u0080') {
                        stringBuilder.append((char)codePoint);
                    } else if (Character.isBmpCodePoint(codePoint)) {
                        stringBuilder.append("\\u");
                        stringBuilder.append(Util.addPrefixZero(Integer.toHexString(codePoint), 4));
                    } else if (Character.isValidCodePoint(codePoint)){
                        stringBuilder.append("\\u");
                        stringBuilder.append(Util.addPrefixZero(Integer.toHexString(Character.highSurrogate(codePoint)), 4));
                        stringBuilder.append("\\u");
                        stringBuilder.append(Util.addPrefixZero(Integer.toHexString(Character.lowSurrogate(codePoint)), 4));
                    }
                }
                return stringBuilder.toString();
            });
        });

        URLEncodeButton.addActionListener(e -> {
            convert(s -> {
                String res = "";
                try {
                    URL url = new URL(s);
                    // 用URI一个参数的构造函数会有问题，处理不了 file://///10.10.10.10/Yev Pri - Ru─▒n G├╢z├╝yle Ortado─Яu.pdf
                    // 用URI 3个参数以上的构造函数都
                    URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(),
                        url.getQuery(), null);
                    res = uri.toASCIIString();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                return res;
            });
        });

        URLDecodeButton.addActionListener(e -> {
            convert(s -> {
                String res = "";
                try {
                    res = URLDecoder.decode(s, "UTF-8");
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                return res;
            });
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
