package com.aio.cacheJ.anno;

import java.lang.annotation.*;

/**
 * @author <a href="jiangliuer_shawn@outlook.com>zhangyingdong</a>
 * @date 2020/4/13 下午2:08
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CacheClear {

    String key() default "";
}
