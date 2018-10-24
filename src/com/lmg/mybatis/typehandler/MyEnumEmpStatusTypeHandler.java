package com.lmg.mybatis.typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import com.lmg.mybatis.bean.EmpStatus;

/**
 * 1.ʵ�� TypeHandler �ӿ�. ���߼̳� BaseTypeHandler
 * @author Administrator
 *
 */
public class MyEnumEmpStatusTypeHandler implements TypeHandler<EmpStatus>{

	/**
	 * ���嵱ǰ���ݿ���α��浽���ݿ�
	 */
	@Override
	public void setParameter(PreparedStatement ps, int i, EmpStatus parameter, JdbcType jdbcType) throws SQLException {
		// TODO Auto-generated method stub
		System.out.println("Ҫ�����״̬��: " + parameter.getCode());
		ps.setString(i, parameter.getCode().toString());
	}

	@Override
	public EmpStatus getResult(ResultSet rs, String columnName) throws SQLException {
		// TODO Auto-generated method stub
		//��Ҫ���ݴ����ݿ��õ���ö��״̬�뷵��һ��ö�ٶ���
		int code = rs.getInt(columnName);
		System.out.println("�����ݿ��л�ȡ��״̬��"+ code);
		EmpStatus status = EmpStatus.getEmpStatusByCode(code);
		return status;
	}

	@Override
	public EmpStatus getResult(ResultSet rs, int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		int code = rs.getInt(columnIndex);
		System.out.println("�����ݿ��л�ȡ��״̬��"+ code);
		EmpStatus status = EmpStatus.getEmpStatusByCode(code);
		return status;
	}

	@Override
	public EmpStatus getResult(CallableStatement cs, int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		int code = cs.getInt(columnIndex);
		System.out.println("�����ݿ��л�ȡ��״̬��"+ code);
		EmpStatus status = EmpStatus.getEmpStatusByCode(code);
		return status;
	}
	
}
