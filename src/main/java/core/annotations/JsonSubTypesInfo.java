package core.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface JsonSubTypesInfo {
    public Type[] value();

    public @interface Type {
        public Class<?> value();
        public String name() default "";
    }
}
