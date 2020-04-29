package com.github.yqy7.misctools;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.*;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.ui.Messages;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.uiDesigner.core.GridConstraints;
import org.apache.commons.lang.StringUtils;

public class AllIconGui {
    private JPanel rootPanel;
    private JTextField filterTextField;
    private JButton filterButton;
    private JPanel contentPanel;
    private Map<String, Icon> icons = new LinkedHashMap<>();
    private Map<String, JLabel> iconLabels = new LinkedHashMap<>();
    private final JPanel imgPanel = new JPanel();

    public AllIconGui() {
        try {
            addIcons(AllIcons.class);

            for (Entry<String, Icon> entry : icons.entrySet()) {
                Icon icon = entry.getValue();
                JLabel label = new JLabel(icon);
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setPreferredSize(new Dimension(50, 50));
                label.setToolTipText(entry.getKey());
                label.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        String iconRef = entry.getKey();
                        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                        StringSelection stringSelection = new StringSelection(iconRef);
                        clipboard.setContents(stringSelection, null);
                        Messages.showInfoMessage(iconRef, "Icon path");
                    }
                });
                iconLabels.put(entry.getKey(), label);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        GridConstraints gridConstraints = new GridConstraints();
        gridConstraints.setFill(GridConstraints.FILL_BOTH);
        contentPanel.add(new JBScrollPane(imgPanel), gridConstraints);
        updateImgPanel();

        filterButton.addActionListener(e -> {
            updateImgPanel();
        });

        filterTextField.addActionListener(e -> {
            updateImgPanel();
        });
    }

    private void updateImgPanel() {
        Map<String, JLabel> iconLabelMap = new LinkedHashMap<>();
        String filterText = filterTextField.getText();
        if (filterText.isEmpty()) {
            iconLabelMap = iconLabels;
        } else {
            for (Entry<String, JLabel> entry : iconLabels.entrySet()) {
                if (StringUtils.containsIgnoreCase(entry.getKey(), filterText)) {
                    iconLabelMap.put(entry.getKey(), entry.getValue());
                }
            }
        }

        imgPanel.removeAll();

        int cols = 6;
        int rows = iconLabelMap.size() / cols + 1;
        GridLayout gridLayout = new GridLayout(rows, cols);
        imgPanel.setLayout(gridLayout);
        for (JLabel jLabel : iconLabelMap.values()) {
            imgPanel.add(jLabel);
        }

        imgPanel.updateUI();
    }

    private void addIcons(Class<?> clazz) throws IllegalAccessException {
        Field[] declaredFields = clazz.getDeclaredFields();
        addIcons(declaredFields, clazz);

        Class<?>[] declaredClasses = clazz.getDeclaredClasses();
        for (Class<?> declaredClass : declaredClasses) {
            if (Modifier.isStatic(declaredClass.getModifiers())) {
                addIcons(declaredClass);
            }
        }
    }

    private void addIcons(Field[] declaredFields, Class<?> clazz) throws IllegalAccessException {
        for (Field declaredField : declaredFields) {
            if (Modifier.isStatic(declaredField.getModifiers())
                && declaredField.getType().equals(Icon.class)) {
                String canonicalName = clazz.getCanonicalName();
                String packageName = clazz.getPackage().getName();
                icons.put(canonicalName.substring(packageName.length() + 1) + "." + declaredField.getName(),
                    (Icon)declaredField.get(null));
            }
        }
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }
}
