package com.zl.bean;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
public class Resorder implements Serializable{
	private static final long serialVersionUID = -145294066792056500L;
	private Integer roid;
	private Integer userid;
	private String address;
	private String tel;
	private String ordertime;
	private String deliverytime;
	private String ps;
	private Integer status;
	public Resorder(Integer roid, Integer userid, String address, String tel, String ordertime, String deliverytime,
			String ps, Integer status) {
		super();
		this.roid = roid;
		this.userid = userid;
		this.address = address;
		this.tel = tel;
		this.ordertime = ordertime;
		this.deliverytime = deliverytime;
		this.ps = ps;
		this.status = status;
	}
	public Resorder() {
		super();
	}
	public Integer getRoid() {
		return roid;
	}
	public void setRoid(Integer roid) {
		this.roid = roid;
	}
	public Integer getUserid() {
		return userid;
	}
	public void setUserid(Integer userid) {
		this.userid = userid;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getOrdertime() {
		return ordertime;
	}
	public void setOrdertime(String ordertime) {
		this.ordertime = ordertime;
	}
	public String getDeliverytime() {
		return deliverytime;
	}
	public String getFormatDeliverytime() {
		//把输入的日期格式转换成下面的那种，让数据库可以接受
		DateFormat df = new SimpleDateFormat("yyyy年MM月dd日HH:mm");
		Date date = null;
		try {
			date = df.parse(deliverytime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return df.format(date);
	}
	public void setDeliverytime(String deliverytime) {
		this.deliverytime = deliverytime;
	}
	public String getPs() {
		return ps;
	}
	public void setPs(String ps) {
		this.ps = ps;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "Resorder [roid=" + roid + ", userid=" + userid + ", address=" + address + ", tel=" + tel
				+ ", ordertime=" + ordertime + ", deliverytime=" + deliverytime + ", ps=" + ps + ", status=" + status
				+ "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		result = prime * result + ((deliverytime == null) ? 0 : deliverytime.hashCode());
		result = prime * result + ((ordertime == null) ? 0 : ordertime.hashCode());
		result = prime * result + ((ps == null) ? 0 : ps.hashCode());
		result = prime * result + ((roid == null) ? 0 : roid.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((tel == null) ? 0 : tel.hashCode());
		result = prime * result + ((userid == null) ? 0 : userid.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Resorder other = (Resorder) obj;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
		if (deliverytime == null) {
			if (other.deliverytime != null)
				return false;
		} else if (!deliverytime.equals(other.deliverytime))
			return false;
		if (ordertime == null) {
			if (other.ordertime != null)
				return false;
		} else if (!ordertime.equals(other.ordertime))
			return false;
		if (ps == null) {
			if (other.ps != null)
				return false;
		} else if (!ps.equals(other.ps))
			return false;
		if (roid == null) {
			if (other.roid != null)
				return false;
		} else if (!roid.equals(other.roid))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		if (tel == null) {
			if (other.tel != null)
				return false;
		} else if (!tel.equals(other.tel))
			return false;
		if (userid == null) {
			if (other.userid != null)
				return false;
		} else if (!userid.equals(other.userid))
			return false;
		return true;
	}
}
