package net.thinkbase.dev.gittools.xls.vo;

import java.nio.file.Path;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.github.liaochong.myexcel.core.annotation.ExcelColumn;
import com.github.liaochong.myexcel.core.annotation.ExcelModel;

import net.thinkbase.dev.gittools.service.vo.CommitStatInfo;

@ExcelModel(sheetName = StatDetailBean.SHEET_NAME,
            style={"title->font-size:10px;font-weight:bold;text-align:center;vertical-align:center;background-color:#F0F8FF;",
            		"cell->font-size:10px;vertical-align:center;"})
public class StatDetailBean {
	public static final String SHEET_NAME="明细统计信息";
	
	@ExcelColumn(order = 1, title = "Repo", width = 8)
	private String repo;
	@ExcelColumn(order = 2, title = "分支", width = 7)
	private String branch;
	
	@ExcelColumn(order = 3, title = "Commit#", width = 18)
	private String id;
	@ExcelColumn(order = 4, title = "提交时间", width = 9, format = "yyyy-MM-dd HH:mm:ss")
	private Date time;
	@ExcelColumn(order = 5, title = "作者", width = 6)
	private String author;
	@ExcelColumn(order = 6, title = "提交人", width = 6)
	private String committer;
	
	@ExcelColumn(order = 7, title = "文件数", width = 4)
	private int files;
	@ExcelColumn(order = 8, title = "新增行数", width = 4)
	private int linesAdded;
	@ExcelColumn(order = 9, title = "删除行数", width = 4)
	private int linesDeleted;
	
	@ExcelColumn(order = 10, title = "备注", width = 30)
	private String comment;
	@ExcelColumn(order = 11, title = "提交文件", width = 32)
	private String diffs;

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
	public String getDiffs() {
		return diffs;
	}
	public void setDiffs(String diffs) {
		this.diffs = diffs;
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

	public static StatDetailBean fromCommitStatInfo(CommitStatInfo ci) {
		StatDetailBean bean = new StatDetailBean();
		
		bean.setRepo(Path.of(ci.getRepo()).getFileName().toString());
		
		bean.setBranch(ci.getBranch());
		bean.setId(ci.getId());
		bean.setTime(ci.getTime());
		bean.setAuthor(ci.getAuthor());
		bean.setCommitter(ci.getCommitter());
		bean.setComment(ci.getComment());
		
		List<String> diffs = ci.getDiffs();
		if (diffs.size()>3) {
			bean.setDiffs(StringUtils.join(diffs.subList(0, 3), "\n") + "\n... ...(共 "+diffs.size()+" 项)");
		}else {
			bean.setDiffs(StringUtils.join(diffs, "\n"));
		}
		
		bean.setFiles(ci.getFiles());
		bean.setLinesAdded(ci.getLinesAdded());
		bean.setLinesDeleted(ci.getLinesDeleted());
		
		return bean;
	}
}
