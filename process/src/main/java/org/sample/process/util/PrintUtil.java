package org.sample.process.util;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.sample.process.cmd.CmdOutput;

@Slf4j
public class PrintUtil {


  public static <T> void print(T... args) {
    if (args != null && args.length != 0) {
      for (T t : args) {
        log.info("{}", t);
      }
    }
  }

  public static void print(List<? extends CmdOutput> list) {
    if (list != null && list.size() != 0) {
      for (CmdOutput cmdOutput : list) {
        log.info("Command : " + cmdOutput.getCmd());
        print(cmdOutput.getOutput());
        log.info("\n\n");
      }
    }
  }

  public static <T extends CmdOutput> void print(T... args) {
    if (args != null && args.length != 0) {
      for (CmdOutput cmdOutput : args) {
        log.info("Command : " + cmdOutput.getCmd());
        print(cmdOutput.getOutput());
        log.info("\n\n");
      }
    }
  }
}
