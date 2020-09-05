package net.thinkbase.dev.gittools.service.utils;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.Edit;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.patch.FileHeader;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.EmptyTreeIterator;
import org.eclipse.jgit.util.io.DisabledOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.thinkbase.dev.gittools.service.vo.CommitStatInfo;

public class GitUtils {
	private static final Logger logger =  LoggerFactory.getLogger(GitUtils.class);
	
	/**
	 * Find the git repos from some paths;
	 * If path ends with "/", treat it as the parent path of several git repos;
	 * If path starts with "--", skip it.
	 * @param repoPaths
	 * @return
	 */
	public static List<File> findAllGitRepos(String[] repoPaths){
		List<File> result = new ArrayList<>();
		
		for (String path: repoPaths) {
			path = path.trim();
			if (path.startsWith("--")) {
				continue;
			}
			if (path.endsWith("/")) {
				File parent = new File(path);
				String[] subDirs = parent.list(new FilenameFilter() {
					@Override
					public boolean accept(File dir, String name) {
						return dir.isDirectory();
					}
				});
				for(String subDir: subDirs) {
					File repo = new File(parent, subDir+"/.git");
					if (repo.exists()) {
						result.add(repo);
					}
				}
			} else {
				File repo = new File(path, ".git");
				if (repo.exists()) {
					result.add(repo);
				}
			}
		}
		
		return result;
	}
	
	/**
	 * Analyse a repo
	 * @param repo
	 * @param cb
	 * @param summaryDiffEntries
	 * @return
	 * @throws IOException
	 * @throws GitAPIException
	 */
	public static void analyseRepoCommits(Repository repo, AnalyseCallback cb, boolean summaryDiffEntries) throws IOException, GitAPIException {
		try (Git git = new Git(repo)) {
			try (RevWalk walk = new RevWalk(repo)){
				try (DiffFormatter diffFormatter = new DiffFormatter(DisabledOutputStream.INSTANCE)){
					try (ObjectReader reader = repo.newObjectReader()) {
						diffFormatter.setRepository(repo);
						diffFormatter.setDiffComparator(RawTextComparator.DEFAULT);
						diffFormatter.setDetectRenames(true);

						doAnalyse(repo, git, walk, reader, diffFormatter, cb, summaryDiffEntries);
					}
				}
			}
		}
    }

	private static void doAnalyse(
			Repository repo, Git git, RevWalk walk,
			ObjectReader reader, DiffFormatter diffFormatter,
			AnalyseCallback cb, boolean summaryDiffEntries) throws GitAPIException, IOException {
		
		String repoPath = git.getRepository().getDirectory().getCanonicalPath();
		
		Iterable<RevCommit> logs = git.log().call();	//logs of current branches
		for(RevCommit commit: logs) {
			if (logger.isDebugEnabled()) {
				logger.debug("Processing : {} of {} ...", commit.getId().getName(), repoPath);
			}
		    
		    AbstractTreeIterator oldTreeIter;
		    if (commit.getParentCount()>0) {
			    RevCommit parent = walk.parseCommit(commit.getParent(0).getId());
		    	oldTreeIter = new CanonicalTreeParser(null, reader, parent.getTree());
		    }else {
		    	oldTreeIter = new EmptyTreeIterator();
		    }
		    
		    AbstractTreeIterator newTreeIter = new CanonicalTreeParser(null, reader, commit.getTree());
		    
		    List<DiffEntry> diffs = diffFormatter.scan(oldTreeIter, newTreeIter);
		    
		    CommitStatInfo ci = null;
		    if (summaryDiffEntries) {
			    ci = buildBasicCommitInfo(repo, commit);
		    }
		    
			int linesAdded = 0;
			int linesDeleted = 0;
			int files = 0;

		    for (DiffEntry diff : diffs) {
		    	if (! summaryDiffEntries) {
		    		ci = buildBasicCommitInfo(repo, commit);
					linesAdded = 0;
					linesDeleted = 0;
					files = 0;
		    	}
		    	
		    	boolean canCompare = false;	//i.e. If PatchType=BINARY, not editlist so can't campare text
		    	
		    	int curDiffDel = 0;
		    	int curDiffAdd = 0;
		    	FileHeader fileHeader = diffFormatter.toFileHeader(diff);
				for (Edit edit : fileHeader.toEditList()) {
					canCompare = true;
					
		            curDiffDel += edit.getEndA() - edit.getBeginA();
		            curDiffAdd += edit.getEndB() - edit.getBeginB();
		            
		        }
				if (canCompare) {
		            ci.addDiff(diff, curDiffAdd, curDiffDel);
				}else {
					ci.addDiff(diff);
				}
	            linesDeleted += curDiffDel;
				linesAdded += curDiffAdd;
				files ++;
				
				if (! summaryDiffEntries) {
				    ci.setFiles(files);
				    ci.setLinesAdded(linesAdded);
				    ci.setLinesDeleted(linesDeleted);
				    cb.perform(ci);

				}
		    }
		    if (summaryDiffEntries) {
			    ci.setFiles(files);
			    ci.setLinesAdded(linesAdded);
			    ci.setLinesDeleted(linesDeleted);
			    cb.perform(ci);
		    }
		}
	}

	private static CommitStatInfo buildBasicCommitInfo(Repository repo, RevCommit commit) throws IOException {
		CommitStatInfo ci;
		ci = new CommitStatInfo();
		ci.setRepo(repo.getWorkTree().getName());
		ci.setBranch(repo.getBranch());
		
		ci.setId(commit.getId().getName());
		ci.setTime(new Date(commit.getCommitTime()*1000L));
		ci.setAuthor(commit.getAuthorIdent().getName());
		ci.setCommitter(commit.getCommitterIdent().getName());
		ci.setComment(commit.getFullMessage());
		
		RevCommit[] parents = commit.getParents();
		for(RevCommit p: parents) {
			ci.addParent(p.getId().getName());
		}
		
		return ci;
	}
	
	public static interface AnalyseCallback {
		public void perform(CommitStatInfo ci);
	}
}
