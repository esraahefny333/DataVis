/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grad.proj.webApp.firstApp.results;

import java.io.Serializable;

/**
 *
 * @author omarkrostom
 */
public class Scale implements Serializable {
    Integer bandSize;
    Integer padding;
    public Integer getBandSize() {
		return bandSize;
	}
	public void setBandSize(Integer bandSize) {
		this.bandSize = bandSize;
	}
	public Integer getPadding() {
		return padding;
	}
	public void setPadding(Integer padding) {
		this.padding = padding;
	}
	public String[] getRange() {
		return range;
	}
	public void setRange(String[] range) {
		this.range = range;
	}
	String[] range;
}
