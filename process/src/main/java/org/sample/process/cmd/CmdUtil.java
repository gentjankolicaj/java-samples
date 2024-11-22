package org.sample.process.cmd;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.sample.process.exception.CmdException;

public class CmdUtil {

  public static String prepareCmd(String cmd, String... options) throws CmdException {
    if (cmd == null || cmd.length() == 0) {
      throw new CmdException("Command " + cmd + " not valid.");
    }

    StringBuilder stringBuilder = new StringBuilder(cmd);
    if (options != null) {
      for (String option : options) {
        stringBuilder.append(" " + option);
      }
    }
    return stringBuilder.toString();
  }

  public static CmdOutput execute(boolean runtime, String cmd, String... options)
      throws CmdException {
    if (runtime) {
      return executeWithRuntime(cmd, options);
    } else {
      return executeWithProcessBuilder(cmd, options);
    }
  }

  public static CmdOutput executeWithProcessBuilder(String cmd, String... options)
      throws CmdException {
    String preparedCmd = prepareCmd(cmd, options);
    CmdOutput cmdOutput = new CmdOutput(preparedCmd, null);

    BufferedReader br = null;
    StringBuilder stringBuilder = null;
    try {
      String[] newCmdArray = new String[options.length + 1];
      newCmdArray[0] = cmd;
      if (options.length != 0) {
        System.arraycopy(options, 0, newCmdArray, 1, options.length);
      }
      ProcessBuilder processBuilder = new ProcessBuilder(newCmdArray);
      Process process = processBuilder.start();
      InputStream is = process.getInputStream();
      br = new BufferedReader(new InputStreamReader(is));
      stringBuilder = new StringBuilder();
      String line;
      while ((line = br.readLine()) != null) {
        stringBuilder.append(line);
        stringBuilder.append(",");
      }
      process.waitFor();
      process.destroy();

      //Build output
      cmdOutput.setOutput(stringBuilder.toString().split(","));

    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        if (br != null) {
          br.close();
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return cmdOutput;
  }

  public static CmdOutput executeWithRuntime(String cmd, String... options) throws CmdException {
    String preparedCmd = prepareCmd(cmd, options);
    CmdOutput cmdOutput = new CmdOutput(preparedCmd, null);

    BufferedReader br = null;
    StringBuilder stringBuilder = null;
    try {
      Process process = Runtime.getRuntime().exec(preparedCmd);
      InputStream is = process.getInputStream();
      br = new BufferedReader(new InputStreamReader(is));
      stringBuilder = new StringBuilder();
      String line;
      while ((line = br.readLine()) != null) {
        stringBuilder.append(line);
        stringBuilder.append(",");
      }
      process.waitFor();
      process.destroy();

      //Build output
      cmdOutput.setOutput(stringBuilder.toString().split(","));

    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        if (br != null) {
          br.close();
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return cmdOutput;
  }
}
