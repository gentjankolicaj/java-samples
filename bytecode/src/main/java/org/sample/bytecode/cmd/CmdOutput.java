package org.sample.bytecode.cmd;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CmdOutput {

  private String cmd;
  private String[] output;
}
