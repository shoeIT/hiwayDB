package de.huberlin.hiwaydb.dal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

// Generated 19.05.2014 12:56:25 by Hibernate Tools 3.4.0.CR1

/**
 * Inoutput generated by hbm2java
 */
@Entity
public class Inoutput implements java.io.Serializable {

	@Id @GeneratedValue
	private Long id;
	@ManyToOne
	private Invocation invocation;
	private String keypart;
	private String content;
	private String type;

	public Inoutput() {
	}

	public Inoutput(Invocation invocation, String keypart, String content,
			String type) {
		this.invocation = invocation;
		this.keypart = keypart;
		this.content = content;
		this.type = type;
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

	public String getKeypart() {
		return this.keypart;
	}

	public void setKeypart(String keypart) {
		this.keypart = keypart;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
