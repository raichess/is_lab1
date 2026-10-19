package com.raichess.repository;

import com.raichess.dto.PersonFilter;
import com.raichess.model.Color;
import com.raichess.model.Country;
import com.raichess.model.Person;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Arrays;

public final class PersonSpecifications {

    private PersonSpecifications() {
    }

    public static Specification<Person> byFilter(PersonFilter f) {
        return Specification
                .where(nameContains(f.getName()))
                .and(locationNameContains(f.getLocationName()))
                .and(enumContains("eyeColor", Color.class, f.getEyeColor()))
                .and(enumContains("hairColor", Color.class, f.getHairColor()))
                .and(enumContains("nationality", Country.class, f.getNationality()))
                .and(heightAtLeast(f.getHeightMin()))
                .and(heightAtMost(f.getHeightMax()));
    }

    private static Specification<Person> nameContains(String text) {
        if (isBlank(text)) {
            return null;
        }
        return (root, query, cb) -> cb.like(cb.lower(root.<String>get("name")), pattern(text), '\\');
    }

    private static Specification<Person> locationNameContains(String text) {
        if (isBlank(text)) {
            return null;
        }
        return (root, query, cb) ->
                cb.like(cb.lower(root.get("location").<String>get("name")), pattern(text), '\\');
    }
    private static Specification<Person> heightAtLeast(Float min) {
        if (min == null) return null;
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.<Float>get("height"), min);
    }

    private static Specification<Person> heightAtMost(Float max) {
        if (max == null) return null;
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.<Float>get("height"), max);
    }

    private static <E extends Enum<E>> Specification<Person> enumContains(String field, Class<E> type, String text) {
        if (isBlank(text)) {
            return null;
        }
        String needle = text.trim().toLowerCase();
        List<E> matches = Arrays.stream(type.getEnumConstants())
                .filter(e -> e.name().toLowerCase().contains(needle))
                .toList();
        return (root, query, cb) -> matches.isEmpty() ? cb.disjunction() : root.<E>get(field).in(matches);
    }

    private static String pattern(String text) {
        String escaped = text.trim().toLowerCase()
                .replace("\\", "\\\\").replace("%", "\\%").replace("_", "\\_");
        return "%" + escaped + "%";
    }

    private static boolean isBlank(String s) {
        return s == null || s.isBlank();
    }
}
