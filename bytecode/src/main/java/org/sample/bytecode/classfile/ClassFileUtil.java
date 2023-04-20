package org.sample.bytecode.classfile;

public class ClassFileUtil {

  public static String getClassFilePath(String classFilename) {
    return "bytecode/target/classes/org/sample/bytecode/halo/" + classFilename;
  }
}
