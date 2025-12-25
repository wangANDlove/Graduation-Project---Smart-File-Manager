package com.smartfilemanager.service.core;

import com.smartfilemanager.config.AppConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ImportResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.*;

@Service
public class FileMonitorService {
    private WatchService watcher;//创建WatchService实例
    @Autowired
    private AppConfig appConfig;
    {
        try {
            watcher = FileSystems.getDefault().newWatchService();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private Path dir;

    // 初始化路径
    public FileMonitorService() {
        if (appConfig != null) {
            dir = Paths.get(appConfig.getWatchedPath());
        }
    }


}
