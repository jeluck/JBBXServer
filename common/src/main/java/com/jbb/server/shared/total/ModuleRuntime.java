package com.jbb.server.shared.total;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;

import com.sun.management.UnixOperatingSystemMXBean;

public class ModuleRuntime {
    private long freeMemory;
    private long totalMemory;
    private long maxMemory;
    private int availableProcessors;
    private long maxFileDescriptorCount;
    private long openFileDescriptorCount;
    private int processCpuLoad = -1;
    private int systemCpuLoad = -1;

    public void snapshot() {
        Runtime runtime = Runtime.getRuntime();
        this.freeMemory = runtime.freeMemory();
        this.totalMemory = runtime.totalMemory();
        this.maxMemory = runtime.maxMemory();
        this.availableProcessors = runtime.availableProcessors();
        
        OperatingSystemMXBean osStats = ManagementFactory.getOperatingSystemMXBean();
        if(osStats instanceof UnixOperatingSystemMXBean) {
            UnixOperatingSystemMXBean unixStats = (UnixOperatingSystemMXBean)osStats;
            maxFileDescriptorCount = unixStats.getMaxFileDescriptorCount();
            openFileDescriptorCount = unixStats.getOpenFileDescriptorCount();
        }

        if(osStats instanceof com.sun.management.OperatingSystemMXBean) {
            com.sun.management.OperatingSystemMXBean sunOsStat = (com.sun.management.OperatingSystemMXBean)osStats;
            double cpuLoad = sunOsStat.getProcessCpuLoad();
            processCpuLoad = cpuLoad < 0.0 || cpuLoad > 1.0 ? -1 : (int)(cpuLoad * 100.0);
            cpuLoad = sunOsStat.getSystemCpuLoad();
            systemCpuLoad = cpuLoad < 0.0 || cpuLoad > 1.0 ? -1 : (int)(cpuLoad * 100.0);
        }
    }
    
    public long getFreeMemory() {
        return freeMemory;
    }
    public void setFreeMemory(long freeMemory) {
        this.freeMemory = freeMemory;
    }
    public long getTotalMemory() {
        return totalMemory;
    }
    public void setTotalMemory(long totalMemory) {
        this.totalMemory = totalMemory;
    }
    public long getMaxMemory() {
        return maxMemory;
    }
    public void setMaxMemory(long maxMemory) {
        this.maxMemory = maxMemory;
    }
    public int getAvailableProcessors() {
        return availableProcessors;
    }
    public void setAvailableProcessors(int availableProcessors) {
        this.availableProcessors = availableProcessors;
    }
    public long getMaxFileDescriptorCount() {
        return maxFileDescriptorCount;
    }
    public void setMaxFileDescriptorCount(long maxFileDescriptorCount) {
        this.maxFileDescriptorCount = maxFileDescriptorCount;
    }
    public long getOpenFileDescriptorCount() {
        return openFileDescriptorCount;
    }
    public void setOpenFileDescriptorCount(long openFileDescriptorCount) {
        this.openFileDescriptorCount = openFileDescriptorCount;
    }
    public int getProcessCpuLoad() {
        return processCpuLoad;
    }
    public void setProcessCpuLoad(int processCpuLoad) {
        this.processCpuLoad = processCpuLoad;
    }
    public int getSystemCpuLoad() {
        return systemCpuLoad;
    }
    public void setSystemCpuLoad(int systemCpuLoad) {
        this.systemCpuLoad = systemCpuLoad;
    }
}
