package com.codingjoa.mybatis;

import java.util.Properties;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Intercepts({
	@Signature(type = Executor.class, method = "flushStatements", args = {})
})
public class MybatisFlushStatementsInterceptor implements Interceptor {

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		String methodName = invocation.getMethod().getName();
		log.info("## {}.intercept, mathod {}", this.getClass().getSimpleName(), methodName);
        return invocation.proceed();
	}

	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties properties) { }

}
