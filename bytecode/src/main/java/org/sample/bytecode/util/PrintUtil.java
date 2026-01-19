package org.sample.bytecode.util;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.sample.bytecode.cmd.CmdOutput;


@Slf4j
public class PrintUtil {


  public static void print(List<? extends CmdOutput> list) {
    if (list != null && !list.isEmpty()) {
      for (CmdOutput output : list) {
        log.info("Command : {}", output.getCmd());
        String[] outputValues = output.getOutput();
        if (outputValues != null) {
          for (String outputValue : outputValues) {
            log.info("{}", outputValue);
          }
        }
        log.info("\n\n");
      }
    }
  }

}
