package com.cachetools.interceptor;

import com.alibaba.fastjson.JSON;
import com.cachetools.anno.CacheClear;
import com.cachetools.anno.CacheValue;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class CacheJMethodInterceptor  implements MethodInterceptor, Serializable {

    private static final Logger log = LoggerFactory.getLogger(CacheJMethodInterceptor.class);

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    protected RedisTemplate<String, String> redisTemplate;

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        Method method = methodInvocation.getMethod();
        if (method.isAnnotationPresent(CacheValue.class)) {
            CacheValue annotation = method.getAnnotation(CacheValue.class);
            //缓存的key
            String key = annotation.key();
            //缓存过期时间
            long expire = annotation.expire();
            //缓存时间单位
            TimeUnit timeUnit = annotation.timeUnit();
            // 缓存名称
            String name = annotation.name();
            //如果key为空,默认使用方法名作为key
            if (StringUtils.isEmpty(key)) {
                key = method.getName();
            }
            //获取spel表达式的缓存key
            String realKey = CacheJSpelExpressionParser.parseCacheKey(key, method, methodInvocation.getArguments());
            //获取数据缓存
            Class<?> returnType = method.getReturnType();

            Object obj;
            if (List.class == returnType) {
                Type genericReturnType = method.getGenericReturnType();
                if (genericReturnType instanceof ParameterizedType) {
                    ParameterizedType parameterizedType = (ParameterizedType) genericReturnType;
                    String typeName = parameterizedType.getActualTypeArguments()[0].getTypeName();
                    obj = JSON.parseArray(stringRedisTemplate.opsForValue().get(realKey), Class.forName(typeName));
                } else {
                    log.warn("返回集合类型数据，缓存未处理！");
                    return methodInvocation.proceed();
                }
            } else {
                obj = stringRedisTemplate.opsForValue().get(realKey);
            }
            //缓存为空,执行方法并缓存方法执行结果
            if (obj == null) {
                Object invoke = method.invoke(methodInvocation.getThis(), methodInvocation.getArguments());
                stringRedisTemplate.opsForValue().set(realKey, JSON.toJSONString(invoke), expire, timeUnit);
                return invoke;
            } else {
                log.info("Walk the cache！ | execute -> {}", name);
                return obj;
            }
        } else if (method.isAnnotationPresent(CacheClear.class)) {
            CacheClear cacheClear = method.getAnnotation(CacheClear.class);
            String key = cacheClear.key();
            String name = cacheClear.name();
            log.info("delete cache -> {}", name);
            if (!StringUtils.isEmpty(key)) {
                String realKey = CacheJSpelExpressionParser.parseCacheKey(key, method, methodInvocation.getArguments());
                stringRedisTemplate.delete(realKey);
            }

            String keyPrefix = cacheClear.keyPrefix();
            if (!StringUtils.isEmpty(keyPrefix)) {
                Set<String> keys = redisTemplate.keys(keyPrefix + "*");
                if (!CollectionUtils.isEmpty(keys)) {
                    for (String k : keys) {
                        stringRedisTemplate.opsForValue().getOperations().delete(k);
                    }
                }
            }
        }
        //正常方法执行
        return methodInvocation.proceed();
    }
}
