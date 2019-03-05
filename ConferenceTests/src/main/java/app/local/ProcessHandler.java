package app.local;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ProcessHandler {
    public String getCpuUsageByProcessName(String name) throws IOException {
        String[] inputArr = {"ps", "-em", "-o", "%cpu,command"};
        String retVal = executeTerminalCommands(inputArr, name);
        return returnSubStringAccordingStr(retVal, " /").replace(" ", "");
    }

    public String getMemoryUsageByProcessName(String name) throws IOException {
        String[] inputArr = {"ps", "-em", "-o", "%mem,command"};
        String retVal = executeTerminalCommands(inputArr, name);
        return returnSubStringAccordingStr(retVal, " /").replace(" ", "");
    }

    private String executeTerminalCommands(String[] inputArr, String commandPart) throws IOException {
        String line;
        Process process = Runtime.getRuntime().exec(inputArr);
        BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
        while ((line = input.readLine()) != null) {
            if (line.contains(commandPart))
            {
                return line;
            }
        }
        return "-1";
    }

    private String returnSubStringAccordingStr(String fullStr, String str){
        int index = fullStr.indexOf(str);
        return fullStr.substring(0, index);
    }
}
