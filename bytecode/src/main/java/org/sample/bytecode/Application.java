package org.sample.bytecode;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.security.CodeSource;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.sample.bytecode.cmd.CmdOutput;
import org.sample.bytecode.cmd.CmdUtil;
import org.sample.bytecode.halo.Human;
import org.sample.bytecode.halo.Spartan;
import org.sample.bytecode.util.PrintUtil;

@Slf4j
public class Application {

  public static void main(String[] args) throws URISyntaxException {
    CodeSource codeSource = Application.class.getProtectionDomain().getCodeSource();

    if (codeSource != null) {
      URL location = codeSource.getLocation();
      log.info("Found location with CodeSource: {}", Paths.get(location.toURI()).toAbsolutePath());
    }

    final String[] cmd = new String[]{"javap", "c"};
    String spartanClassFile = Objects.requireNonNull(Spartan.class.getResource("Spartan.class")).getPath();
    String humanClassFile = Objects.requireNonNull(Human.class.getResource("Human.class")).getPath();

    List<CmdOutput> cmdOutputs = CmdUtil.readBytecode(cmd, new String[]{spartanClassFile, humanClassFile});
    PrintUtil.print(cmdOutputs);
  }

}
