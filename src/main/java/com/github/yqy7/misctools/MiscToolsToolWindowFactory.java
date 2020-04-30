package com.github.yqy7.misctools;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory.SERVICE;
import com.intellij.ui.content.ContentManager;
import org.jetbrains.annotations.NotNull;

public class MiscToolsToolWindowFactory implements ToolWindowFactory {
    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        ContentManager contentManager = toolWindow.getContentManager();

        EncodeDecodeGui encodeDecodeGui = new EncodeDecodeGui();
        Content encodeDecodeContent = SERVICE.getInstance().createContent(encodeDecodeGui.getRootPanel(), "Encode&Decode", false);
        contentManager.addContent(encodeDecodeContent);

        CharsetGui charsetGui = new CharsetGui();
        Content charsetContent = SERVICE.getInstance().createContent(charsetGui.getRootPanel(), "Charset", false);
        contentManager.addContent(charsetContent);

        NumeralSystemGui numeralSystemGui = new NumeralSystemGui();
        Content numeralSystemContent = SERVICE.getInstance().createContent(numeralSystemGui.getRootPanel(), "Numeral System", false);
        contentManager.addContent(numeralSystemContent);

        QRCodeGui qrCodeGui = new QRCodeGui();
        Content qrCodeContent = SERVICE.getInstance().createContent(qrCodeGui.getRootPanel(), "QRCode", false);
        contentManager.addContent(qrCodeContent);
    }
}
