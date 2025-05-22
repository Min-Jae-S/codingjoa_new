package com.codingjoa.mybatis;

import java.util.Arrays;
import java.util.Properties;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Intercepts({
	@Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class }),
})
public class MybatisUpdateInterceptor implements Interceptor {

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		String methodName = invocation.getMethod().getName();
		log.info("## {}.intercept, method: {}", this.getClass().getSimpleName(), methodName);
		
		Object[] args = invocation.getArgs();
		log.info("\t > args = {}", Arrays.toString(args));
		
        return invocation.proceed();
	}

	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties properties) {	}

}
