package org.sample.process.util;

import org.sample.process.cmd.CmdOutput;

import java.util.List;

public class PrintUtil {


    public static <T> void print(T...args){
        if(args!=null && args.length!=0){
            for(T t:args)
                System.out.println(t);
        }
    }

    public static void print(List<? extends CmdOutput> list){
        if(list!=null && list.size()!=0){
            for(CmdOutput cmdOutput:list){
                System.out.println("Command : "+cmdOutput.getCmd());
                print(cmdOutput.getOutput());
                System.out.println("\n\n");
            }
        }
    }

    public static <T extends CmdOutput> void print(T... args){
        if(args!=null && args.length!=0){
            for(CmdOutput cmdOutput:args){
                System.out.println("Command : "+cmdOutput.getCmd());
                print(cmdOutput.getOutput());
                System.out.println("\n\n");
            }
        }
    }
}
