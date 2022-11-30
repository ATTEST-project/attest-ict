package com.attest.ict.tools;

import com.attest.ict.tools.exception.RunningToolException;
import java.io.File;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RunWrapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(RunWrapper.class);

    public static Process executeBatchFile(String toolWorkingDir, String jsonConfigFilePath, String launcherName)
        throws RunningToolException {
        try {
            String runToolBatchFile = toolWorkingDir.concat(File.separator).concat(launcherName);
            String[] params = { runToolBatchFile, jsonConfigFilePath };
            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.command(params);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            return process;
        } catch (IOException e) {
            throw new RunningToolException("Exception running tool ", e);
        }
    }
}
