package com.br.srm.asset.test.exceptions;

public class SettledTransactionException extends RuntimeException {
  public SettledTransactionException(String message) {
    super(message);
  }
}