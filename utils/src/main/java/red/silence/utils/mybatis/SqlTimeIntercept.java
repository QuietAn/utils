package red.silence.utils.mybatis;

import java.sql.Statement;
import java.util.Properties;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Intercepts({
		@Signature(type = StatementHandler.class, method = "query", args = {
				Statement.class, ResultHandler.class }),
		@Signature(type = StatementHandler.class, method = "update", args = { Statement.class }),
		@Signature(type = StatementHandler.class, method = "batch", args = { Statement.class }) })
public class SqlTimeIntercept implements Interceptor {

	private static Logger logger = LoggerFactory.getLogger(SqlTimeIntercept.class);

	@Override
	public Object intercept(Invocation invocation) throws Throwable {

		long startTime = System.currentTimeMillis();
		try {
			if (invocation.getTarget() instanceof StatementHandler) {
				StatementHandler statementHandler = (StatementHandler) invocation
						.getTarget();
				BoundSql boundSql = statementHandler.getBoundSql();
				
				logger.info("sql : [" + boundSql.getSql() + " ] ");
				logger.info("sqlgetParameterObject : [" + boundSql.getParameterObject() + " ] ");
			}
			return invocation.proceed();
		} finally {
			long endTime = System.currentTimeMillis();
			long sqlCost = endTime - startTime;

			logger.info("sqlTime : [" + sqlCost + "ms ] ");
		}
	}

	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties properties) {

	}

}
