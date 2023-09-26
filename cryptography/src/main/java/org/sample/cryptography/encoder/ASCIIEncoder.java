package org.sample.cryptography.encoder;

import java.nio.ByteBuffer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class ASCIIEncoder implements CharEncoder {

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
