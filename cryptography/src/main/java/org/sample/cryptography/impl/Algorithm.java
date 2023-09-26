package org.sample.cryptography.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public enum Algorithm {
  CAESAR(new CaesarCipher(), new PseudoCaesarKey(3)),
  VIGENERE(new VigenereCipher(), new PseudoVigenereKey("ARSFDA#$r45gvasfc34")),
  TRITHEMIUS(new TrithemiusCipher(),new PseudoTrithemiusKey("ABCDEFGHIJKLMNOPQRSTUVWXYZ")),
  AUTOKEY(new AutokeyCipher(), new PseudoAutoKey("Spartan-117_Master_Chief")),
  VERNAM(new VernamCipher(), new PseudoVernamKey());

  private final Cipher cipher;
  private final Key key;
}
