package dal;

// Generated 27.04.2014 18:21:34 by Hibernate Tools 3.4.0.CR1

import java.util.HashSet;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import java.util.Set;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Filestagingevent generated by hbm2java
 */
@Entity
public class Filestagingevent implements java.io.Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "invocation_fk")
	private Invocation invocation;
	
	private String name;
	private boolean in;
	private Long size;

	@OneToMany(mappedBy="invocation")
	private Set<Timestat> timestats = new HashSet<Timestat>(0);

	public Filestagingevent() {
	}

	public Filestagingevent(Invocation invocation, boolean in) {
		this.invocation = invocation;
		this.in = in;
	}

	public Filestagingevent(Invocation invocation, String name, boolean in,
			Long size, Set<Timestat> timestats) {
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

	public Set<Timestat> getTimestats() {
		return this.timestats;
	}

	public void setTimestats(Set<Timestat> timestats) {
		this.timestats = timestats;
	}

}
