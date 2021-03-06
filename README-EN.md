# CacheJ
> Use springboot + redis +  method interceptor,a feature similar to @cacheable
## Functional description
* [@CacheClear](/src/main/java/com/cachetools/anno/CacheClear.java)
  The annotation contains
  ```
  /**
     * Name of the cache
     * @return ""
     */
    String name() default "";

    /**
     * The cache key
     * @return ""
     */
    String key() default "";

    /**
     * Key with this prefix
     * @return ""
     */
    String keyPrefix() default "";
  ```
  1. name: Represents the cache name；
  2. key：Represents the cache key； 
  3. keyPrefix：Key representing the same prefix
* [@CacheValue](/src/main/java/com/cachetools/anno/CacheValue.java)
  This annotation contains the following
  ```
  /**
     * Support for SPEL expressions
     */
    String key() default "";

    String name() default "";

    long expire() default 300L;

    TimeUnit timeUnit() default TimeUnit.SECONDS;
  ```
  1. key：Represents the cache key；
  2. name：Represents the cache name and the log print name；
  3. expire：Is the expiration time；
  4. timeUnit：Unit of time
* [CacheJMethodInterceptor](/src/main/java/com/cachetools/interceptor/CacheJMethodInterceptor.java)
  The specific handling method of caching annotations
* [CacheJPointCutAdvisor](/src/main/java/com/cachetools/interceptor/CacheJPointCutAdvisor.java)
  Cache the annotation facets
* [CacheJSpelExpressionParser](/src/main/java/com/cachetools/interceptor/CacheJSpelExpressionParser.java)
  Spex expression handling
* [RedisCacheConfiguration](/src/main/java/com/cachetools/redis/RedisCacheConfiguration.java)
  Redis serial number configuration, using FastJason serialization.
## Use
* The first way is to simply reference the project as a JAR。
* The second way is to refer to the project as a module。
> tips: The configuration in the project is automatically loaded and only needs to be referenced