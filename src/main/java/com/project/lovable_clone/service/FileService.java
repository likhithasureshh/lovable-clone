package com.project.lovable_clone.service;

import com.project.lovable_clone.dto.project.FileContentResponse;
import com.project.lovable_clone.dto.project.FileNode;

import java.util.List;

public interface FileService {

    FileContentResponse getFileContent(Long projectId, String path, Long userId);

    List<FileNode> getFileTree(Long projectId, Long userId);
}
