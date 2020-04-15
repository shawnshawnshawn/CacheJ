package com.aio.cacheJ.intercepter;

import com.aio.cacheJ.anno.CacheClear;
import com.aio.cacheJ.anno.CacheValue;
import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;

/**
 * @author <a href="jiangliuer_shawn@outlook.com>zhangyingdong</a>
 * @date 2020/4/15 下午1:07
 */

@Component
public class CacheJPointCutAdvisor extends AbstractPointcutAdvisor {

    @Resource
    private CacheJMethodIntercepter cacheJMethodIntercepter;

    private StaticMethodMatcherPointcut staticMethodMatcherPointcut = new StaticMethodMatcherPointcut() {
        @Override
        public boolean matches(Method method, Class<?> aClass) {
            return method.isAnnotationPresent(CacheValue.class) || aClass.isAnnotationPresent(CacheValue.class)
                    || method.isAnnotationPresent(CacheClear.class);
        }
    };

    @Override
    public Pointcut getPointcut() {
        return this.staticMethodMatcherPointcut;
    }

    @Override
    public Advice getAdvice() {
        return this.cacheJMethodIntercepter;
    }
}
