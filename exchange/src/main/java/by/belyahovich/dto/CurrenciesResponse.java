package by.belyahovich.dto;

public class CurrenciesResponse {
    private int id;
    private String name;
    private String code;
    private String sign;

    public static Builder newBuilder(){
        return new CurrenciesResponse().new Builder();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

        CurrenciesResponse that = (CurrenciesResponse) o;

        if (id != that.id) return false;
        if (!name.equals(that.name)) return false;
        if (!code.equals(that.code)) return false;
        return sign.equals(that.sign);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + name.hashCode();
        result = 31 * result + code.hashCode();
        result = 31 * result + sign.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "CurrenciesResponse{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", sign='" + sign + '\'' +
                '}';
    }

    public class Builder{
        private Builder() {
        }

        public Builder setId(int id){
            CurrenciesResponse.this.id = id;
            return this;
        }

        public Builder setName(String name){
            CurrenciesResponse.this.name = name;
            return this;
        }

        public Builder setCode(String code){
            CurrenciesResponse.this.code = code;
            return this;
        }

        public Builder setSign(String sign){
            CurrenciesResponse.this.sign = sign;
            return this;
        }

        public CurrenciesResponse build(){
            return CurrenciesResponse.this;
        }
    }
}
