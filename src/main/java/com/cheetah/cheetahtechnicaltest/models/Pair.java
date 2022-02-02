package com.cheetah.cheetahtechnicaltest.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

/**
 * @author Yuvaraj
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//@EqualsAndHashCode
public class Pair {

    private String first;
    private String second;
    private List<String> tags;

//    @Override public int hashCode() {
//        return first.hashCode();
//    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair pair = (Pair) o;
        return (Objects.equals(first, pair.first) && Objects.equals(second, pair.second)) || (Objects.equals(second, pair.first) && Objects.equals(first, pair.second));
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }
}
