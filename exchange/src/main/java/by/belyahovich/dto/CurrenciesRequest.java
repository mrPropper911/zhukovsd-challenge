package by.belyahovich.dto;

public class CurrenciesRequest {
    private String name;
    private String code;
    private String sign;

    public static Builder newBuilder(){
        return new CurrenciesRequest().new Builder();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CurrenciesRequest that = (CurrenciesRequest) o;

        if (!name.equals(that.name)) return false;
        if (!code.equals(that.code)) return false;
        return sign.equals(that.sign);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + code.hashCode();
        result = 31 * result + sign.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "CurrenciesRequest{" +
                "name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", sign='" + sign + '\'' +
                '}';
    }

    public class Builder {
        public Builder() {
        }

        public Builder setName(String name){
            CurrenciesRequest.this.name = name;
            return this;
        }

        public Builder setCode(String code){
            CurrenciesRequest.this.code = code;
            return this;
        }

        public Builder setSign(String sign){
            CurrenciesRequest.this.sign = sign;
            return this;
        }

        public CurrenciesRequest build(){
            return CurrenciesRequest.this;
        }
    }
}
