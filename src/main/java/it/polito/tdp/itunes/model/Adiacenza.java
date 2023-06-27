package it.polito.tdp.itunes.model;

import java.util.Objects;

public class Adiacenza {
	
	public Adiacenza(Track v1, Track v2) {
		super();
		this.v1 = v1;
		this.v2 = v2;
	}
	private Track v1;
	private Track v2;
	public Track getV1() {
		return v1;
	}
	public void setV1(Track v1) {
		this.v1 = v1;
	}
	public Track getV2() {
		return v2;
	}
	public void setV2(Track v2) {
		this.v2 = v2;
	}
	@Override
	public int hashCode() {
		return Objects.hash(v1, v2);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Adiacenza other = (Adiacenza) obj;
		return Objects.equals(v1, other.v1) && Objects.equals(v2, other.v2);
	}
	

}
