package com.codingjoa.mybatis;

import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;

import com.codingjoa.entity.BoardImage;
import com.codingjoa.entity.UserImage;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Intercepts({
	@Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class }),
})
public class MybatisUpdateInterceptor implements Interceptor {
	
	private List<Long> boardImageIdsToFail = List.of();
	private List<Long> userImageIdsToFail = List.of(620L, 581L, 571L);

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		Object[] args = invocation.getArgs();
		MappedStatement ms = (MappedStatement) args[0];
		Object parameter = args[1];
		
		String statementId = ms.getId();
		String queryId = statementId.substring(statementId.lastIndexOf(".") + 1);
		log.info("## {} ({})", this.getClass().getSimpleName(), queryId);
		
		if (statementId.endsWith("BatchMapper.deleteBoardImage")) {
			Long id = ((BoardImage) parameter).getId();
			log.info("\t > boardIamge id: {}", id);
			
			if (boardImageIdsToFail.contains(id)) {
				throw new SQLException();
			}
		} else if (statementId.endsWith("BatchMapper.deleteUserImage")) {
			Long id = ((UserImage) parameter).getId();
			log.info("\t > userImage id: {}", id);
			
			if (userImageIdsToFail.contains(id)) {
				throw new SQLException();
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
