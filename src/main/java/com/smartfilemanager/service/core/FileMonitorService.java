package com.smartfilemanager.service.core;

import com.smartfilemanager.config.AppConfig;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ImportResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.*;

@Service
public class FileMonitorService implements InitializingBean {
    private WatchService watcher;//创建WatchService实例
    @Autowired
    private AppConfig appConfig;
    private Path dir;
    private Thread moniterThread;
    private boolean isRunning = false;

    public FileMonitorService() throws IOException {
        // 初始化WatchService实例
        try {
            watcher = FileSystems.getDefault().newWatchService();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    @Override
    public void afterPropertiesSet(){
        // 初始化路径
        updateDirectory();
//        moniterThread = new Thread(this::startMonitoring);
//        moniterThread.setDaemon( true);
//        moniterThread.setName("FileMonitorService");
//        moniterThread.start();

    }
    public void updateDirectory() {
        if(appConfig!=null&&appConfig.getWatchedPath()!=null){
            dir = Paths.get(appConfig.getWatchedPath());
        }else {
            dir = Paths.get(System.getProperty("user.home"));
        }
        System.out.println("监控目录："+dir);
    }

    public void startMonitoring() {
        if(isRunning){
            System.out.println("FileMonitorService is already running.");
            return;
        }
        updateDirectory();
        try {
            dir.register(watcher,
                    StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_MODIFY,
                    StandardWatchEventKinds.ENTRY_DELETE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        isRunning = true;
        moniterThread = new Thread(this::monitorLoop);
        moniterThread.setDaemon(true);
        moniterThread.setName("FileMonitorService");
        moniterThread.start();
        System.out.println("监控服务已启动");

    }

    private void monitorLoop() {
        while (isRunning && !Thread.currentThread().isInterrupted()) {
            WatchKey key;
            try {
                key = watcher.take();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }

            for (WatchEvent<?> event : key.pollEvents()) {
                WatchEvent.Kind<?> kind = event.kind();
                if (kind == StandardWatchEventKinds.OVERFLOW) {
                    continue;
                }
                Path fileName = (Path) event.context();
                System.out.println(kind.name() + ": " + fileName);

                if (!key.reset()) {
                    break;
                }
            }
        }
    }
    public void stopMonitoring() {
        if (!isRunning) {
            System.out.println("FileMonitorService is not running.");
            return;
        }
        isRunning = false;
        if(moniterThread!=null){
            moniterThread.interrupt();
            System.out.println("监控服务已停止");
        }

    }


}
