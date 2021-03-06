package com.cachetools.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;

public class CacheJSpelExpressionParser {

    private final static Logger logger = LoggerFactory.getLogger(CacheJSpelExpressionParser.class);

    /**
     * SPEL表达式解析器
     */
    private static final ExpressionParser EXPRESSION_PARSER = new SpelExpressionParser();
    /**
     * 获取方法参数名称发现器
     */
    private static final ParameterNameDiscoverer PARAMETER_NAME_DISCOVERER = new DefaultParameterNameDiscoverer();
    public static String parseCacheKey(String expression, Method method, Object[] args) {
        // 无参传入,直接返回
        if (args == null || args.length == 0) {
            return expression;
        }
        // 获取参数名
        String[] parameterNames = PARAMETER_NAME_DISCOVERER.getParameterNames(method);
        if (parameterNames == null) {
            return expression;
        }
        // 无参缓存key,直接返回
        if (!expression.contains("#")) {
            return expression;
        }
        //由于java不允许有匿名参数,所以如果参数名多于参数值,则必为非法
        if (parameterNames.length > args.length) {
            logger.error("参数值的长度少于参数名长度, 方法:{}, 参数名长度: {},参数值长度:{}", method, parameterNames.length, args.length);
            throw new IllegalArgumentException("参数传入不足");
        }
        // 将参数名与参数值放入参数上下文
        EvaluationContext evaluationContext = new StandardEvaluationContext();
        for (int i = 0; i < parameterNames.length; i++) {
            evaluationContext.setVariable(parameterNames[i], args[i]);
        }
        // 计算表达式(根据参数上下文)
        return EXPRESSION_PARSER.parseExpression(expression).getValue(evaluationContext, String.class);
    }
}
