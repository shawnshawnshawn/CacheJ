package com.cachetools.anno;

import java.lang.annotation.*;

/**
 * clear cache obj with this key.
 *
 * @author 白也
 * @date 2020/12/7 下午 6:47
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CacheClear {

    /**
     * 缓存名称
     * @return ""
     */
    String name() default "";

    /**
     * 缓存key
     * @return ""
     */
    String key() default "";

    /**
     * 带此前缀的key
     * @return ""
     */
    String keyPrefix() default "";
}
