/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grad.proj.webApp.firstApp.results;

/**
 *
 * @author omarkrostom
 */
public class Axis {
    String title;
    Boolean grid;
    Integer offset;
    public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Boolean getGrid() {
		return grid;
	}
	public void setGrid(Boolean grid) {
		this.grid = grid;
	}
	public Integer getOffset() {
		return offset;
	}
	public void setOffset(Integer offset) {
		this.offset = offset;
	}
	public Integer getAxisWidth() {
		return axisWidth;
	}
	public void setAxisWidth(Integer axisWidth) {
		this.axisWidth = axisWidth;
	}
	public String getOrient() {
		return orient;
	}
	public void setOrient(String orient) {
		this.orient = orient;
	}
	Integer axisWidth;
    String orient;
}
