package com.codingjoa.mybatis;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;

import com.codingjoa.entity.BoardImage;

import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("unused")
@Slf4j
@Intercepts({
	@Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class }),
	@Signature(type = Executor.class, method = "flushStatements", args = {})
})
public class MybatisExecuteInterceptor implements Interceptor {
	
	private List<Long> failIds = List.of(196L, 195L);

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		log.info("## {}.intercept", this.getClass().getSimpleName());
		String method = invocation.getMethod().getName();
		Object[] args = invocation.getArgs();
		
		List<String> argsClass = (args == null) ? null : 
			Arrays.stream(args)
				.map(obj -> obj.getClass().getSimpleName())
				.collect(Collectors.toList());
		log.info("\t > method: {}, args: {}", method, argsClass);
		
		if ("update".equals(method)) {
			MappedStatement ms = (MappedStatement) args[0];
			Object parameter = args[1];
			
			if (ms.getId().endsWith("BatchMapper.deleteBoardImage") && parameter instanceof BoardImage) {
				Long id = ((BoardImage) parameter).getId();
				log.info("\t > boardIamge id: {}", id);
				
				if (failIds.contains(id)) {
					throw new SQLException("id=" + id);
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
	public void setProperties(Properties properties) {
		// TODO Auto-generated method stub
	}

}
