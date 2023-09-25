package org.sample.cryptography.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public enum Algorithm {
  CAESAR(new CaesarCipher(), new PseudoCaesarKey(3)), AUTOKEY(new AutokeyCipher(), new PseudoAutoKey()), VIGENERE(
      new VigenereCipher(), new PseudoVigenereKey()), VERNAM(new VernamCipher(), new PseudoVernamKey());

  private final Cipher cipher;
  private final Key key;
}
