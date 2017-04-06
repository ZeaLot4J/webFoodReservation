package com.zl.bean;

import java.io.Serializable;

public class Resorderitem implements Serializable{
	private static final long serialVersionUID = -3159278794952914913L;
	private Integer roiid;
	private Integer roid;
	private Integer fid;
	private Double dealprice;
	private Integer num;
	public Resorderitem() {
		super();
	}
	public Integer getRoiid() {
		return roiid;
	}
	public void setRoiid(Integer roiid) {
		this.roiid = roiid;
	}
	public Integer getRoid() {
		return roid;
	}
	public void setRoid(Integer roid) {
		this.roid = roid;
	}
	public Integer getFid() {
		return fid;
	}
	public void setFid(Integer fid) {
		this.fid = fid;
	}
	public Double getDealprice() {
		return dealprice;
	}
	public void setDealprice(Double dealprice) {
		this.dealprice = dealprice;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	@Override
	public String toString() {
		return "Resorderitem [roiid=" + roiid + ", roid=" + roid + ", fid=" + fid + ", dealprice=" + dealprice
				+ ", num=" + num + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dealprice == null) ? 0 : dealprice.hashCode());
		result = prime * result + ((fid == null) ? 0 : fid.hashCode());
		result = prime * result + ((num == null) ? 0 : num.hashCode());
		result = prime * result + ((roid == null) ? 0 : roid.hashCode());
		result = prime * result + ((roiid == null) ? 0 : roiid.hashCode());
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
		Resorderitem other = (Resorderitem) obj;
		if (dealprice == null) {
			if (other.dealprice != null)
				return false;
		} else if (!dealprice.equals(other.dealprice))
			return false;
		if (fid == null) {
			if (other.fid != null)
				return false;
		} else if (!fid.equals(other.fid))
			return false;
		if (num == null) {
			if (other.num != null)
				return false;
		} else if (!num.equals(other.num))
			return false;
		if (roid == null) {
			if (other.roid != null)
				return false;
		} else if (!roid.equals(other.roid))
			return false;
		if (roiid == null) {
			if (other.roiid != null)
				return false;
		} else if (!roiid.equals(other.roiid))
			return false;
		return true;
	}
	
	
}
