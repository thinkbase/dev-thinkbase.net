package net.thinkbase.dev.gittools.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.thinkbase.dev.gittools.service.utils.GitUtils;
import net.thinkbase.dev.gittools.service.vo.ExportResult;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/service")
public class GitService {
	private static final Logger logger =  LoggerFactory.getLogger(GitService.class);

	@PostMapping("/export")
	public Mono<ExportResult> doExport(@RequestBody String pathLines) throws IOException, GitAPIException, GitAPIException {
		String[] paths = pathLines.split("[\\n|\\r]");
		List<File> repos = GitUtils.findAllGitRepos(paths);

		var idxRepoCount=0;
		var idxCommitCount=1;
		var idxStartTime=2;
		var buf = new long[] {0, 0, System.currentTimeMillis()};
		for (var dir: repos) {
			FileRepositoryBuilder builder = new FileRepositoryBuilder();
			try (var repo = builder.setGitDir(dir).build()) {
				logger.info("Begin process repo {} ...", repo.getDirectory());
				buf[idxRepoCount]++;
				GitUtils.analyseRepoCommits(repo, (ci)->{
					logger.info(ci.toString());
					buf[idxCommitCount]++;
				});
	        }
		}
		String msg = "导出操作完成: 共处理 "+buf[idxRepoCount]+ "个仓库, "+buf[idxCommitCount]+" 个提交,"
				+ " 耗时"+(System.currentTimeMillis()-buf[idxStartTime])/1000+"秒";
		
		var data = new ExportResult(msg);
		return Mono.just(data);
	}
}
