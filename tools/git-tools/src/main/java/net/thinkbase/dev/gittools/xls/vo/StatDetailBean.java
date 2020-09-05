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
	
	@ExcelColumn(title = "Repo", width = 8)
	private String repo;
	@ExcelColumn(title = "分支", width = 7)
	private String branch;
	
	@ExcelColumn(title = "Commit#", width = 18)
	private String id;
	@ExcelColumn(title = "提交时间", width = 9, format = "yyyy-MM-dd HH:mm:ss")
	private Date time;
	@ExcelColumn(title = "作者", width = 6)
	private String author;
	@ExcelColumn(title = "提交人", width = 6)
	private String committer;
	
	@ExcelColumn(title = "文件数", width = 4)
	private int files;
	@ExcelColumn(title = "新增行数", width = 4)
	private int linesAdded;
	@ExcelColumn(title = "删除行数", width = 4)
	private int linesDeleted;
	
	@ExcelColumn(title = "备注", width = 30)
	private String comment;
	@ExcelColumn(title = "提交文件", width = 32)
	private String diffs;
	
	@ExcelColumn(title = "合并来源", width = 8)
	private String mergeParents;

	@ExcelColumn(title = "有效性", width = 4)
	private String exValidation;

	@ExcelColumn(title = "组织", width = 6)
	private String exOrganization;

	@ExcelColumn(title = "团队", width = 6)
	private String exTeam;

	@ExcelColumn(title = "产品", width = 6)
	private String exProductLine;

	@ExcelColumn(title = "组件", width = 6)
	private String exComponent;

	@ExcelColumn(title = "阶段", width = 6)
	private String exStage;

	@ExcelColumn(title = "任务", width = 6)
	private String exTask;

	@ExcelColumn(title = "属性1", width = 6)
	private String exAttribute1;

	@ExcelColumn(title = "属性2", width = 6)
	private String exAttribute2;

	@ExcelColumn(title = "属性3", width = 6)
	private String exAttribute3;

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
	public String getMergeParents() {
		return mergeParents;
	}
	public void setMergeParents(String mergeParents) {
		this.mergeParents = mergeParents;
	}

	public static StatDetailBean fromCommitStatInfo(CommitStatInfo ci) {
		StatDetailBean bean = new StatDetailBean();
		
		bean.setRepo(Path.of(ci.getRepo()).getFileName().toString());
		
		bean.setBranch(ci.getBranch());
		bean.setId(ci.getId());
		bean.setTime(ci.getTime());
		bean.setAuthor(ci.getAuthor());
		bean.setCommitter(ci.getCommitter());
		
		String comment = ci.getComment();
		if (null!=comment && comment.length()>2000) {
			comment = comment.substring(0, 2000) + " ...";
		}
		bean.setComment(comment);
		
		List<String> diffs = ci.getDiffs();
		if (diffs.size()>3) {
			bean.setDiffs(StringUtils.join(diffs.subList(0, 3), "\n") + "\n... ...(共 "+diffs.size()+" 项)");
		}else {
			bean.setDiffs(StringUtils.join(diffs, "\n"));
		}
		
		bean.setFiles(ci.getFiles());
		bean.setLinesAdded(ci.getLinesAdded());
		bean.setLinesDeleted(ci.getLinesDeleted());
		
		List<String> parents = ci.getParents();
		if (parents.size()>1) {
			bean.setMergeParents(StringUtils.join(parents.subList(1, parents.size()), ","));
		}
		
		return bean;
	}
	
	public String getExValidation() {
		return exValidation;
	}
	public void setExValidation(String exValidation) {
		this.exValidation = exValidation;
	}
	public String getExOrganization() {
		return exOrganization;
	}
	public void setExOrganization(String exOrganization) {
		this.exOrganization = exOrganization;
	}
	public String getExTeam() {
		return exTeam;
	}
	public void setExTeam(String exTeam) {
		this.exTeam = exTeam;
	}
	public String getExProductLine() {
		return exProductLine;
	}
	public void setExProductLine(String exProductLine) {
		this.exProductLine = exProductLine;
	}
	public String getExComponent() {
		return exComponent;
	}
	public void setExComponent(String exComponent) {
		this.exComponent = exComponent;
	}
	public String getExStage() {
		return exStage;
	}
	public void setExStage(String exStage) {
		this.exStage = exStage;
	}
	public String getExTask() {
		return exTask;
	}
	public void setExTask(String exTask) {
		this.exTask = exTask;
	}
	public String getExAttribute1() {
		return exAttribute1;
	}
	public void setExAttribute1(String exAttribute1) {
		this.exAttribute1 = exAttribute1;
	}
	public String getExAttribute2() {
		return exAttribute2;
	}
	public void setExAttribute2(String exAttribute2) {
		this.exAttribute2 = exAttribute2;
	}
	public String getExAttribute3() {
		return exAttribute3;
	}
	public void setExAttribute3(String exAttribute3) {
		this.exAttribute3 = exAttribute3;
	}
}
