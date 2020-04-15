package com.aio.cacheJ.intercepter;


import com.aio.cacheJ.anno.CacheClear;
import com.aio.cacheJ.anno.CacheValue;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="jiangliuer_shawn@outlook.com>zhangyingdong</a>
 * @date 2020/4/15 上午10:51
 */
@Slf4j
@Component
public class CacheJMethodIntercepter implements MethodInterceptor, Serializable {

    @Resource
    private RedisTemplate redisTemplate;

    @Override
    @SuppressWarnings("unchecked")
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {

        log.info("执行方法拦截器");

        Method method = methodInvocation.getMethod();

        if (method.isAnnotationPresent(CacheValue.class)) {

            CacheValue annotation = method.getAnnotation(CacheValue.class);

            //缓存的key
            String key = annotation.key();

            //缓存过期时间
            long expire = annotation.expire();

            //缓存时间单位
            TimeUnit timeUnit = annotation.timeUnit();

            //如果key为空,默认使用方法名作为key
            if (StringUtils.isBlank(key)) {
                key = method.getName();
            }

            //获取spel表达式的缓存key
            String realKey = CacheJSpelExpressionParser.parseCacheKey(key, method, methodInvocation.getArguments());

            //获取数据缓存
            Object ob = redisTemplate.opsForValue().get(realKey);

            //缓存为空,执行方法并缓存方法执行结果
            if (ob == null) {
                Object invoke = method.invoke(methodInvocation.getThis(), methodInvocation.getArguments());
                redisTemplate.opsForValue().set(realKey, invoke, expire, timeUnit);
                return invoke;
            } else {
                return ob;
            }
        } else if (method.isAnnotationPresent(CacheClear.class)) {

            CacheClear cacheClear = method.getAnnotation(CacheClear.class);

            String key = cacheClear.key();

            if (StringUtils.isBlank(key)) {
                key = method.getName();
            }

            //获取spel表达式的缓存key
            String realKey = CacheJSpelExpressionParser.parseCacheKey(key, method, methodInvocation.getArguments());

            redisTemplate.delete(realKey);
        }

        //正常方法执行
        return methodInvocation.proceed();
    }
}
