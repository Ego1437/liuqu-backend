package com.omate.liuqu.specifications;

import com.omate.liuqu.model.Activity;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class ActivitySpecifications {
    public static Specification<Activity> filterBy(
            Integer categoryLevel1,
            Integer categoryLevel2,
            Integer minActivityDuration,
            Integer maxActivityDuration,
            String activityName,
            String activityAddress
    ) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (categoryLevel1 != null) {
                predicates.add(criteriaBuilder.equal(root.get("categoryLevel1"), categoryLevel1));
            }
            if (categoryLevel2 != null) {
                predicates.add(criteriaBuilder.equal(root.get("categoryLevel2"), categoryLevel2));
            }
            if (minActivityDuration != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("activityDuration"), minActivityDuration));
            }
            if (maxActivityDuration != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("activityDuration"), maxActivityDuration));
            }
            if (activityName != null) {
                predicates.add(criteriaBuilder.like(root.get("activityName"), "%" + activityName + "%"));
            }
            if (activityAddress != null) {
                predicates.add(criteriaBuilder.like(root.get("activityAddress"), "%" + activityAddress + "%"));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}


