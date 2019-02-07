package jihuayu.mccoder.annotation;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.SOURCE)
public @interface RegItems {
    RegItem[] value();
}
