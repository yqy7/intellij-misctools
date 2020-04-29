package com.github.yqy7.misctools;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

public class AllIconsToolWindowFactory implements ToolWindowFactory {
    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        AllIconGui allIconGui = new AllIconGui();
        Content iconContent = ContentFactory.SERVICE.getInstance().createContent(allIconGui.getRootPanel(), "AllIcons", true);
        toolWindow.getContentManager().addContent(iconContent);
    }
}
