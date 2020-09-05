package net.thinkbase.dev.gittools.service.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffEntry.ChangeType;

public class CommitStatInfo {
	private String repo;
	private String branch;
	
	private String id;
	private Date time;
	private String author;
	private String committer;
	private String comment;
	private List<String> diffs = new ArrayList<>();
	private List<String> parents = new ArrayList<>();
	
	private int files;
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

	public int getFiles() {
		return files;
	}
	public void setFiles(int files) {
		this.files = files;
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

	public List<String> getParents() {
		return parents;
	}

	public void addParent(String parentId) {
		this.parents.add(parentId);
	}

	public List<String> getDiffs() {
		return diffs;
	}
	
	public void addDiff(DiffEntry diff) {
		String diffInfo = buildDiffInfo(diff);
		this.diffs.add(diffInfo);
	}
	public void addDiff(DiffEntry diff, int lineAdded, int lineDeleted) {
		String diffInfo = buildDiffInfo(diff)+", "+lineAdded+"+, "+lineDeleted+"-";
		this.diffs.add(diffInfo);
	}
	/**
	 * Summary as {@link DiffEntry#toString()} but skip leading "DiffEntry"
	 * @param diff
	 */
	private String buildDiffInfo(DiffEntry diff) {
		ChangeType changeType = diff.getChangeType();
		String newPath = diff.getNewPath();
		String oldPath = diff.getOldPath();
		
		StringBuilder buf = new StringBuilder();
		buf.append("[");
		buf.append(changeType);
		buf.append(" ");
		switch (changeType) {
		case ADD:
			buf.append(newPath);
			break;
		case COPY:
			buf.append(oldPath + "->>" + newPath);
			break;
		case DELETE:
			buf.append(oldPath);
			break;
		case MODIFY:
			buf.append(oldPath);
			break;
		case RENAME:
			buf.append(oldPath + "-->" + newPath);
			break;
		}
		buf.append("]");
		String diffInfo = buf.toString();
		return diffInfo;
	}

	public String getRepo() {
		return repo;
	}
	public void setRepo(String repo) {
		this.repo = repo;
	}
	public String getBranch() {
		return branch;
	}
	public void setBranch(String branch) {
		this.branch = branch;
	}

	@Override
	public String toString() {
		return String.format(
				"CommitStatInfo [repo=%s, branch=%s,"
				+ "\nid=%s, time=%s, author=%s, committer=%s;"
				+ "\ncomment=%s,"
				+ "\ndiffs=%s,"
				+ "\nfiles=%s, linesAdded=%s, linesDeleted=%s]",
				repo, branch,
				id, DateFormatUtils.ISO_8601_EXTENDED_DATETIME_FORMAT.format(time), author, committer,
				comment,
				StringUtils.join(diffs, "\n\t"),
				files, linesAdded, linesDeleted);
	}
}
