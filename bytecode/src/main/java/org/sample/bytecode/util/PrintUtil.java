package org.sample.bytecode.util;

import org.sample.bytecode.cmd.CmdOutput;

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
            for(CmdOutput output:list){
                System.out.println("Command : "+output.getCmd());
                print(output.getOutputArray());
                System.out.println("\n\n\n");
            }
        }
    }
}
