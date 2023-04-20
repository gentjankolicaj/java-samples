package org.sample.bytecode.cmd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CmdUtil {

  static final Runtime runtime = Runtime.getRuntime();

  static String buildCmd(String[] cmdArray) {
    StringBuilder cmdOutput = new StringBuilder();
    if (cmdArray == null || cmdArray.length == 0) {
      throw new RuntimeException("Can't build cmd.Command null");
    }
    cmdOutput.append(cmdArray[0]);
    for (int i = 1; i < cmdArray.length; i++) {
      cmdOutput.append(" -" + cmdArray[i]);
    }
    return cmdOutput.toString();
  }

  static String buildCmd(String cmd, String fullClassFileName) {
    return cmd + " " + fullClassFileName;
  }

  static CmdOutput execute(String preparedCmd) {
    CmdOutput cmdOutput = new CmdOutput(preparedCmd, null);
    BufferedReader br = null;
    try {
      StringBuilder stringBuilder = new StringBuilder();
      Process process = runtime.exec(preparedCmd);
      InputStream inputStream = process.getInputStream();
      br = new BufferedReader(new InputStreamReader(inputStream));
      String line;
      while ((line = br.readLine()) != null) {
        stringBuilder.append(line);
        stringBuilder.append(",");
      }
      process.waitFor();
      process.destroy();

      //prepare return object
      cmdOutput.setOutput(stringBuilder.toString().split(","));
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        if (br != null) {
          br.close();
        }
      } catch (IOException io) {
        io.printStackTrace();
      }
    }
    return cmdOutput;
  }

  public static List<CmdOutput> readBytecode(String[] cmdArray, String[] classes) {
    String cmd = buildCmd(cmdArray);
    if (classes == null || classes.length == 0) {
      throw new RuntimeException("Can't build cmd.No classes provided");
    }

    int classNumber = classes.length;
    String[] preparedCmdArray = new String[classNumber];
    for (int i = 0; i < classNumber; i++) {
      preparedCmdArray[i] = buildCmd(cmd, classes[i]);
    }
    List<CmdOutput> cmdOutputs = new ArrayList<>();
    for (String preparedCmd : preparedCmdArray) {
      cmdOutputs.add(execute(preparedCmd));
    }
    return cmdOutputs;
  }


}
