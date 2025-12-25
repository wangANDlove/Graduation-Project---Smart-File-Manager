package com.smartfilemanager.service.core;

import java.io.IOException;
import java.nio.file.*;

public class FileMonitorService {
    private WatchService watcher;//创建WatchService实例
    {
        try {
            watcher = FileSystems.getDefault().newWatchService();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    Path dir= Paths.get("");


}
