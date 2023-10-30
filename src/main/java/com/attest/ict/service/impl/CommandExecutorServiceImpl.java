package com.attest.ict.service.impl;

import com.attest.ict.custom.exception.KillProcessException;
import com.attest.ict.custom.utils.FileUtils;
import com.attest.ict.service.CommandExecutorService;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CommandExecutorServiceImpl implements CommandExecutorService {

    private final Logger log = LoggerFactory.getLogger(CommandExecutorServiceImpl.class);
    protected final Map<Long, Process> processMap = new HashMap<>();

    protected final ReadWriteLock lock = new ReentrantReadWriteLock();

    @Override
    public int launchTool(
        String toolInstallationDir,
        String configFilePath,
        String logFilePath,
        String launcherFileName,
        Long timeOutInSecs,
        Long taskId
    ) throws IOException, InterruptedException {
        Long timeOut = (timeOutInSecs != null) ? timeOutInSecs : -1;

        log.info(
            "Enter launchTool() with arguments: [toolInstallationDir: {}, configFilePath: {}, logFile:{}, launchFile: {}, timeOutInSecs: {} taskId: {} ]",
            toolInstallationDir,
            configFilePath,
            logFilePath,
            launcherFileName,
            timeOutInSecs,
            taskId
        );
        String launcherFilePath = toolInstallationDir.concat(File.separator).concat(launcherFileName);
        String[] commandLineParams = { launcherFilePath, configFilePath };
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command(commandLineParams);
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();
        log.info("START batch script- Process PID :{}  ", process.pid());
        try {
            lock.writeLock().lock();
            processMap.put(taskId, process);
        } finally {
            lock.writeLock().unlock();
        }

        FileUtils.writeToolLogsToFile(new InputStreamReader(process.getInputStream()), logFilePath);

        int exitCode = -1;
        if (timeOut <= 0) {
            exitCode = process.waitFor();
        } else {
            boolean isTerminated = process.waitFor(timeOut, TimeUnit.SECONDS);
            if (isTerminated) {
                exitCode = process.exitValue();
                log.info("END batch script - Process PID: {} , exitCode: {} ", process.pid(), exitCode);
            }
        }

        process.getInputStream().close();
        process.getOutputStream().close();
        process.getErrorStream().close();
        log.info("Exit: launchTool() Process_PID: {},  taskId: {} exitCode: {},", process.pid(), taskId, exitCode);
        return exitCode;
    }

    @Override
    public boolean killProcessByTaskId(Long taskId, Process process) throws KillProcessException {
        log.info("Enter: killProcessByTaskId() - TaskId: {} ", taskId);
        boolean isTerminated = false;
        try {
            ProcessHandle processHandle = process.toHandle();
            long pid = processHandle.pid();
            log.info("Parent Process PID: {}, INFO: {}, isAlive:{} ", pid, processHandle.info(), processHandle.isAlive());
            if (processHandle.isAlive()) {
                this.printProcessTree(processHandle);
                int exitCode = killProcessAndChildren(pid);
                isTerminated = (exitCode == 0);
            } else {
                log.info("Process PID: {}, is NOT Alive! ", pid);
            }
            log.info("Exit: killProcessByTaskId() isTerminated: {} ", isTerminated);
            return isTerminated;
        } catch (IOException | InterruptedException e) {
            log.error("Error: killProcessByTaskId() TaskId: " + taskId + ", raise this exception: " + e.getMessage());
            throw new KillProcessException("Error in killProcessByTaskId() for taskId: " + taskId, e);
        }
    }

    private int killProcessAndChildren(long processId) throws IOException, InterruptedException {
        log.info("Enter: killProcessAndChildren() processId: {}", processId);
        // Execute the taskkill command to stop the process using its PID, this parameter allow to kill all process' s children
        ProcessBuilder processBuilder = new ProcessBuilder("taskkill", "/F", "/T", "/PID", String.valueOf(processId));
        log.info("Ready to kill Process PID: {} and all children", processId);
        Process processTaskKill = processBuilder.start();
        int exitCode = processTaskKill.waitFor();
        processBuilder.redirectErrorStream(true);
        // Capture output
        BufferedReader outputReader = new BufferedReader(new InputStreamReader(processTaskKill.getInputStream()));
        String outputLine;
        while ((outputLine = outputReader.readLine()) != null) {
            log.info(outputLine);
        }
        if (exitCode == 0) {
            log.info("Process PID: {}  terminated successfully.", processId);
        } else {
            log.error("Process PID: {} termination failed. Exit code: {}" + processId, exitCode);
        }
        log.info("Exit: killProcessAndChildren() with exitCode: {}", exitCode);
        return exitCode;
    }

    /**
     * Print info about all children process of the parent process
     * @param parentProcess Parent Process
     */
    private void printProcessTree(ProcessHandle parentProcess) {
        Stream<ProcessHandle> children = parentProcess.children();
        children.forEach(childProcess -> {
            log.info("Child Process PID: {}, INFO: {} : " + childProcess.info());
            printProcessTree(childProcess);
        });
    }

    @Override
    public Process getProcessByTaskId(Long taskId) {
        try {
            lock.readLock().lock();
            return processMap.get(taskId);
        } finally {
            lock.readLock().unlock();
        }
    }
}
