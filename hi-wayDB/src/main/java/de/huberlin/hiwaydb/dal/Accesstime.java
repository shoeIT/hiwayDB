package de.huberlin.hiwaydb.dal;

//Generated 19.05.2014 12:56:25 by Hibernate Tools 3.4.0.CR1

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import de.huberlin.hiwaydb.dal.Invocation;


/**
* Accesstime generated by hbm2java
*/
@Entity
public class Accesstime implements java.io.Serializable {

	@Id @GeneratedValue
	private Long id;
	
	private String funktion;
	private String input;
	private Long tick;
	private Long tock;
	private Long ticktockdif;
	private Long dbvolume;
	private Long returnvolume;
	

	public Accesstime() {
	}


	public String getFunktion() {
		return funktion;
	}


	public void setFunktion(String funktion) {
		this.funktion = funktion;
	}


	public String getInput() {
		return input;
	}


	public void setInput(String input) {
		this.input = input;
	}


	public Long getTick() {
		return tick;
	}


	public void setTick(Long tick) {
		this.tick = tick;
	}


	public Long getTock() {
		return tock;
	}


	public void setTock(Long tock) {
		this.tock = tock;
	}


	public Long getDbvolume() {
		return dbvolume;
	}


	public void setDbvolume(Long dbvolume) {
		this.dbvolume = dbvolume;
	}


	public Long getReturnvolume() {
		return returnvolume;
	}


	public void setReturnvolume(Long returnvolume) {
		this.returnvolume = returnvolume;
	}


	public Long getTicktockdif() {
		return ticktockdif;
	}


	public void setTicktockdif(Long ticktockdif) {
		this.ticktockdif = ticktockdif;
	}

	

}
