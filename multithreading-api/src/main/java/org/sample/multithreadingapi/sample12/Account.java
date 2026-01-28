package org.sample.multithreadingapi.sample12;

public class Account {

  private int balance;

  public Account() {
    super();
    this.balance = 30000;
  }

  public Account(int balance) {
    super();
    this.balance = balance;
  }

  public int getBalance() {
    return balance;
  }

  public void setBalance(int balance) {
    this.balance = balance;
  }


}
