package de.huberlin.hiwaydb.dal;

import java.util.HashSet;

import javax.persistence.*;

import java.util.Set;

/**
 * Filestagingevent generated by hbm2java
 */
@Entity
public class Filestagingevent implements java.io.Serializable {

	@Id @GeneratedValue
	private Long id;
	@ManyToOne
	private Invocation invocation;
	private String name;
	private boolean in;
	private Long size;
	@OneToMany(mappedBy="filestagingevent")
	private Set<Timestat> timestats = new HashSet<Timestat>(0);
	

	public Filestagingevent() {
	}

	public Filestagingevent(Invocation invocation, boolean in) {
		this.invocation = invocation;
		this.in = in;
	}

	public Filestagingevent(Invocation invocation, String name, boolean in,
			Long size, Set timestats) {
		this.invocation = invocation;
		this.name = name;
		this.in = in;
		this.size = size;
		this.timestats = timestats;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Invocation getInvocation() {
		return this.invocation;
	}

	public void setInvocation(Invocation invocation) {
		this.invocation = invocation;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isIn() {
		return this.in;
	}

	public void setIn(boolean in) {
		this.in = in;
	}

	public Long getSize() {
		return this.size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public Set getTimestats() {
		return this.timestats;
	}

	public void setTimestats(Set timestats) {
		this.timestats = timestats;
	}

}
