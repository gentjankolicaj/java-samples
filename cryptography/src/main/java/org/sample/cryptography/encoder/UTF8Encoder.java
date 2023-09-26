package org.sample.cryptography.encoder;

import java.nio.ByteBuffer;

public class UTF8Encoder implements CharEncoder{


  @Override
  public byte[] encode(int decimal) {
    byte[] array = new byte[4];
    array[0] = (byte) (decimal >> 24);
    array[1] = (byte) (decimal >> 18);
    array[2] = (byte) (decimal >> 10);
    array[3] = (byte) decimal;
    return array;
  }

  @Override
  public int decode(byte[] chars) {
    return ByteBuffer.wrap(chars).getInt();
  }
}
