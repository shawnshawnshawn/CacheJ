package com.aio.cacheJ.anno;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="jiangliuer_shawn@outlook.com>zhangyingdong</a>
 * @date 2020/4/13 下午1:29
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CacheValue {

    /**
     * 支持Spel表达式
     */
    String key() default "";

    long expire() default 300L;

    TimeUnit timeUnit() default TimeUnit.SECONDS;
}
