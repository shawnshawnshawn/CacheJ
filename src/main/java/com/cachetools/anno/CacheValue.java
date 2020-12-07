package com.cachetools.anno;


import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * cache obj with this key. you can custom Expire time. Key support SPEL expression.
 * @author 白也
 * @date 2020/12/7 下午 6:47
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CacheValue {

    /**
     * 支持Spel表达式
     */
    String key() default "";

    String name() default "";

    long expire() default 300L;

    TimeUnit timeUnit() default TimeUnit.SECONDS;


}
