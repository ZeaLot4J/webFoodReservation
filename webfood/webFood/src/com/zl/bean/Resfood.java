package com.zl.bean;
import java.io.Serializable;
public class Resfood implements Serializable{
	private static final long serialVersionUID = 42569982363813432L;
	private Integer fid;
	private String fname;
	private Double normprice; 
	private Double realprice;
	private String detail;
	private String fphoto;
	private Integer up;
	private Integer down;
	
	private Integer num;//商品的数量,这个属性在数据库中没有
	
	public Integer getUp() {
		return up;
	}
	public void setUp(Integer up) {
		this.up = up;
	}
	public Integer getDown() {
		return down;
	}
	public void setDown(Integer down) {
		this.down = down;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	public Integer getFid() {
		return fid;
	}
	public void setFid(Integer fid) {
		this.fid = fid;
	}
	public String getFname() {
		return fname;
	}
	public void setFname(String fname) {
		this.fname = fname;
	}
	public Double getNormprice() {
		return normprice;
	}
	public void setNormprice(Double normprice) {
		this.normprice = normprice;
	}
	public Double getRealprice() {
		return realprice;
	}
	public void setRealprice(Double realprice) {
		this.realprice = realprice;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public String getFphoto() {
		return fphoto;
	}
	public void setFphoto(String fphoto) {
		this.fphoto = fphoto;
	}

	@Override
	public String toString() {
		return "Resfood [fid=" + fid + ", fname=" + fname + ", normprice=" + normprice + ", realprice=" + realprice
				+ ", detail=" + detail + ", fphoto=" + fphoto + ", up=" + up + ", down=" + down + ", num=" + num + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((detail == null) ? 0 : detail.hashCode());
		result = prime * result + ((down == null) ? 0 : down.hashCode());
		result = prime * result + ((fid == null) ? 0 : fid.hashCode());
		result = prime * result + ((fname == null) ? 0 : fname.hashCode());
		result = prime * result + ((fphoto == null) ? 0 : fphoto.hashCode());
		result = prime * result + ((normprice == null) ? 0 : normprice.hashCode());
		result = prime * result + ((num == null) ? 0 : num.hashCode());
		result = prime * result + ((realprice == null) ? 0 : realprice.hashCode());
		result = prime * result + ((up == null) ? 0 : up.hashCode());
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
		Resfood other = (Resfood) obj;
		if (detail == null) {
			if (other.detail != null)
				return false;
		} else if (!detail.equals(other.detail))
			return false;
		if (down == null) {
			if (other.down != null)
				return false;
		} else if (!down.equals(other.down))
			return false;
		if (fid == null) {
			if (other.fid != null)
				return false;
		} else if (!fid.equals(other.fid))
			return false;
		if (fname == null) {
			if (other.fname != null)
				return false;
		} else if (!fname.equals(other.fname))
			return false;
		if (fphoto == null) {
			if (other.fphoto != null)
				return false;
		} else if (!fphoto.equals(other.fphoto))
			return false;
		if (normprice == null) {
			if (other.normprice != null)
				return false;
		} else if (!normprice.equals(other.normprice))
			return false;
		if (num == null) {
			if (other.num != null)
				return false;
		} else if (!num.equals(other.num))
			return false;
		if (realprice == null) {
			if (other.realprice != null)
				return false;
		} else if (!realprice.equals(other.realprice))
			return false;
		if (up == null) {
			if (other.up != null)
				return false;
		} else if (!up.equals(other.up))
			return false;
		return true;
	}
	public Resfood(Integer fid, String fname, Double normprice, Double realprice, String detail, String fphoto) {
		super();
		this.fid = fid;
		this.fname = fname;
		this.normprice = normprice;
		this.realprice = realprice;
		this.detail = detail;
		this.fphoto = fphoto;
	}
	public Resfood() {
		super();
	}
}
