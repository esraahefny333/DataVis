/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grad.proj.webApp.firstApp.results;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author omarkrostom
 */
@XmlRootElement
public class Result implements Serializable {
    Url data;
    Transform transform;
    String mark;
    Encoding encoding;
    public Url getData() {
		return data;
	}
	public void setData(Url data) {
		this.data = data;
	}
	public Transform getTransform() {
		return transform;
	}
	public void setTransform(Transform transform) {
		this.transform = transform;
	}
	public String getMark() {
		return mark;
	}
	public void setMark(String mark) {
		this.mark = mark;
	}
	public Encoding getEncoding() {
		return encoding;
	}
	public void setEncoding(Encoding encoding) {
		this.encoding = encoding;
	}
	public Config getConfig() {
		return config;
	}
	public void setConfig(Config config) {
		this.config = config;
	}
	Config config;
}
