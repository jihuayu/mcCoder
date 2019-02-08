package jihuayu.mccoder.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE,ElementType.FIELD,ElementType.LOCAL_VARIABLE})
@Retention(RetentionPolicy.SOURCE)
public @interface I18n {
    String value();
}
