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
 * 1. �ӿ�ʽ���
 * ԭ��: 			Dao 	===> DaoImpl
 * mybatis: 	Mapper 	===> xxMapper.xml
 * 
 * 2. SqlSession ��������ݿ��һ�λỰ, �������ر�.
 * 3. SqlSession �� Connection һ�����Ƿ��̰߳�ȫ��.
 * 4. mapper �ӿ�û��ʵ����, ���� mybatis ��Ϊ����ӿ�����һ���������.
 * 		(���ӿ� �� xml ���а�)
 * 		EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);
 * 5. ������Ҫ�������ļ�:
 * 		mybatis ��ȫ�������ļ�: �������ݿ����ӳ���Ϣ, �����������Ϣ��... ϵͳ���л�����Ϣ
 * 		sqlӳ���ļ�:������ÿһ�� sql ����ӳ����Ϣ:
 * 					�� sql ��ȡ����
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
	 * 1. ����xml�����ļ� (ȫ�������ļ�) ����һ�� SqlSessionFactory ����
	 * 		������ԴһЩ���л�����Ϣ
	 * 2. sql ӳ���ļ�:������ÿһ�� sql, �Լ� sql �ķ�װ����ȡ�
	 * 3. �� sql ӳ���ļ�ע����ȫ�������ļ���
	 * 4. д����:
	 * 		 1). ����ȫ�������ļ��õ� SqlSessionFactory
	 * 		 2). ʹ�� sqlSession ����, ��ȡ sqlSession ����ʹ������ִ����ɾ�Ĳ�
	 * 			 һ�� sqlSession ���Ǵ�������ݿ��һ�λỰ, ����ر�
	 * 		 3). ʹ�� sql ��Ψһ��ʶ������ MyBatis ִ���ĸ� sql��sql ���Ǳ����� sql ӳ���ļ��еġ�
	 * @throws IOException
	 */
	@Test
	public void test() throws IOException{
		String resource = "mybatis-conf.xml";
		InputStream inputStream = Resources.getResourceAsStream(resource);
		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
		
		//2.��ȡsqlSessionʵ��,��ֱ��ִ���Ѿ�ӳ���sql���
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
		//1. ��ȡsqlSessionFactory����
		SqlSessionFactory sessionFactory = getSqlSessionFactory();
		
		//2. ��ȡsqlSession����
		SqlSession session = sessionFactory.openSession();
		
		try {
			
			//3.��ȡ�ӿڵ�ʵ�������
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
		//1. ��ȡsqlSessionFactory����
		SqlSessionFactory sessionFactory = getSqlSessionFactory();
		
		//2. ��ȡsqlSession����
		SqlSession session = sessionFactory.openSession();
		
		try {
			
			//3.��ȡ�ӿڵ�ʵ�������
			EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);
			
			Page<Object> page = PageHelper.startPage(1,1);
			
			
			List<Employee> emps = mapper.getEmps();
			
			//����Ҫ������ʾ����ҳ
			PageInfo<Employee> info = new PageInfo<>(emps,5);
			
			for (Employee employee : emps) {
				System.out.println(employee);
			}
			/*System.out.println("��ǰҳ��"+page.getPageNum());
			System.out.println("�ܼ�¼��"+page.getTotal());
			System.out.println("ÿҳ��¼��"+page.getPageSize());
			System.out.println("��ҳ��"+page.getPages());*/
			
			
			System.out.println("��ǰҳ��"+info.getPageNum());
			System.out.println("�ܼ�¼��"+info.getTotal());
			System.out.println("ÿҳ��¼��"+info.getPageSize());
			System.out.println("��ҳ��"+info.getPages());
			System.out.println("�Ƿ��һҳ"+info.isIsFirstPage());
			System.out.println("������ʾ��ҳ��:");
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
		//����ִ������������sqlSession
		SqlSession openSession = sqlSessionFactory.openSession(ExecutorType.BATCH);
		long start = System.currentTimeMillis();
		try {
			
			EmployeeMapper mapper = openSession.getMapper(EmployeeMapper.class);
			for (int i = 0; i < 10000; i++) {
				
				mapper.addEmp(new Employee(UUID.randomUUID().toString().substring(0, 5),"a","1"));
			}
			long end = System.currentTimeMillis();
			//����:(Ԥ����sqlһ��==>���ò���==>10000��==>ִ��һ��)
			//ִ��ʱ��: 2842
			//������:(Ԥ����sql==>���ò���==>ִ��)==>10000
			//ִ��ʱ��: 10310
			System.out.println("ִ��ʱ��: " + (end-start));
		} finally {
			openSession.close();
		}
		
	}
	/**
	 * oracle ��ҳ
	 * 		����rownum: �к�;�Ӳ�ѯ;
	 * �洢���̰�װ��ҳ�߼�
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
			
			System.out.println("�ܼ�¼��:" + page.getCount());
			System.out.println("��ѯ������" + page.getEmps().size());
			System.out.println("���������" + page.getEmps());
		} finally {
			openSession.close();
		}
	}
	
	/**
	 * Ĭ�� mybatis �ڴ���ö�ٶ����ʱ�򱣴����ö�ٵ�����: EnumTypeHandler
	 * �ı�ʹ�� EnumOrdinalTypeHandler
	 */
	@Test
	public void testEnumUse(){
		EmpStatus login  = EmpStatus.LOGIN;
		System.out.println("ö�ٵ�����: "+ login.ordinal());
		System.out.println("ö�ٵ�����: "+ login.name());
		
		System.out.println("ö�ٵ�״̬��: " + login.getCode());
		System.out.println("ö�ٵ���ʾ��Ϣ: "+ login.getMsg());
	}
	
	@Test
	public void testEnum() throws IOException{
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
		SqlSession openSession = sqlSessionFactory.openSession();
		try {
			EmployeeMapper mapper = openSession.getMapper(EmployeeMapper.class);
			Employee employee = new Employee("test_enum","enum@qq.com","1");
//			mapper.addEmp(employee);
//			System.out.println("����ɹ�"+employee.getId());
//			openSession.commit();
			Employee empById = mapper.getEmpById(10022);
			System.out.println(empById.getEmpStatus());
		} finally {
			openSession.close();
		}
	}
}
