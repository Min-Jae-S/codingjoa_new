package com.codingjoa.mybatis;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Intercepts({
	@Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class }),
	@Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class })
})
public class MybatisExecuteInterceptor implements Interceptor {
	
	private List<Long> failIds = new ArrayList<>(List.of(3170L, 3166L));

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		log.info("## {}.intercept", this.getClass().getSimpleName());
		MappedStatement ms = (MappedStatement) invocation.getArgs()[0];
		Object parameter = invocation.getArgs()[1];
		log.info("\t > intercepted method: {}", ms.getId());
		log.info("\t > parameter: {}", parameter);
        
        if (ms.getId().equals("deleteBoardImage") && parameter instanceof Long  && failIds.contains((Long) parameter)) {
            throw new SQLException("강제 delete boardImage 실패: id=" + parameter);
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
