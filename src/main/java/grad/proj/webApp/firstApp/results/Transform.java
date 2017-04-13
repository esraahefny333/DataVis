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
public class Transform implements Serializable {
    String filter;
    Tuple[] calculate;
	public String getFilter() {
		return filter;
	}
	public void setFilter(String filter) {
		this.filter = filter;
	}
	public Tuple[] getCalculate() {
		return calculate;
	}
	public void setCalculate(Tuple[] calculate) {
		this.calculate = calculate;
	}
}
