package jihuayu.mccoder.annotation;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Repeatable(RegItems.class)
@Retention(RetentionPolicy.SOURCE)
public @interface RegItem {
    String value(); //regName
    String unLocalName();
}
