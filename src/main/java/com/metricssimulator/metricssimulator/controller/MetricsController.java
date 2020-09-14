package com.metricssimulator.metricssimulator.controller;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/metrics")
public class MetricsController {

    private MeterRegistry meterRegistry;

    private List<Integer> transitGatewayStatusUnavailable = new ArrayList<>();
    private List<Integer> globalProtectStatusDisconnected = new ArrayList<>();
    private Map<String, VMMetrics> registeredVMs = new HashMap<>();

    public MetricsController(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;

        Gauge.builder("transitgateway.unavailable", transitGatewayStatusUnavailable, Collection::size)
                .description("Transit Gateway Unavailable Status")
                .register(meterRegistry);

        Gauge.builder("globalprotect.disconnected", globalProtectStatusDisconnected, Collection::size)
                .description("Global Protect Disconnected Status")
                .register(meterRegistry);

    }

    @GetMapping("/transitgateway-unavailable")
    public String transitgatewayUnavailable() {
        if(transitGatewayStatusUnavailable.size() == 0) {
            transitGatewayStatusUnavailable.add(1);
        }
        return "Transit Gateway status set to unavailable";
    }

    @GetMapping("/transitgateway-available")
    public String transitgatewayAvailable() {
        transitGatewayStatusUnavailable.clear();
        return "Transit Gateway status set to available";
    }

    @GetMapping("/globalprotect-disconnected")
    public String globalProtectAttached() {
        if(globalProtectStatusDisconnected.size() == 0) {
            globalProtectStatusDisconnected.add(1);
        }
        return "Global Protect status set to disconnected";
    }

    @GetMapping("/globalprotect-attached")
    public String globalProtectDisconnected() {
        globalProtectStatusDisconnected.clear();
        return "Global Protect status set to attached";
    }

    @GetMapping("/register-vm")
    public String registerVM(@RequestParam("vm-id") String vmId) {
        if(vmId == null || vmId.isEmpty()) return "Please supply a query parameter 'vm-id' to register a Virtual Machine";

        if(!registeredVMs.containsKey(cleanVMIdInput(vmId))) registeredVMs.put(vmId, new VMMetrics(vmId, this.meterRegistry));
        return "Virtual Machine [" + vmId + "] Registered";
    }

    @GetMapping("/vm-state-stopped-terminated")
    public String vmStateStopped(@RequestParam("vm-id") String vmId) {
        registeredVMs.get(cleanVMIdInput(vmId)).setAvailable(false);
        return "Virtual Machine [" + vmId + "] Status is set to Unavailable";
    }

    @GetMapping("/vm-state-running")
    public String vmStateRunning(@RequestParam("vm-id") String vmId) {
        registeredVMs.get(cleanVMIdInput(vmId)).setAvailable(true);
        return "Virtual Machine [" + vmId + "] Status is set to Running";
    }

    @GetMapping("/vm-system-reachability-passed")
    public String vmSystemReachabilityPassed(@RequestParam("vm-id") String vmId) {
        registeredVMs.get(cleanVMIdInput(vmId)).setSystemReachable(true);
        return "Virtual Machine [" + vmId + "] System Reachability set to Passed";
    }

    @GetMapping("/vm-system-reachability-failed")
    public String vmSystemReachabilityFailed(@RequestParam("vm-id") String vmId) {
        registeredVMs.get(cleanVMIdInput(vmId)).setSystemReachable(false);
        return "Virtual Machine [" + vmId + "] System Reachability set to Failed";
    }

    @GetMapping("/vm-instance-reachability-passed")
    public String vmInstanceReachabilityPassed(@RequestParam("vm-id") String vmId) {
        registeredVMs.get(cleanVMIdInput(vmId)).setInstanceReachable(true);
        return "Virtual Machine [" + vmId + "] Instance Reachable set to Passed";
    }

    @GetMapping("/vm-instance-reachability-failed")
    public String vmInstanceReachabilityFailed(@RequestParam("vm-id") String vmId) {
        registeredVMs.get(cleanVMIdInput(vmId)).setInstanceReachable(false);
        return "Virtual Machine [" + vmId + "] Instance Reachable set to Failed";
    }

    @GetMapping("/vm-cpu")
    public String vmCpuUtilisation(@RequestParam("vm-id") String vmId, @RequestParam("percentage") Double percentage) {
        registeredVMs.get(cleanVMIdInput(vmId)).setInstanceCpuUtilization(percentage);
        return "Virtual Machine [" + vmId + "] CPU Utilization [" + percentage  + "] event captured";
    }

    @GetMapping("/vm-network-in")
    public String vmNetworkIn(@RequestParam("vm-id") String vmId, @RequestParam("bytes") Integer bytes) {
        registeredVMs.get(cleanVMIdInput(vmId)).setNetworkIn(bytes);
        return "Virtual Machine [" + vmId + "] Network In event captured";
    }

    @GetMapping("/vm-network-out")
    public String vmNetworkOut(@RequestParam("vm-id") String vmId, @RequestParam("bytes") Integer bytes) {
        registeredVMs.get(cleanVMIdInput(vmId)).setNetworkOut(bytes);
        return "Virtual Machine [" + vmId + "] Network Out event captured";
    }

    private String cleanVMIdInput(String vmId) {
        return vmId.trim().toLowerCase();
    }
}
