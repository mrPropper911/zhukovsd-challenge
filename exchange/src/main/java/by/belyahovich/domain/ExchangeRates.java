package by.belyahovich.domain;


import java.util.Objects;

public class ExchangeRates {
    private int id;
    private Currencies baseCurrency;
    private Currencies targetCurrency;
    private double rate;

    public static Builder newBuilder(){
        return new ExchangeRates().new Builder();
    }

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
        ExchangeRates that = (ExchangeRates) o;
        return id == that.id && Double.compare(that.rate, rate) == 0 && baseCurrency.equals(that.baseCurrency) && targetCurrency.equals(that.targetCurrency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, baseCurrency, targetCurrency, rate);
    }

    @Override
    public String toString() {
        return "ExchangeRates{" +
                "id=" + id +
                ", baseCurrency=" + baseCurrency +
                ", targetCurrency=" + targetCurrency +
                ", rate=" + rate +
                '}';
    }

    public class Builder {
        private Builder() {
        }

        public Builder setId(int id) {
            ExchangeRates.this.id = id;
            return this;
        }

        public Builder setBaseCurrency(Currencies baseCurrency) {
            ExchangeRates.this.baseCurrency = baseCurrency;
            return this;
        }

        public Builder setTargetCurrency(Currencies targetCurrency){
            ExchangeRates.this.targetCurrency = targetCurrency;
            return this;
        }

        public Builder setRate(double rate){
            ExchangeRates.this.rate = rate;
            return this;
        }

        public  ExchangeRates build (){
            return ExchangeRates.this;
        }
    }
}
