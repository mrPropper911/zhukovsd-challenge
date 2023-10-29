package by.belyahovich.dto;

import by.belyahovich.domain.Currencies;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class ExchangeRatesResponse {
    @JsonProperty("id")
    private int id;
    @JsonProperty("baseCurrency")
    private Currencies baseCurrency;
    @JsonProperty("targetCurrency")
    private Currencies targetCurrency;
    @JsonProperty("rate")
    private double rate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Currencies getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(Currencies baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public Currencies getTargetCurrency() {
        return targetCurrency;
    }

    public void setTargetCurrency(Currencies targetCurrency) {
        this.targetCurrency = targetCurrency;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExchangeRatesResponse that = (ExchangeRatesResponse) o;
        return id == that.id && Double.compare(that.rate, rate) == 0 && baseCurrency.equals(that.baseCurrency) && Objects.equals(targetCurrency, that.targetCurrency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, baseCurrency, targetCurrency, rate);
    }

    @Override
    public String toString() {
        return "ExchangeRatesResponse{" +
                "id=" + id +
                ", baseCurrency=" + baseCurrency +
                ", targetCurrency=" + targetCurrency +
                ", rate=" + rate +
                '}';
    }

    public static Builder newBuilder(){
        return new ExchangeRatesResponse().new Builder();
    }

    public class Builder{
        private Builder() {
        }

        public Builder setId(int id){
            ExchangeRatesResponse.this.id = id;
            return this;
        }

        public Builder setBaseCurrency(Currencies currencies){
            ExchangeRatesResponse.this.baseCurrency = currencies;
            return this;
        }

        public Builder setTargetCurrency(Currencies targetCurrency){
            ExchangeRatesResponse.this.targetCurrency = targetCurrency;
            return this;
        }

        public Builder setRate(double rate){
            ExchangeRatesResponse.this.rate = rate;
            return this;
        }

        public ExchangeRatesResponse build(){
            return ExchangeRatesResponse.this;
        }


    }

}
