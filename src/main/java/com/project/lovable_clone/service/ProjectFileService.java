package com.project.lovable_clone.service;

import com.project.lovable_clone.dto.project.FileContentResponse;
import com.project.lovable_clone.dto.project.FileNode;

import java.util.List;

public interface ProjectFileService {

    FileContentResponse getFileContent(Long projectId, String path);

    List<FileNode> getFileTree(Long projectId);

    void saveFiles(Long projectId, String filePath, String fileContent);
}
