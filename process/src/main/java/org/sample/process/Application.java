package org.sample.process;

import org.sample.process.cmd.CmdOutput;
import org.sample.process.cmd.CmdUtil;
import org.sample.process.util.PrintUtil;

/**
 * gentjan kolicaj
 */
public class Application {

  public static void main(String[] args) {
    CmdOutput lsOutput = CmdUtil.execute(true, "ls", "-alh");
    CmdOutput pwdOutput = CmdUtil.execute(true, "pwd");
    CmdOutput unameOutput = CmdUtil.execute(true, "uname", "-a");
    PrintUtil.print(lsOutput, pwdOutput, unameOutput);

    CmdOutput lsOutput1 = CmdUtil.execute(false, "ls", "-alh");
    CmdOutput pwdOutput1 = CmdUtil.execute(false, "pwd");
    PrintUtil.print(lsOutput1, pwdOutput1);
  }
}
