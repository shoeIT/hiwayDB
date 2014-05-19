package de.huberlin.hiwaydb.dal;

// Generated 19.05.2014 12:56:25 by Hibernate Tools 3.4.0.CR1

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * Timestat generated by hbm2java
 */
@Entity
public class Timestat implements java.io.Serializable {

	@Id @GeneratedValue
	private Long id;
	@ManyToOne
	private Invocation invocation;
	@ManyToOne
	private File file;
	private Date didOn;
	private long nminPageFault;
	private long nforcedContextSwitch;
	private long avgDataSize;
	private long nsocketRead;
	private long nioWrite;
	private long avgResidentSetSize;
	private long nmajPageFault;
	private long nwaitContextSwitch;
	private double userTime;
	private double realTime;
	private double sysTime;
	private long nsocketWrite;
	private long maxResidentSetSize;
	private long avgStackSize;
	private long nswapOutMainMem;
	private long nioRead;
	private long nsignal;
	private long avgTextSize;
	private String type;

	public Timestat() {
	}

	public Timestat(Date didOn, long nminPageFault, long nforcedContextSwitch,
			long avgDataSize, long nsocketRead, long nioWrite,
			long avgResidentSetSize, long nmajPageFault,
			long nwaitContextSwitch, double userTime, double realTime,
			double sysTime, long nsocketWrite, long maxResidentSetSize,
			long avgStackSize, long nswapOutMainMem, long nioRead,
			long nsignal, long avgTextSize, String type) {
		this.didOn = didOn;
		this.nminPageFault = nminPageFault;
		this.nforcedContextSwitch = nforcedContextSwitch;
		this.avgDataSize = avgDataSize;
		this.nsocketRead = nsocketRead;
		this.nioWrite = nioWrite;
		this.avgResidentSetSize = avgResidentSetSize;
		this.nmajPageFault = nmajPageFault;
		this.nwaitContextSwitch = nwaitContextSwitch;
		this.userTime = userTime;
		this.realTime = realTime;
		this.sysTime = sysTime;
		this.nsocketWrite = nsocketWrite;
		this.maxResidentSetSize = maxResidentSetSize;
		this.avgStackSize = avgStackSize;
		this.nswapOutMainMem = nswapOutMainMem;
		this.nioRead = nioRead;
		this.nsignal = nsignal;
		this.avgTextSize = avgTextSize;
		this.type = type;
	}

	public Timestat(Invocation invocation, File file, Date didOn,
			long nminPageFault, long nforcedContextSwitch, long avgDataSize,
			long nsocketRead, long nioWrite, long avgResidentSetSize,
			long nmajPageFault, long nwaitContextSwitch, double userTime,
			double realTime, double sysTime, long nsocketWrite,
			long maxResidentSetSize, long avgStackSize, long nswapOutMainMem,
			long nioRead, long nsignal, long avgTextSize, String type) {
		this.invocation = invocation;
		this.file = file;
		this.didOn = didOn;
		this.nminPageFault = nminPageFault;
		this.nforcedContextSwitch = nforcedContextSwitch;
		this.avgDataSize = avgDataSize;
		this.nsocketRead = nsocketRead;
		this.nioWrite = nioWrite;
		this.avgResidentSetSize = avgResidentSetSize;
		this.nmajPageFault = nmajPageFault;
		this.nwaitContextSwitch = nwaitContextSwitch;
		this.userTime = userTime;
		this.realTime = realTime;
		this.sysTime = sysTime;
		this.nsocketWrite = nsocketWrite;
		this.maxResidentSetSize = maxResidentSetSize;
		this.avgStackSize = avgStackSize;
		this.nswapOutMainMem = nswapOutMainMem;
		this.nioRead = nioRead;
		this.nsignal = nsignal;
		this.avgTextSize = avgTextSize;
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

	public File getFile() {
		return this.file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public Date getDidOn() {
		return this.didOn;
	}

	public void setDidOn(Date didOn) {
		this.didOn = didOn;
	}

	public long getNminPageFault() {
		return this.nminPageFault;
	}

	public void setNminPageFault(long nminPageFault) {
		this.nminPageFault = nminPageFault;
	}

	public long getNforcedContextSwitch() {
		return this.nforcedContextSwitch;
	}

	public void setNforcedContextSwitch(long nforcedContextSwitch) {
		this.nforcedContextSwitch = nforcedContextSwitch;
	}

	public long getAvgDataSize() {
		return this.avgDataSize;
	}

	public void setAvgDataSize(long avgDataSize) {
		this.avgDataSize = avgDataSize;
	}

	public long getNsocketRead() {
		return this.nsocketRead;
	}

	public void setNsocketRead(long nsocketRead) {
		this.nsocketRead = nsocketRead;
	}

	public long getNioWrite() {
		return this.nioWrite;
	}

	public void setNioWrite(long nioWrite) {
		this.nioWrite = nioWrite;
	}

	public long getAvgResidentSetSize() {
		return this.avgResidentSetSize;
	}

	public void setAvgResidentSetSize(long avgResidentSetSize) {
		this.avgResidentSetSize = avgResidentSetSize;
	}

	public long getNmajPageFault() {
		return this.nmajPageFault;
	}

	public void setNmajPageFault(long nmajPageFault) {
		this.nmajPageFault = nmajPageFault;
	}

	public long getNwaitContextSwitch() {
		return this.nwaitContextSwitch;
	}

	public void setNwaitContextSwitch(long nwaitContextSwitch) {
		this.nwaitContextSwitch = nwaitContextSwitch;
	}

	public double getUserTime() {
		return this.userTime;
	}

	public void setUserTime(double userTime) {
		this.userTime = userTime;
	}

	public double getRealTime() {
		return this.realTime;
	}

	public void setRealTime(double realTime) {
		this.realTime = realTime;
	}

	public double getSysTime() {
		return this.sysTime;
	}

	public void setSysTime(double sysTime) {
		this.sysTime = sysTime;
	}

	public long getNsocketWrite() {
		return this.nsocketWrite;
	}

	public void setNsocketWrite(long nsocketWrite) {
		this.nsocketWrite = nsocketWrite;
	}

	public long getMaxResidentSetSize() {
		return this.maxResidentSetSize;
	}

	public void setMaxResidentSetSize(long maxResidentSetSize) {
		this.maxResidentSetSize = maxResidentSetSize;
	}

	public long getAvgStackSize() {
		return this.avgStackSize;
	}

	public void setAvgStackSize(long avgStackSize) {
		this.avgStackSize = avgStackSize;
	}

	public long getNswapOutMainMem() {
		return this.nswapOutMainMem;
	}

	public void setNswapOutMainMem(long nswapOutMainMem) {
		this.nswapOutMainMem = nswapOutMainMem;
	}

	public long getNioRead() {
		return this.nioRead;
	}

	public void setNioRead(long nioRead) {
		this.nioRead = nioRead;
	}

	public long getNsignal() {
		return this.nsignal;
	}

	public void setNsignal(long nsignal) {
		this.nsignal = nsignal;
	}

	public long getAvgTextSize() {
		return this.avgTextSize;
	}

	public void setAvgTextSize(long avgTextSize) {
		this.avgTextSize = avgTextSize;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
