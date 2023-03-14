package org.sample.bytecode.util;

import lombok.extern.slf4j.Slf4j;
import org.sample.bytecode.cmd.CmdOutput;

import java.util.List;


@Slf4j

public class PrintUtil {


    public static <T> void print(T... args) {
        if (args != null && args.length != 0) {
            for (T t : args)
                log.info("{}", t);
        }
    }

    public static void print(List<? extends CmdOutput> list){
        if(list!=null && list.size()!=0){
            for(CmdOutput output:list){
                log.info("Command : " + output.getCmd());
                print(output.getOutput());
                log.info("\n\n");
            }
        }
    }
}
