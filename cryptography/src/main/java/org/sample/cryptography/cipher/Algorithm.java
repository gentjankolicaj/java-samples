package org.sample.cryptography.cipher;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public enum Algorithm {
  CAESAR(new CaesarCipher(), new PseudoCaesarKey(3)),
  VIGENERE(new VigenereCipher(), new PseudoVigenereKey("ARSFDA#$r45gvasfc34")),
  TRITHEMIUS(new TrithemiusCipher(), new PseudoTrithemiusKey("ABCDEFGHIJKLMNOPQRSTUVWXYZ")),
  AUTOKEY(new AutokeyCipher(), new PseudoAutoKey("Spartan-117_Master_Chief")),
  VERNAM(new VernamCipher(), new PseudoVernamKey("VernamKey")),
  OTP(new OTPCipher(), new NoSecureKey("Dont' use in prod, this is just a pseudo sample."));

  private final Cipher cipher;
  private final Key key;
}
