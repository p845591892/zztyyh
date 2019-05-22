/**
 * Copyright 2018 lenos
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gnz48.zzt.entity.activiti;

import java.util.Date;
import org.activiti.engine.repository.Model;

/**
 * @author zhuxiaomeng
 * @date 2018/1/18.
 * @email 154040976@qq.com 模型列表
 */
public class ActModel {

	private String id;
	private String name;
	private String key;
	private String category;
	private Date createTime;
	private Date lastUpdateTime;
	private Integer version;
	private String metaInfo;
	private String deploymentId;
	private String tenantId;
	private boolean hasEditorSource;

	public ActModel() {
	}

	public ActModel(Model model) {
		this.id = model.getId();
		this.name = model.getName();
		this.key = model.getKey();
		this.category = model.getCategory();
		this.createTime = model.getCreateTime();
		this.lastUpdateTime = model.getLastUpdateTime();
		this.version = model.getVersion();
		this.metaInfo = model.getMetaInfo();
		this.deploymentId = model.getDeploymentId();
		this.tenantId = model.getTenantId();
		this.hasEditorSource = model.hasEditorSource();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getMetaInfo() {
		return metaInfo;
	}

	public void setMetaInfo(String metaInfo) {
		this.metaInfo = metaInfo;
	}

	public String getDeploymentId() {
		return deploymentId;
	}

	public void setDeploymentId(String deploymentId) {
		this.deploymentId = deploymentId;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public boolean isHasEditorSource() {
		return hasEditorSource;
	}

	public void setHasEditorSource(boolean hasEditorSource) {
		this.hasEditorSource = hasEditorSource;
	}

}
