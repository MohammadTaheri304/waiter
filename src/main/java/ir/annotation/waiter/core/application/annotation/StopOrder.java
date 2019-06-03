package ir.annotation.waiter.core.application.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An order annotation to be used on components for ordered stop.
 *
 * <p>
 * Components stop from lower order values to higher ones.
 * Note: All components that did not specify order annotation will stop in alphabetically order based on component's name. (componentA will stop before componentB)
 * </p>
 *
 * @author Alireza Pourtaghi
 */
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface StopOrder {

    int value();
}
