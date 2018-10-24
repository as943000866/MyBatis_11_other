package com.lmg.mybatis.test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lmg.mybatis.bean.EmpStatus;
import com.lmg.mybatis.bean.Employee;
import com.lmg.mybatis.bean.OraclePage;
import com.lmg.mybatis.dao.EmployeeMapper;

/**
 * 1. 接口式编程
 * 原生: 			Dao 	===> DaoImpl
 * mybatis: 	Mapper 	===> xxMapper.xml
 * 
 * 2. SqlSession 代表和数据库的一次会话, 用完必须关闭.
 * 3. SqlSession 和 Connection 一样都是非线程安全的.
 * 4. mapper 接口没有实现类, 但是 mybatis 会为这个接口生成一个代理对象.
 * 		(将接口 和 xml 进行绑定)
 * 		EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);
 * 5. 两个重要的配置文件:
 * 		mybatis 的全局配置文件: 包含数据库连接池信息, 事务管理器信息等... 系统运行环境信息
 * 		sql映射文件:保存了每一个 sql 语句的映射信息:
 * 					将 sql 抽取出来
 *   
 * @author Administrator
 *
 */
public class MyBatisTest {
	
	private SqlSessionFactory getSqlSessionFactory() throws IOException {
		String resource = "mybatis-conf.xml";
		InputStream inputStream = Resources.getResourceAsStream(resource);
		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
		return sqlSessionFactory;
	}
	/**
	 * 1. 根据xml配置文件 (全局配置文件) 创建一个 SqlSessionFactory 对象
	 * 		有数据源一些运行环境信息
	 * 2. sql 映射文件:配置了每一个 sql, 以及 sql 的封装规则等。
	 * 3. 将 sql 映射文件注册在全局配置文件中
	 * 4. 写代码:
	 * 		 1). 根据全局配置文件得到 SqlSessionFactory
	 * 		 2). 使用 sqlSession 工厂, 获取 sqlSession 对象使用他来执行增删改查
	 * 			 一个 sqlSession 就是代表和数据库的一次会话, 用完关闭
	 * 		 3). 使用 sql 的唯一标识来告诉 MyBatis 执行哪个 sql。sql 都是保存在 sql 映射文件中的。
	 * @throws IOException
	 */
	@Test
	public void test() throws IOException{
		String resource = "mybatis-conf.xml";
		InputStream inputStream = Resources.getResourceAsStream(resource);
		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
		
		//2.获取sqlSession实例,能直接执行已经映射的sql语句
		//statement Unique identifier matching the statement to use.
		//parameter A parameter object to pass to the statement.
		
		SqlSession session = sqlSessionFactory.openSession();
		try {
			Employee employee = session.selectOne("com.lmg.mybatis.EmployeeMapper.selectEmp", 1);
			System.out.println(employee);
		} finally {
			session.close();
			
		}
		
	}
	
	@Test
	public void test01() throws IOException{
		//1. 获取sqlSessionFactory对象
		SqlSessionFactory sessionFactory = getSqlSessionFactory();
		
		//2. 获取sqlSession对象
		SqlSession session = sessionFactory.openSession();
		
		try {
			
			//3.获取接口的实现类对象
			EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);
			
			Employee employee = mapper.getEmpById(1);
			System.out.println(mapper.getClass());
			System.out.println(employee);
		} finally {
			
			session.close();
		}
		
	}
	
	@Test
	public void test02() throws IOException{
		//1. 获取sqlSessionFactory对象
		SqlSessionFactory sessionFactory = getSqlSessionFactory();
		
		//2. 获取sqlSession对象
		SqlSession session = sessionFactory.openSession();
		
		try {
			
			//3.获取接口的实现类对象
			EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);
			
			Page<Object> page = PageHelper.startPage(1,1);
			
			
			List<Employee> emps = mapper.getEmps();
			
			//传入要连续显示多少页
			PageInfo<Employee> info = new PageInfo<>(emps,5);
			
			for (Employee employee : emps) {
				System.out.println(employee);
			}
			/*System.out.println("当前页码"+page.getPageNum());
			System.out.println("总记录数"+page.getTotal());
			System.out.println("每页记录数"+page.getPageSize());
			System.out.println("总页码"+page.getPages());*/
			
			
			System.out.println("当前页码"+info.getPageNum());
			System.out.println("总记录数"+info.getTotal());
			System.out.println("每页记录数"+info.getPageSize());
			System.out.println("总页码"+info.getPages());
			System.out.println("是否第一页"+info.isIsFirstPage());
			System.out.println("连续显示的页码:");
			int[] nums = info.getNavigatepageNums();
			for (int i = 0; i < nums.length; i++) {
				System.out.println(nums[i]);
			}
		} finally {
			
			session.close();
		}
		
	}
	
	@Test
	public void testBatch() throws IOException{
		
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
		//可以执行批量操作的sqlSession
		SqlSession openSession = sqlSessionFactory.openSession(ExecutorType.BATCH);
		long start = System.currentTimeMillis();
		try {
			
			EmployeeMapper mapper = openSession.getMapper(EmployeeMapper.class);
			for (int i = 0; i < 10000; i++) {
				
				mapper.addEmp(new Employee(UUID.randomUUID().toString().substring(0, 5),"a","1"));
			}
			long end = System.currentTimeMillis();
			//批量:(预编译sql一次==>设置参数==>10000次==>执行一次)
			//执行时长: 2842
			//非批量:(预编译sql==>设置参数==>执行)==>10000
			//执行时长: 10310
			System.out.println("执行时长: " + (end-start));
		} finally {
			openSession.close();
		}
		
	}
	/**
	 * oracle 分页
	 * 		借助rownum: 行号;子查询;
	 * 存储过程包装分页逻辑
	 * @throws IOException 
	 * 
	 */
	@Test
	public void testProcedure() throws IOException{
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
		SqlSession openSession = sqlSessionFactory.openSession();
		try {
			EmployeeMapper mapper = openSession.getMapper(EmployeeMapper.class);
			OraclePage page = new OraclePage();
			page.setStart(1);
			page.setEnd(5);
			mapper.getPageByProcedure(page);
			
			System.out.println("总记录数:" + page.getCount());
			System.out.println("查询的数据" + page.getEmps().size());
			System.out.println("查出的数据" + page.getEmps());
		} finally {
			openSession.close();
		}
	}
	
	/**
	 * 默认 mybatis 在处理枚举对象的时候保存的是枚举的名字: EnumTypeHandler
	 * 改变使用 EnumOrdinalTypeHandler
	 */
	@Test
	public void testEnumUse(){
		EmpStatus login  = EmpStatus.LOGIN;
		System.out.println("枚举的索引: "+ login.ordinal());
		System.out.println("枚举的名字: "+ login.name());
		
		System.out.println("枚举的状态码: " + login.getCode());
		System.out.println("枚举的提示消息: "+ login.getMsg());
	}
	
	@Test
	public void testEnum() throws IOException{
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
		SqlSession openSession = sqlSessionFactory.openSession();
		try {
			EmployeeMapper mapper = openSession.getMapper(EmployeeMapper.class);
			Employee employee = new Employee("test_enum","enum@qq.com","1");
//			mapper.addEmp(employee);
//			System.out.println("保存成功"+employee.getId());
//			openSession.commit();
			Employee empById = mapper.getEmpById(10022);
			System.out.println(empById.getEmpStatus());
		} finally {
			openSession.close();
		}
	}
}
