import org.apache.commons.lang3.time.DateFormatUtils

import net.thinkbase.dev.gittools.service.vo.CommitStatInfo
import net.thinkbase.dev.gittools.xls.vo.StatDetailBean

/**
 * exValidation;    //有效性
 * exOrganization;  //组织
 * exTeam;          //团队
 * exProductLine;   //产品
 * exComponent;     //组件
 * exStage;         //阶段
 * exTask;          //任务
 * exAttribute1;    //分类1
 * exAttribute2;    //分类2
 * exAttribute3;    //分类3
 * 
 * git clone https://github.com/vuejs/cn.vuejs.org.git
 * git clone https://github.com/bailicangdu/vue2-manage.git
 * git clone https://github.com/mozilla/rhino.git
 * git clone https://github.com/ehcache/ehcache3.git
 */
class GitToolsEx {

	def run(CommitStatInfo ci, StatDetailBean si) {
		if (ci.parents.size()>1) {
			si.exValidation = "N";
		}
		
		if (ci.repo ==~ /.*vue.*/) {
			si.exOrganization = "vuejs";
		}else if (ci.repo ==~ /.*rhino.*/) {
			si.exOrganization = "Mozilla";
		}else if (ci.repo ==~ /.*ehcache.*/) {
			si.exOrganization = "EhCache";
		}
		
		if (ci.repo == "cn.vuejs.org") {
			si.exTeam = "中文";
		}
		
		if (ci.repo ==~ /.*vue.*/) {
			si.exProductLine = "Web";
		}else if (ci.repo ==~ /.*rhino.*/) {
			si.exProductLine = "Java";
		}else if (ci.repo ==~ /.*ehcache.*/) {
			si.exProductLine = "Java";
		}
		
		if (ci.repo ==~ /.*vue.*/) {
			si.exComponent = "MVVC";
		}else if (ci.repo ==~ /.*rhino.*/) {
			si.exComponent = "JavaScript";
		}else if (ci.repo ==~ /.*ehcache.*/) {
			si.exComponent = "Cache";
		}
		
		def _y = DateFormatUtils.format(ci.getTime(), "yyyy");
		def _Q = QuarterTable.get(DateFormatUtils.format(ci.getTime(), "MM"));
		si.exStage = "${_y}-${_Q}";
		
		si.exTask = resolverTask(ci.comment);
		
		if (ci.comment.startsWith("Merge")) {
			si.exAttribute1 = "Merge";
		}else if (ci.comment.startsWith("Fix")) {
			si.exAttribute1 = "Fix";
		}
		
		if (ci.files!=0 && (ci.linesAdded+ci.linesDeleted)/ci.files > 100) {
			si.exAttribute2 = "大量修改";
		}
		if (ci.files > 100) {
			si.exAttribute2 = "大量文件";
		}

		def realName = RealNameTable.get(ci.author);
		if (null!=realName) {
			si.author = realName;
			si.exAttribute3 = ci.author;	//备份被规范化修改的作者名称
		}
	}

	def resolverTask(str){
		def MASK = "#";
		def maxIdx = str.length()-1;
		def start = str.indexOf(MASK);
		if (start >= 0){
			def end = -1;
			for (def i=0; i<10; i++){
				def pointer = start+MASK.length()+i;
				if (pointer > maxIdx){
					break;
				}
				def chr = str[pointer];
				if (chr >= "0" && chr <= "9"){
					end = pointer;
				}else{
					break;
				}
			}
			if (end > start){
				return str[start..end]
			}
		}
		return "";
	}

	/**
	 * 月份与季度的对应关系
	 */
	static def QuarterTable = [
		"01": "Q1", "02": "Q1", "03": "Q1",
		"04": "Q2", "05": "Q2", "06": "Q2",
		"07": "Q3", "08": "Q3", "09": "Q3",
		"10": "Q4", "11": "Q4", "12": "Q4",
	];
	
	/**
	 * 作者的真名对应关系
	 */
	static def RealNameTable = [
		"勾三股四": "GU Yiling",
		"vue-bot": "-"
	]
}
