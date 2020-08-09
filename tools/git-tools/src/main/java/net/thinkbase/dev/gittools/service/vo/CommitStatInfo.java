package net.thinkbase.dev.gittools.service.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

public class CommitStatInfo {
	private String id;
	private Date time;
	private String author;
	private String committer;
	private String comment;
	private List<String> diffs = new ArrayList<String>();
	
	private int linesAdded;
	private int linesDeleted;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getCommitter() {
		return committer;
	}
	public void setCommitter(String committer) {
		this.committer = committer;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	public int getLinesAdded() {
		return linesAdded;
	}

	public void setLinesAdded(int linesAdded) {
		this.linesAdded = linesAdded;
	}

	public int getLinesDeleted() {
		return linesDeleted;
	}

	public void setLinesDeleted(int linesDeleted) {
		this.linesDeleted = linesDeleted;
	}

	public List<String> getDiffs() {
		return diffs;
	}
	public void addDiff(String diff) {
		this.diffs.add(diff);
	}
	@Override
	public String toString() {
		return String.format(
				"CommitStatInfo [id=%s, time=%s, author=%s, committer=%s; \ncomment=%s, \ndiffs=%s, \nlinesAdded=%s, linesDeleted=%s]",
				id, DateFormatUtils.ISO_8601_EXTENDED_DATETIME_FORMAT.format(time),
				author, committer, comment, StringUtils.join(diffs, "\n\t"), linesAdded, linesDeleted);
	}
}
