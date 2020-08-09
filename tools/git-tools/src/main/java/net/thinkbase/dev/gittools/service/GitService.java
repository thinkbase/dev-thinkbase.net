package net.thinkbase.dev.gittools.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
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

		for (File dir: repos) {
			FileRepositoryBuilder builder = new FileRepositoryBuilder();
			try (Repository repo = builder.setGitDir(dir).build()) {
				logger.info("Begin process repo {} ...", repo.getDirectory());
				GitUtils.analyseRepoCommits(repo, (ci)->{
					logger.info(ci.toString());
				});
	        }
		}
		
		ExportResult data = new ExportResult();
		
		data.setSuccess(true);
		
		return Mono.just(data);
	}
}
