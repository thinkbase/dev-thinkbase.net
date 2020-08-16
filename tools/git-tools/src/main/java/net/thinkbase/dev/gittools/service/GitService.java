package net.thinkbase.dev.gittools.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Workbook;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.liaochong.myexcel.core.DefaultStreamExcelBuilder;
import com.github.liaochong.myexcel.utils.FileExportUtil;

import net.thinkbase.dev.gittools.config.GittoolsConfig;
import net.thinkbase.dev.gittools.service.utils.GitUtils;
import net.thinkbase.dev.gittools.service.vo.ExportResult;
import net.thinkbase.dev.gittools.xls.vo.StatDetailBean;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/service")
public class GitService {
	private static final Logger logger =  LoggerFactory.getLogger(GitService.class);
	
	@Autowired
	@Qualifier(GittoolsConfig.EXPORT_WORK_DIR)
	private File exportWorkDir;

	@PostMapping("/export-stat-detail")
	public Mono<ExportResult> doExportStatDetail(@RequestBody String pathLines) throws IOException, GitAPIException, GitAPIException {
		String[] paths = pathLines.split("[\\n|\\r]");
		List<File> repos = GitUtils.findAllGitRepos(paths);

		final int idxRepoCount=0, idxCommitCount=1;
		final var startTime=System.currentTimeMillis();
		var buf = new long[] {0, 0};
		
	    DefaultStreamExcelBuilder<StatDetailBean> streamBuilder =
	    		DefaultStreamExcelBuilder.of(StatDetailBean.class).fixedTitles().start();
		
		for (var dir: repos) {
			FileRepositoryBuilder builder = new FileRepositoryBuilder();
			try (var repo = builder.setGitDir(dir).build()) {
				logger.info("Begin process repo {} ...", repo.getDirectory());
				buf[idxRepoCount]++;
				GitUtils.analyseRepoCommits(repo, (ci)->{
					streamBuilder.append(StatDetailBean.fromCommitStatInfo(ci));
					buf[idxCommitCount]++;
				});
	        }
		}
		
	    Workbook workbook = streamBuilder.build();
	    String fileName=StatDetailBean.SHEET_NAME+"."+System.currentTimeMillis()+".xlsx";
	    FileExportUtil.export(workbook, new File(exportWorkDir, fileName));
		
		String msg = "导出操作完成: 共处理 "+buf[idxRepoCount]+ "个仓库, "+buf[idxCommitCount]+" 个提交,"
				+ " 耗时"+(System.currentTimeMillis()-startTime)/1000+"秒";
		
		var data = new ExportResult(msg);
		data.setDownloadAddress(fileName);
		return Mono.just(data);
	}
}
