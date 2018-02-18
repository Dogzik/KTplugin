package com.example;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MyToolWindowFactory implements ToolWindowFactory {
    private JButton refreshToolWindowButton;
    private JButton hideToolWindowButton;
    private JLabel sourcesStats;
    private JLabel testSourceStats;
    private JPanel myToolWindowContent;
    private ToolWindow myToolWindow;
    private ProjectRootManager manager;
    private ProjectFileIndex index;

    public MyToolWindowFactory() {
        hideToolWindowButton.addActionListener(e -> myToolWindow.hide(null));
        refreshToolWindowButton.addActionListener(e -> MyToolWindowFactory.this.updateStats());
    }

    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        myToolWindow = toolWindow;
        manager = ProjectRootManager.getInstance(project);
        index = manager.getFileIndex();
        this.updateStats();
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(myToolWindowContent, "", false);
        toolWindow.getContentManager().addContent(content);
    }

    private void updateStats() {
        VirtualFile[] roots = manager.getContentSourceRoots();
        FileVisitor visitor = new FileVisitor();
        for (VirtualFile file : roots) {
            visitor.isTest = index.isInTestSourceContent(file) ? 1 : 0;
            try {
                Files.walkFileTree(Paths.get(file.getPath()), visitor);
            } catch (IOException e) {
                System.err.println("Unable to process project");
                return;
            }
        }

        int[] kt = visitor.getKTSources();
        int[] all = visitor.getSources();
        int src = (all[0] > 0) ? (kt[0] * 100 / all[0]) : 0;
        int test = (all[1] > 0) ? (kt[1] * 100 / all[1]) : 0;

        sourcesStats.setText(".kt source files: " + Integer.toString(src) + "%");
        testSourceStats.setText(".kt test source files: " + Integer.toString(test) + "%");
    }
}