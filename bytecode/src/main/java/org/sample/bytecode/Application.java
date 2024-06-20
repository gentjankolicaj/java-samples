package org.sample.bytecode;

import java.util.List;
import org.sample.bytecode.classfile.ClassFileUtil;
import org.sample.bytecode.cmd.CmdOutput;
import org.sample.bytecode.cmd.CmdUtil;
import org.sample.bytecode.util.PrintUtil;

public class Application {

  public static void main(String[] args) {
    final String[] cmd = new String[]{"javap", "c"};
    String spartanClassFile = ClassFileUtil.getClassFilePath("Spartan.class");
    String humanClassFile = ClassFileUtil.getClassFilePath("Human.class");

    List<CmdOutput> cmdOutputs = CmdUtil.readBytecode(cmd,
        new String[]{spartanClassFile, humanClassFile});
    PrintUtil.print(cmdOutputs);
  }

}
