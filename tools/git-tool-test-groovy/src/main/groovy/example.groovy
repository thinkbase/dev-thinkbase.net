import net.thinkbase.dev.gittools.service.vo.CommitStatInfo
import net.thinkbase.dev.gittools.xls.vo.StatDetailBean

class GitToolsEx {
	def run(CommitStatInfo ci, StatDetailBean si) {
		si.exCatalog1 = "测试";
	}
}
