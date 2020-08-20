package com.dili.gw.uap;

import com.dili.ss.dto.IDTO;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;

import java.util.Date;

public interface UserTicket extends IDTO {
	@FieldDef(label="主键")
	@EditMode(editor = FieldEditor.Number, required = true)
	Long getId();

	void setId(Long id);

	@FieldDef(label="用户名", maxLength = 50)
	@EditMode(editor = FieldEditor.Text, required = true)
	String getUserName();

	void setUserName(String userName);

	@FieldDef(label="密码", maxLength = 128)
	@EditMode(editor = FieldEditor.Text, required = true)
	String getPassword();

	void setPassword(String password);

	@FieldDef(label="归属市场编码", maxLength = 50)
	@EditMode(editor = FieldEditor.Text, required = false)
	String getFirmCode();

	void setFirmCode(String firmCode);

	@FieldDef(label="归属市场ID", maxLength = 20)
	@EditMode(editor = FieldEditor.Text, required = false)
	Long getFirmId();

	void setFirmId(Long firmId);

	@FieldDef(label="归属市场名称", maxLength = 20)
	@EditMode(editor = FieldEditor.Text)
	String getFirmName();

	void setFirmName(String firmName);

	@FieldDef(label="归属部门")
	@EditMode(editor = FieldEditor.Number, required = false)
	Long getDepartmentId();

	void setDepartmentId(Long departmentId);

	@FieldDef(label="职位", maxLength = 20)
	@EditMode(editor = FieldEditor.Text, required = false)
	String getPosition();

	void setPosition(String position);

	@FieldDef(label="卡号", maxLength = 20)
	@EditMode(editor = FieldEditor.Text, required = false)
	String getCardNumber();

	void setCardNumber(String cardNumber);

	@FieldDef(label="创建时间")
	@EditMode(editor = FieldEditor.Datetime, required = true)
	Date getCreated();

	void setCreated(Date created);

	@FieldDef(label="修改时间")
	@EditMode(editor = FieldEditor.Datetime, required = true)
	Date getModified();

	void setModified(Date modified);

	@FieldDef(label="状态")
	@EditMode(editor = FieldEditor.Number, required = true)
	Integer getState();

	void setState(Integer state);

	@FieldDef(label="真实姓名", maxLength = 64)
	@EditMode(editor = FieldEditor.Text, required = true)
	String getRealName();

	void setRealName(String realName);

	@FieldDef(label="用户编号", maxLength = 128)
	@EditMode(editor = FieldEditor.Text, required = false)
	String getSerialNumber();

	void setSerialNumber(String serialNumber);

	@FieldDef(label="手机号码", maxLength = 24)
	@EditMode(editor = FieldEditor.Text, required = false)
	String getCellphone();

	void setCellphone(String cellphone);

	@FieldDef(label="邮箱", maxLength = 64)
	@EditMode(editor = FieldEditor.Text, required = false)
	String getEmail();

	void setEmail(String email);

	@FieldDef(label="锁定时间")
	@EditMode(editor = FieldEditor.Datetime, required = true)
	Date getLocked();

	void setLocked(Date locked);

}
