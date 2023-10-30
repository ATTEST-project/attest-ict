package com.attest.ict.service;

import com.attest.ict.custom.exception.KillProcessException;
import java.io.IOException;

public interface CommandExecutorService {
    int launchTool(String workingDir, String configFilePath, String logFilePath, String launcherFileName, Long timeOutInSecs, Long taskId)
        throws IOException, InterruptedException;

    boolean killProcessByTaskId(Long taskId, Process process) throws KillProcessException;
    Process getProcessByTaskId(Long taskId);
}
