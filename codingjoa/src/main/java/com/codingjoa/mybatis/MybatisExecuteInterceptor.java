package com.codingjoa.mybatis;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;

import com.codingjoa.entity.BoardImage;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Intercepts({
	@Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class }),
	@Signature(type = Executor.class, method = "flushStatements", args = {})
})
public class MybatisExecuteInterceptor implements Interceptor {
	
	private List<Long> failIds = List.of(4000L, 3418L, 2685L, 2611L, 1820L, 1545L);

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		log.debug("## {}.intercept", this.getClass().getSimpleName());
		String method = invocation.getMethod().getName();
		Object[] args = invocation.getArgs();
		
		List<String> argsClass = (args == null) ? null : 
			Arrays.stream(args)
				.map(obj -> obj.getClass().getSimpleName())
				.collect(Collectors.toList());
		log.debug("\t > method: {}, args: {}", method, argsClass);
		
		if ("update".equals(method)) {
			MappedStatement ms = (MappedStatement) args[0];
			Object parameter = args[1];
			log.debug("\t > parameter type: {}", parameter.getClass().getSimpleName());
			
			if (ms.getId().endsWith("BatchMapper.deleteBoardImage") && parameter instanceof BoardImage) {
				Long id = ((BoardImage) parameter).getId();
				log.debug("\t > boardIamge id: {}", id);
				
				if (failIds.contains(id)) {
					throw new SQLException();
				}
			}
		}
		
        return invocation.proceed();
	}

	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties properties) {	}

}
