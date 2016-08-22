package xiaofei.library.comparatorgenerator.internal;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Eric on 16/8/22.
 */
public interface CriterionManager {
    ConcurrentHashMap<Integer, SortingCriterion> getCriteriaIn(Class<?> clazz);
}
