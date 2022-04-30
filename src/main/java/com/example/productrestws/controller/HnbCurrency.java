package com.example.productrestws.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class HnbCurrency {

  @JsonProperty("Valuta")
  private String currencyName;

  @JsonProperty("Srednji za devize")
  private String conversionRate;

  public HnbCurrency() {
  }

  public String getCurrencyName() {
    return this.currencyName;
  }

  public void setCurrencyName(String currencyName) {
    this.currencyName = currencyName;
  }

  public String getConversionRate() {
    return this.conversionRate;
  }

  public void setConversionRate(String conversionRate) {
    this.conversionRate = conversionRate;
  }

  @Override
  public String toString() {
    return "HnbCurrency{" +
        "currencyName = " + currencyName +
        ", conversionRate = " + conversionRate + "}";
  }
}