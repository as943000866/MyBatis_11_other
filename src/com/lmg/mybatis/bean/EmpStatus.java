package com.lmg.mybatis.bean;

/**
 * ϣ�����ݿⱣ����� 100,200 ��Щ״̬��,������Ĭ�� 0,1 ����ö�ٵ���
 * @author Administrator
 *
 */
public enum EmpStatus {
	LOGIN(100,"�û���¼"),LOGOUT(200,"�û��ǳ�"),REMOVE(300,"�û�������");
	
	private Integer code;
	private String msg;
	private EmpStatus(Integer code,String msg){
		this.code = code;
		this.msg = msg;
	}
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	//����״̬�뷵��ö�ٶ���
	public static EmpStatus getEmpStatusByCode(Integer code){
		switch (code) {
		case 100:
			return LOGIN;
		case 200:
			return LOGOUT;
		case 300:
			return REMOVE;
		default:
			return LOGOUT;
		}
	}
}
