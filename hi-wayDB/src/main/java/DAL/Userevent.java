// default package
// Generated 27.04.2014 17:42:05 by Hibernate Tools 4.0.0

/**
 * Userevent generated by hbm2java
 */
public class Userevent implements java.io.Serializable {

	private Long id;
	private Invocation invocation;
	private String content;

	public Userevent() {
	}

	public Userevent(Invocation invocation, String content) {
		this.invocation = invocation;
		this.content = content;
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

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}