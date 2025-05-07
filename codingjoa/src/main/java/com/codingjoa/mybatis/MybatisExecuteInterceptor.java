package com.codingjoa.mybatis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

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
	
	private List<Long> failIds = new ArrayList<>(List.of(350L, 349L));

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		log.info("## {}.intercept", this.getClass().getSimpleName());
		try {
			String method = invocation.getMethod().getName();
			Object[] args = invocation.getArgs();
			log.info("\t > method: {}", method);
			log.info("\t > args: {}", Arrays.toString(args));
			
			if ("update".equals(method)) {
				MappedStatement ms = (MappedStatement) args[0];
				Object parameter = args[1];
				
				if (ms.getId().endsWith("BatchMapper.deleteBoardImage") && parameter instanceof BoardImage) {
					Long id = ((BoardImage) parameter).getId();
					log.info("\t > boardIamge id: {}", id);
					
					BoundSql boundSql  = ms.getBoundSql(parameter);
					log.info("\t > bound sql: {}", boundSql.getSql());
					
//					if (failIds.contains(id)) {
//						throw new SQLException("id=" + id);
//					}
				}
			} 
		} catch (Exception e) {
			e.printStackTrace();
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
