package com.project.lovable_clone.service;

import com.project.lovable_clone.dto.project.FileContentResponse;
import com.project.lovable_clone.dto.project.FileNode;
import com.project.lovable_clone.dto.project.FileTreeResponse;

import java.util.List;

public interface ProjectFileService {

    FileContentResponse getFileContent(Long projectId, String path);

    FileTreeResponse getFileTree(Long projectId);

    void saveFiles(Long projectId, String filePath, String fileContent);
}
