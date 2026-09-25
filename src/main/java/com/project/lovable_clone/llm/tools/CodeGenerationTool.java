package com.project.lovable_clone.llm.tools;

import com.project.lovable_clone.service.ProjectFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class CodeGenerationTool {
    private final ProjectFileService projectFileService;
    private final Long projectId;

    @Tool(name = "read_files",
            description = """
                     Read the contents of existing project files.
                    
                             Only provide relative paths that exist in FILE_TREE.
                    
                             Use this tool only when the actual source code is needed.
                             Read all relevant files in one call whenever possible.
                             Do not repeatedly read the same file.
                             Once enough information has been obtained, stop using this tool
                             and generate the implementation.
                 
                    """)
    public List<String> readFiles(
            @ToolParam(description = "List of relative paths (e.g., ['src/App.tsx'])")
            List<String> paths
    ) {
        {
            List<String> result = new ArrayList<>();
            for (String path : paths) {
                String cleanPath = path.startsWith("/") ? path.substring(1) : path;
                String content = projectFileService.getFileContent(projectId, cleanPath).content();
                result.add(String.format(
                        "--- START OF FILE: %s ---\n%s\n--- END OF FILE ---",
                        cleanPath, content
                ));
            }
            return result;
        }
    }
}
