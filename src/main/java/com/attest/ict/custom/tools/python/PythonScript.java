package com.attest.ict.custom.tools.python;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PythonScript {

    private static final Logger LOGGER = LoggerFactory.getLogger(PythonScript.class);

    public static int execute(String filePath) {
        try {
            // get folder path to be used as directory for the process
            String folderPath = filePath.substring(0, filePath.lastIndexOf('/'));

            // get only file name to be run
            String file = filePath.substring(filePath.lastIndexOf('/') + 1);

            // command from cmd/terminal -> python "/path/to/python_file.py"
            // python should be in PATH, otherwise replace "python.exe" with python path in your file system
            String[] params = { "python.exe", file };

            ProcessBuilder processBuilder = new ProcessBuilder();

            // set folder path as main directory to run python file
            // this is to be sure that 'main.py' can find input files
            processBuilder.directory(new File(folderPath));

            processBuilder.command(params);
            processBuilder.redirectErrorStream(true);

            Process process = processBuilder.start();

            // read and log output of the process in console
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                LOGGER.info(line);
            }
            return process.exitValue();
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static Process executeProcess(String filePath) {
        try {
            // get folder path to be used as directory for the process
            String folderPath = filePath.substring(0, filePath.lastIndexOf('/'));

            // get only file name to be run
            String file = filePath.substring(filePath.lastIndexOf('/') + 1);

            // command from cmd/terminal -> python "/path/to/python_file.py"
            // python should be in PATH, otherwise replace "python.exe" with python path in your file system
            String[] params = { "python.exe", file };

            ProcessBuilder processBuilder = new ProcessBuilder();

            // set folder path as main directory to run python file
            // this is to be sure that 'main.py' can find input files
            processBuilder.directory(new File(folderPath));

            processBuilder.command(params);
            processBuilder.redirectErrorStream(true);

            Process process = processBuilder.start();

            return process;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
