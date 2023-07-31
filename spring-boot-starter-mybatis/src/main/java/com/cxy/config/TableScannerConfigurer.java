package com.cxy.config;

import com.cxy.annotation.Table;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.reflections.Reflections;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.Set;

@Configuration
public class TableScannerConfigurer implements InitializingBean {
	private Class scannerAnnotation = Table.class;

	@Autowired
	private SqlSessionFactory sqlSessionFactory;

	@Autowired
	private DataSource dataSource;

	@Override
	public void afterPropertiesSet() throws Exception {
		// 要扫描的包
		String packageName = "com.cxy";
		//反射
		Reflections ref = new Reflections(packageName);
		// 获取扫描到的标记注解的集合
		Set<Class<?>> set = ref.getTypesAnnotatedWith(scannerAnnotation);
		set.stream().forEach(System.out::println);
		set.stream().forEach(e->{
			createTable(e);
		});
	}

	public void createTable(Class tableClazz){
		try{
			SqlSession sqlSession = sqlSessionFactory.openSession();
			Connection connection = sqlSession.getConnection();
			Table annotation = (Table)tableClazz.getAnnotation(Table.class);
			String tableName = annotation.value(); //表名

			PreparedStatement ps=connection.prepareStatement("select database() as database_name");
			ResultSet rs=ps.executeQuery();
			rs=ps.executeQuery();
			if(rs.next()) {
				String database = rs.getString("database_name");
				if (database == null){
					return;
				}else {//查询当前表名是否已存在
					String querTable = "select * from information_schema.TABLES where table_schema = \""
							+ database + "\" and TABLE_NAME = \"" + tableName + "\"";
					ps=connection.prepareStatement(querTable);
					rs=ps.executeQuery();
					if(rs.next())
						System.out.println("表已存在");
					else {
						System.out.println("表不存在");
						StringBuilder creatSql = new StringBuilder("create table ");
						creatSql.append(tableName).append(" (");
						Field[] declaredFields = tableClazz.getDeclaredFields();//读取表字段
						Arrays.stream(declaredFields).forEach(e->{
							com.cxy.annotation.Field field = (com.cxy.annotation.Field) e.getAnnotation(com.cxy.annotation.Field.class);
							if (field != null){
								String value = !"".equals(field.value()) ? field.value() : e.getName();//字段名
								String type = field.type().toString();//字段类型
								boolean primary = field.primary(); //是否主键
								String len = field.len();//字段长度
								boolean nullable = field.nullable();
								creatSql.append(value).append(" ").append(type);
								if(!"".equals(len)){
									creatSql.append("(").append(len).append(")");
								}
								if(primary){
									creatSql.append(" AUTO_INCREMENT");
									creatSql.append(" PRIMARY KEY");
								}
								if(!nullable){
									creatSql.append(" NOT NULL");
								}
								creatSql.append(",");
							}
						});
						creatSql.deleteCharAt(creatSql.length()-1);
						creatSql.append(")");
						System.out.println(creatSql.toString());
//						ps=connection.prepareStatement("CREATE TABLE student2 (id INT(11) PRIMARY KEY ,name VARCHAR(25) ) " );
						ps=connection.prepareStatement(creatSql.toString());
						ps.executeUpdate();
						System.out.println("表创建成功");
					}
				}
			}
			sqlSession.commit();
		}catch (Exception e){
			e.printStackTrace();
		}

	}
}
