package by.belyahovich.domain;

public class Currencies {
    private int id;
    private String code;
    private String fullName;
    private String sign;

    public static Builder newBuilder() {
        return new Currencies().new Builder();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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

        Currencies that = (Currencies) o;

        if (id != that.id) return false;
        if (!code.equals(that.code)) return false;
        if (!fullName.equals(that.fullName)) return false;
        return sign.equals(that.sign);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + code.hashCode();
        result = 31 * result + fullName.hashCode();
        result = 31 * result + sign.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Currencies{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", fullName='" + fullName + '\'' +
                ", sign='" + sign + '\'' +
                '}';
    }

    public class Builder {
        private Builder() {
        }

        public Builder setId(int id) {
            Currencies.this.id = id;
            return this;
        }

        public Builder setCode(String code) {
            Currencies.this.code = code;
            return this;
        }

        public Builder setFullName(String fullName) {
            Currencies.this.fullName = fullName;
            return this;
        }

        public Builder setSign(String sign) {
            Currencies.this.sign = sign;
            return this;
        }

        public Currencies build() {
            return Currencies.this;
        }
    }
}
