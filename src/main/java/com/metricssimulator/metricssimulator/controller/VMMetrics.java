package com.metricssimulator.metricssimulator.controller;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class VMMetrics {

    private List<Integer> systemUnreachableGauge = new ArrayList<>();
    private List<Integer> instanceUnreachableGauge = new ArrayList<>();
    private List<Integer> instanceStatusUnavailableGauge = new ArrayList<>();
    private AtomicLong cpuUtilization = new AtomicLong();
    private AtomicInteger networkInBytes = new AtomicInteger(0);
    private AtomicInteger networkOutBytes = new AtomicInteger(0);

    public VMMetrics(String vmId, MeterRegistry meterRegistry) {
        Gauge.builder("vm-" + vmId +".system-reachability-failed", systemUnreachableGauge, Collection::size)
                .description("Virtual Machine [" + vmId + " System Reachability Status")
                .register(meterRegistry);

        Gauge.builder("vm-" + vmId +".instance-reachability-failed", instanceUnreachableGauge, Collection::size)
                .description("Virtual Machine [" + vmId + " Instance Reachability Status")
                .register(meterRegistry);

        Gauge.builder("vm-" + vmId +".unavailable", instanceStatusUnavailableGauge, Collection::size)
                .description("Virtual Machine [" + vmId + " Instance Running Status")
                .register(meterRegistry);

        Gauge.builder("vm-" + vmId + ".cpuUtilization-utilization", cpuUtilization, AtomicLong::doubleValue)
                .description("Virtual Machine [" + vmId + " CPU Utilization")
                .register(meterRegistry);

        Gauge.builder("vm-" + vmId +".network-in", networkInBytes, AtomicInteger::intValue)
                .description("Virtual Machine [" + vmId + " NetworkIn")
                .register(meterRegistry);

        Gauge.builder("vm-" + vmId +".network-out", networkOutBytes, AtomicInteger::intValue)
                .description("Virtual Machine [" + vmId + " NetworkOut")
                .register(meterRegistry);

    }

    void setSystemReachable(boolean isHealthy) {
        if(isHealthy) {
            systemUnreachableGauge.clear();
        } else {
            if(systemUnreachableGauge.size() == 0) systemUnreachableGauge.add(1);
        }
    }

    void setInstanceReachable(boolean isHealthy) {
        if(isHealthy) {
            instanceUnreachableGauge.clear();
        } else {
            if(instanceUnreachableGauge.size() == 0) instanceUnreachableGauge.add(1);
        }
    }

    void setInstanceCpuUtilization(Double percentage) {
        cpuUtilization.set(percentage.longValue());
    }

    void setNetworkIn(Integer bytes) {
        networkInBytes.set(bytes);
    }

    void setNetworkOut(Integer bytes) {
        networkOutBytes.set(bytes);
    }

    public void setAvailable(boolean isAvailable) {
        if(isAvailable) {
            instanceStatusUnavailableGauge.clear();
        } else {
            if(instanceStatusUnavailableGauge.size() == 0) instanceStatusUnavailableGauge.add(1);
        }
    }
}
