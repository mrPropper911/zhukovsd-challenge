package by.belyahovich;

import java.io.Serializable;
import java.util.Objects;

public class Mystery implements Serializable {
    private long id;
    private String word;
    private Difficult difficult;

    public static Builder newBuilder(){
        return new Mystery().new Builder();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Difficult getDifficult() {
        return difficult;
    }

    public void setDifficult(Difficult difficult) {
        this.difficult = difficult;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Mystery mystery = (Mystery) o;

        if (id != mystery.id) return false;
        if (!Objects.equals(word, mystery.word)) return false;
        return difficult == mystery.difficult;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (word != null ? word.hashCode() : 0);
        result = 31 * result + (difficult != null ? difficult.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Mystery{" +
                "id=" + id +
                ", word='" + word + '\'' +
                ", difficult=" + difficult +
                '}';
    }

    public class Builder{
        private Builder(){}

        public Builder setMysteryId(long id){
            Mystery.this.id = id;
            return this;
        }

        public Builder setMysteryWord(String word){
            Mystery.this.word = word;
            if (word.length() < 4){
                Mystery.this.difficult = Difficult.EASY;
            } else if (word.length() > 4  && word.length() < 7) {
                Mystery.this.difficult = Difficult.MEDIUM;
            } else {
                Mystery.this.difficult = Difficult.HARD;
            }
            return this;
        }

        public Mystery build(){
            return Mystery.this;
        }
    }
}

enum Difficult {
    EASY, MEDIUM, HARD
}