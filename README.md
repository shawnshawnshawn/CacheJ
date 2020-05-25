# CacheJ
使用springboot + redis + 方法拦截器,一种类似@Cacheable的功能

使用方式：
   1. 引用jar包；
   2. 在方法上使用@CacheValue缓存数据，使用CacheClear清楚缓存数据；
   3. 缓存的key支持spel表达式。
