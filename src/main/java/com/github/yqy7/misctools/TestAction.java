package com.github.yqy7.misctools;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;

public class TestAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        //// 获取jdk
        Project project = e.getProject();
        //Sdk sdk = ProjectRootManager.getInstance(project).getProjectSdk();
        //System.out.println(sdk.getName());
        //System.out.println(sdk.getHomePath());
        //System.out.println(sdk.getHomeDirectory());
        //System.out.println(sdk.getSdkType());
        //System.out.println(sdk.getVersionString());
        //System.out.println(sdk.getSdkModificator());
        //System.out.println(sdk.getRootProvider());
        //System.out.println(sdk.getSdkAdditionalData());

        //Document document = e.getData(PlatformDataKeys.EDITOR).getDocument();
        //System.out.println(FileDocumentManager.getInstance().getFile(document).getName());
        //
        //PsiFile psiFile = e.getData(LangDataKeys.PSI_FILE);
        //System.out.println(psiFile.getFileType());

        //PropertiesComponent instance = PropertiesComponent.getInstance(project);
        //instance.setValue("hahaha", 1234,0);
        //System.out.println(instance.getInt("hahaha",99));
        //instance.setValue("hahaha",1234,1234);
    }
}
