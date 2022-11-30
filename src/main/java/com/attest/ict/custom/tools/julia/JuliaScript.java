package com.attest.ict.custom.tools.julia;

import com.attest.ict.tools.exception.RunningToolException;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JuliaScript {

    private static final Logger LOGGER = LoggerFactory.getLogger(JuliaScript.class);

    public static int execute(String filePath) {
        try {
            // command from cmd/terminal -> julia "/path/to/julia/file.jl"
            // julia should be in PATH, otherwise replace "julia.exe" with julia path in your file system
            String[] params = { "julia.exe", filePath };

            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.command(params);
            processBuilder.redirectErrorStream(true);

            Process process = processBuilder.start();

            // read and log output of the process in console
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (!(line.contains("Warning") || line.contains("@ "))) {
                    LOGGER.info(line);
                }
            }
            return process.exitValue();
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static Process executeProcess(String filePath) throws Exception {
        try {
            // command from cmd/terminal -> julia "/path/to/julia/file.jl"
            // julia should be in PATH, otherwise replace "julia.exe" with julia path in your file system
            String[] params = { "julia.exe", filePath };

            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.command(params);
            processBuilder.redirectErrorStream(true);

            Process process = processBuilder.start();

            return process;
        } catch (IOException e) {
            throw new Exception("Exception execute Julia Process for file: " + filePath, e);
        }
    }

    public static Process executeBatchFile(String toolWorkingDir, String jsonConfigFilePath) throws RunningToolException {
        try {
            String runToolBatchFile = toolWorkingDir.concat(File.separator).concat("launch.bat");
            String[] params = { runToolBatchFile, jsonConfigFilePath };
            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.command(params);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            return process;
        } catch (IOException e) {
            throw new RunningToolException("Exception execute  Process for file: " + toolWorkingDir, e);
        }
    }
}
