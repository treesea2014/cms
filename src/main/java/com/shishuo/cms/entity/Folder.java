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
package com.shishuo.cms.entity;

import java.util.Date;

import com.shishuo.cms.constant.FolderConstant;
import com.shishuo.cms.constant.SystemConstant;

/**
 * 目录实体
 * 
 * @author zsy
 * 
 */
public class Folder {
	/**
	 * 目录Id
	 */
	private long folderId;

	/**
	 * 父亲Id
	 */
	private long fatherId;

	/**
	 * 英文名称
	 */
	private String ename;
	/**
	 * 目录名称
	 */
	private String name;

	/**
	 * 路径
	 */
	private String path;
	/**
	 * 层级
	 */
	private int level;

	/**
	 * 排序
	 */
	private int sort;

	/**
	 * 文件数
	 */
	private int count;

	/**
	 * 等级
	 */
	private FolderConstant.Rank rank;

	/**
	 * 类型
	 */
	private SystemConstant.Type type;
	/**
	 * 状态
	 */
	private FolderConstant.Status status;

	/**
	 * 时间
	 */
	private Date createTime;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public long getFolderId() {
		return folderId;
	}

	public void setFolderId(long folderId) {
		this.folderId = folderId;
	}

	public long getFatherId() {
		return fatherId;
	}

	public void setFatherId(long fatherId) {
		this.fatherId = fatherId;
	}

	public String getName() {
		return name;
	}

	public String getEname() {
		return ename;
	}

	public void setEname(String ename) {
		this.ename = ename;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}


	public FolderConstant.Rank getRank() {
		return rank;
	}

	public void setRank(FolderConstant.Rank rank) {
		this.rank = rank;
	}

	public void setType(SystemConstant.Type type) {
		this.type = type;
	}

	public void setStatus(FolderConstant.Status status) {
		this.status = status;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public SystemConstant.Type getType() {
		return type;
	}

	public FolderConstant.Status getStatus() {
		return status;
	}

	
}
