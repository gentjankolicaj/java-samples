package org.sample.bytecode;

import org.sample.bytecode.classfile.ClassFileUtil;
import org.sample.bytecode.cmd.CmdOutput;
import org.sample.bytecode.cmd.CmdUtil;
import org.sample.bytecode.util.PrintUtil;


import java.util.List;

/**
 *  gentjan kolicaj
 *
 */
public class Main {

    public static void main( String[] args ) {
        final String[] cmd=new String[]{"javap","c"};
        String spartanClassFile= ClassFileUtil.getClassFilePath("Spartan.class");
        String humanClassFile=ClassFileUtil.getClassFilePath("Human.class");

        List<CmdOutput> cmdOutputs= CmdUtil.readBytecode(cmd,new String[]{spartanClassFile,humanClassFile});
        PrintUtil.print(cmdOutputs);
    }

}
