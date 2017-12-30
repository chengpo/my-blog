package com.monkeyapp.blog;

import java.io.File;

public class PathHelper {
    private final String rootPath;

    PathHelper(String rootPath) {
        this.rootPath = rootPath;
    }

    public String realPostPath(String fileName) {
        return String.format("%smd/posts/%s", rootPath, fileName).replace("/", File.separator);
    }

    public String realPagePath(String fileName) {
        return String.format("%smd/pages/%s", rootPath, fileName).replace("/", File.separator);
    }
}
