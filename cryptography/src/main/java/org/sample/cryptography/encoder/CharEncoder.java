package org.sample.cryptography.encoder;

public interface CharEncoder {

   byte[] encode(int decimal);

   int decode(byte[] chars);

}
