package sjsu.cloud.cohort10.dto;

import java.io.Serializable;

public class JobsCountResponse implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Integer iu;
	private Integer ib;
	private Integer is;
	private Integer pu;
	private Integer pb;
	private Integer ps;
	private Integer fu;
	private Integer fb;
	private Integer fs;
	public Integer getIu() {
		return iu;
	}
	public void setIu(Integer iu) {
		this.iu = iu;
	}
	public Integer getIb() {
		return ib;
	}
	public void setIb(Integer ib) {
		this.ib = ib;
	}
	public Integer getIs() {
		return is;
	}
	public void setIs(Integer is) {
		this.is = is;
	}
	public Integer getPu() {
		return pu;
	}
	public void setPu(Integer pu) {
		this.pu = pu;
	}
	public Integer getPb() {
		return pb;
	}
	public void setPb(Integer pb) {
		this.pb = pb;
	}
	public Integer getPs() {
		return ps;
	}
	public void setPs(Integer ps) {
		this.ps = ps;
	}
	public Integer getFu() {
		return fu;
	}
	public void setFu(Integer fu) {
		this.fu = fu;
	}
	public Integer getFb() {
		return fb;
	}
	public void setFb(Integer fb) {
		this.fb = fb;
	}
	public Integer getFs() {
		return fs;
	}
	public void setFs(Integer fs) {
		this.fs = fs;
	}
}
