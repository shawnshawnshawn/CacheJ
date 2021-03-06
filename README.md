# CacheJ [En-document](/README-EN.md)
> 使用springboot + redis + 方法拦截器,一种类似@Cacheable的功能
## 功能描述
* [@CacheClear](/src/main/java/com/cachetools/anno/CacheClear.java)
  该注解包含
  ```
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
  ```
  name表示缓存名称；key：表示缓存key；keyPrefix：表示相同前缀的key
* [@CacheValue](/src/main/java/com/cachetools/anno/CacheValue.java)
  该注解包含以下
  ```
  /**
     * 支持Spel表达式
     */
    String key() default "";

    String name() default "";

    long expire() default 300L;

    TimeUnit timeUnit() default TimeUnit.SECONDS;
  ```
  key：表示缓存key；name：表示缓存名称，日志打印名称；expire：表示过期时长；timeUnit：表示时间单位
* [CacheJMethodInterceptor](/src/main/java/com/cachetools/interceptor/CacheJMethodInterceptor.java)
  缓存注解的具体处理方法
* [CacheJPointCutAdvisor](/src/main/java/com/cachetools/interceptor/CacheJPointCutAdvisor.java)
  缓存注解切面
* [CacheJSpelExpressionParser](/src/main/java/com/cachetools/interceptor/CacheJSpelExpressionParser.java)
  spex表达式处理
* [RedisCacheConfiguration](/src/main/java/com/cachetools/redis/RedisCacheConfiguration.java)
  redis序列号配置，使用fastjason序列化。
## 使用
* 第一种方式，直接把项目打成jar包引用。
* 第二种方式，把该项目作为module引用。
> 项目中的配置会自动加载，只需要引用即可