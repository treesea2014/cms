/*
 * 
 *	Copyright © 2013 Changsha Shishuo Network Technology Co., Ltd. All rights reserved.
 *	长沙市师说网络科技有限公司 版权所有
 *	http://www.shishuo.com
 *
 *	Licensed under the Apache License, Version 2.0 (the "License");
 *	you may not use this file except in compliance with the License.
 *	You may obtain a copy of the License at
 *	 
 *		http://www.apache.org/licenses/LICENSE-2.0
 *
 *	Unless required by applicable law or agreed to in writing, software
 *	distributed under the License is distributed on an "AS IS" BASIS,
 *	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *	See the License for the specific language governing permissions and
 *	limitations under the License.
 */
package com.shishuo.cms.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.shishuo.cms.constant.ArticleConstant;
import com.shishuo.cms.constant.AttachmentConstant;
import com.shishuo.cms.constant.CommentConstant;
import com.shishuo.cms.constant.SystemConstant;
import com.shishuo.cms.dao.ArticleDao;
import com.shishuo.cms.dao.FolderDao;
import com.shishuo.cms.entity.Admin;
import com.shishuo.cms.entity.Article;
import com.shishuo.cms.entity.Folder;
import com.shishuo.cms.entity.vo.ArticleVo;
import com.shishuo.cms.entity.vo.FolderVo;
import com.shishuo.cms.entity.vo.PageVo;
import com.shishuo.cms.exception.ArticleNotFoundException;

/**
 * 
 * 文章服务
 * 
 * @author Zhangjiale
 * 
 */
@Service
public class ArticleService {

	@Autowired
	private ArticleDao articleDao;

	@Autowired
	private AdminService adminService;

	@Autowired
	private FolderService folderService;

	@Autowired
	private FolderDao folderDao;

	@Autowired
	private CommentService commentService;

	@Autowired
	private AttachmentService attachmentService;

	// ///////////////////////////////
	// ///// 增加 ////////
	// ///////////////////////////////

	/**
	 * 增加文件
	 * 
	 * @param folderId
	 * @param adminId
	 * @param picture
	 *            {@link:FileConstant.PICTURE}
	 * @param name
	 * @param content
	 * @param type
	 * @param status
	 * @param createTime
	 * @return
	 */
	@CacheEvict(value = "article", allEntries = true)
	public Article addArticle(long folderId) {
		FolderVo folder = folderService.getFolderById(folderId);
		Article article = new Article();
		Date now = new Date();
		article.setFirstFolderId(folder.getFirstFolderId());
		article.setSecondFolderId(folder.getSecondFolderId());
		article.setThirdFolderId(folder.getThirdFolderId());
		article.setFourthFolderId(folder.getFourthFolderId());
		article.setCreateTime(now);
		article.setUpdateTime(now);
		article.setExpireTime(now);
		articleDao.addArticle(article);
		return articleDao.getArticleById(article.getArticleId());
	}

	// ///////////////////////////////
	// ///// 刪除 ////////
	// ///////////////////////////////

	/**
	 * 删除文件
	 * 
	 * @param fileId
	 * @return boolean
	 */
	@CacheEvict(value = "article", allEntries = true)
	public boolean deleteFileByArticleId(long articleId) {
		return articleDao.deleteArticle(articleId);
	}

	// ///////////////////////////////
	// ///// 修改 ////////
	// ///////////////////////////////

	/**
	 * 修改文件
	 * 
	 * @param fileId
	 * @param folderId
	 * @param adminId
	 * @param picture
	 * @param name
	 * @param content
	 * @param type
	 * @param status
	 * @return
	 */
	@CacheEvict(value = "article", allEntries = true)
	public Article updateFileByFileId(long fileId, long folderId,
			long fatherId, long adminId, String name, String content,
			String title, long size, String description,
			ArticleConstant.Status status) {
		Article file = articleDao.getArticleById(fileId);
		FolderVo folder = folderDao.getFolderById(folderId);
		file.setFirstFolderId(folder.getFirstFolderId());
		file.setSecondFolderId(folder.getSecondFolderId());
		file.setThirdFolderId(folder.getThirdFolderId());
		file.setFourthFolderId(folder.getFourthFolderId());
		file.setAdminId(adminId);
		file.setName(name);
		file.setContent(content);
		file.setTitle(title);
		file.setDescription(description);
		file.setViewCount(0);
		file.setCommentCount(0);
		file.setStatus(status);
		file.setUpdateTime(new Date());
		articleDao.updateArticle(file);
		return file;
	}

	/**
	 * 修改文件的状态
	 * 
	 * @param fileId
	 * @param status
	 * @return boolean
	 * 
	 */
	public void updateStatusByFileId(long articleId,
			ArticleConstant.Status status) {
		articleDao.updateStatusByArticleId(articleId, status);
	}

	/**
	 * 更新浏览人数
	 * 
	 * @param fileId
	 * @param viewCount
	 * 
	 */
	public void updateViewCount(long articleId, int nowViewCount) {
		articleDao.updateViewCount(articleId, nowViewCount + 1);
	}

	/**
	 * 更新评论数
	 * 
	 * @param fileId
	 */
	public void updateCommentCount(long articleId) {
		int commentCount = commentService
				.getCommentCountByFatherId(articleId,
						CommentConstant.kind.article, 0,
						CommentConstant.Status.display);
		articleDao.updateCommentCount(articleId, commentCount);
	}

	// ///////////////////////////////
	// ///// 查詢 ////////
	// ///////////////////////////////

	/**
	 * 得到文件
	 * 
	 * @param fileId
	 * @return File
	 * @throws ArticleNotFoundException
	 */
	@Cacheable(value = "article", key = "'getArticleByArticleId_'+#articleId")
	public ArticleVo getArticleByArticleId(long articleId)
			throws ArticleNotFoundException {

		ArticleVo articleVo = articleDao.getArticleById(articleId);
		if (articleVo == null) {
			throw new ArticleNotFoundException(articleId + " 文件，不存在");
		} else {
			Admin admin = adminService.getAdminById(articleVo.getAdminId());

			articleVo
					.setFolder(folderDao.getFolderById(articleVo.getFolderId()));
			articleVo.setAdmin(admin);
			articleVo.setAttachmentList(attachmentService
					.getAttachmentListByKindId(articleId,
							AttachmentConstant.Kind.article,
							AttachmentConstant.Status.display));
			return articleVo;
		}
	}

	/**
	 * 得到目录的显示的文件分页
	 * 
	 * @param folderId
	 * @return pageVo
	 */
	@Cacheable(value = "article", key = "'getArticlePageByFolderId_'+#folderId+'_'+#pageNum")
	public PageVo<ArticleVo> getArticlePageByFolderId(long folderId,
			int pageNum, int rows) {
		PageVo<ArticleVo> pageVo = new PageVo<ArticleVo>(pageNum);
		FolderVo folder = folderService.getFolderById(folderId);
		pageVo.setRows(rows);
		pageVo.setCount(this.getArticleCountByFoderIdPath(
				folder.getFirstFolderId(), folder.getSecondFolderId(),
				folder.getThirdFolderId(), folder.getFourthFolderId(),
				ArticleConstant.Status.display));
		List<ArticleVo> articlelist = this.getArticleListByFoderIdPath(
				folder.getFirstFolderId(), folder.getSecondFolderId(),
				folder.getThirdFolderId(), folder.getFourthFolderId(),
				ArticleConstant.Status.display, pageVo.getOffset(),
				pageVo.getRows());
		for (ArticleVo article : articlelist) {
			article.setAttachmentList(attachmentService
					.getAttachmentPageByKindId(article.getArticleId(),
							AttachmentConstant.Kind.article, 1000, 1).getList());
		}
		pageVo.setList(articlelist);
		return pageVo;
	}

	/**
	 * 得到目录下的文件
	 * 
	 * @param foderId
	 * @return
	 */
	public List<ArticleVo> getArticleListByFoderIdPath(long firstFolderId,
			long secondFolderId, long thirdFolderId, long fourthFolderId,
			ArticleConstant.Status status, long offset, long rows) {
		List<ArticleVo> articlelist = articleDao.getArticleListByFoderIdPath(
				firstFolderId, secondFolderId, thirdFolderId, fourthFolderId,
				status, offset, rows);
		for (ArticleVo article : articlelist) {
			article.setAttachmentList(attachmentService
					.getAttachmentPageByKindId(article.getArticleId(),
							AttachmentConstant.Kind.article, 1000, 1).getList());
			article.setAdmin(adminService.getAdminById(article.getAdminId()));
			article.setFolder(folderService.getFolderById(article.getFolderId()));
			article.setFolderPathList(folderService
					.getFolderPathListByFolderId(article.getFolderId()));
		}
		return articlelist;
	}

	/**
	 * 得到目录的某种文件的数量
	 * 
	 * @param folderId
	 * @param type
	 * @param status
	 * @return Integer
	 */
	public int getArticleCountByFoderIdPath(long firstFolderId,
			long secondFolderId, long thirdFolderId, long fourthFolderId,
			ArticleConstant.Status status) {
		return articleDao.getArticleCountByFoderIdPath(firstFolderId,
				secondFolderId, thirdFolderId, fourthFolderId, status);
	}

	/**
	 * @param firstFolderId
	 * @param secondFolderId
	 * @param thirdFolderId
	 * @param fourthFolderId
	 * @return
	 */
	public int getArticleCountByFoderId(long firstFolderId,
			long secondFolderId, long thirdFolderId, long fourthFolderId) {
		return articleDao.getArticleCountByFoderId(firstFolderId,
				secondFolderId, thirdFolderId, fourthFolderId);
	}

	/**
	 * 获取某种文件的分页
	 * 
	 * @param type
	 * @param status
	 * @param pageNum
	 * @return PageVo<File>
	 * 
	 */
	public PageVo<ArticleVo> getArticlePageByTypeAndStatusPage(
			ArticleConstant.Status status, int pageNum) {
		PageVo<ArticleVo> pageVo = new PageVo<ArticleVo>(pageNum);
		pageVo.setRows(20);
		pageVo.getArgs().put("status", status.name());
		List<ArticleVo> list = this.getArticleListByStatus(status,
				pageVo.getOffset(), pageVo.getRows());
		for (ArticleVo articleVo : list) {
			articleVo
					.setFolder(folderDao.getFolderById(articleVo.getFolderId()));
			if (articleVo.getFirstFolderId() != 0) {
				articleVo.getFolderPathList().add(
						folderDao.getFolderById(articleVo.getFirstFolderId()));
			}
			if (articleVo.getSecondFolderId() != 0) {
				articleVo.getFolderPathList().add(
						folderDao.getFolderById(articleVo.getSecondFolderId()));
			}
			if (articleVo.getThirdFolderId() != 0) {
				articleVo.getFolderPathList().add(
						folderDao.getFolderById(articleVo.getThirdFolderId()));
			}
			if (articleVo.getFourthFolderId() != 0) {
				articleVo.getFolderPathList().add(
						folderDao.getFolderById(articleVo.getFourthFolderId()));
			}
		}
		pageVo.setList(list);
		pageVo.setCount(this.getArticleCountByStatus(status));

		return pageVo;
	}

	/**
	 * 获取不同类型的文件的列表
	 * 
	 * @param type
	 * @param status
	 * @param offset
	 * @param rows
	 * @return List<File>
	 * 
	 */
	public List<ArticleVo> getArticleListByStatus(
			ArticleConstant.Status status, long offset, long rows) {
		return articleDao.getArticleListByStatus(status, offset, rows);
	}

	/**
	 * 获取不同类型的文件的数量
	 * 
	 * @param type
	 * @param status
	 * @param Integer
	 * 
	 */
	public int getArticleCountByStatus(ArticleConstant.Status status) {
		return articleDao.getArticleCountByStatus(status);
	}

}