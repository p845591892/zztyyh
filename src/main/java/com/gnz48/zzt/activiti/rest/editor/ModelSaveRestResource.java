/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gnz48.zzt.activiti.rest.editor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Model;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 流程信息入库
 * @author Tijs Rademakers
 */
@RestController
@RequestMapping("/service")
public class ModelSaveRestResource implements ModelDataJsonConstants {
	
	private static final Logger log = LoggerFactory.getLogger(ModelSaveRestResource.class);

	@Autowired
	private RepositoryService repositoryService;

	@PostMapping(value = "/model/{modelId}/save")
	@ResponseStatus(value = HttpStatus.OK)
	public void saveModel(@PathVariable String modelId, String name, String description, String json_xml,
			String svg_xml, HttpServletRequest request, HttpServletResponse response) {
		log.info("流程信息入库: modelId = {}, name = {}, description = {}, json_xml = {}, svg_xml = {}",
				modelId, name, description, json_xml, svg_xml);
		
		try {
			Map<String, String[]> map = request.getParameterMap();
			JSONObject jsonObject = new JSONObject();
			// 全跑到key了，可取方案
			for (Map.Entry<String, String[]> entry : map.entrySet()) {
				String data = entry.getKey() + "=" + (entry.getValue()[0]);
				jsonObject = JSON.parseObject(data);
			}
			name = (String) jsonObject.get("name");
			description = (String) jsonObject.get("description");
			json_xml = (String) jsonObject.get("json_xml");
			svg_xml = (String) jsonObject.get("svg_xml");

			Model model = repositoryService.getModel(modelId);
			JSONObject object = new JSONObject();
			object.put(MODEL_NAME, name);
			object.put(MODEL_DESCRIPTION, description);
			model.setMetaInfo(object.toString());
			model.setName(name);
			repositoryService.saveModel(model);

			repositoryService.addModelEditorSource(model.getId(), json_xml.getBytes("utf-8"));

			InputStream svgStream = new ByteArrayInputStream(svg_xml.getBytes("utf-8"));
			TranscoderInput input = new TranscoderInput(svgStream);

			PNGTranscoder transcoder = new PNGTranscoder();
			// Setup output
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			TranscoderOutput output = new TranscoderOutput(outStream);

			// Do the transformation
			transcoder.transcode(input, output);
			final byte[] result = outStream.toByteArray();
			repositoryService.addModelEditorSourceExtra(model.getId(), result);
			outStream.close();

		} catch (Exception e) {
			log.error("Error saving model", e);
			throw new ActivitiException("Error saving model", e);
		}
	}
}
