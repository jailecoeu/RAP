package com.taobao.rigel.rap.mock.web.action;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.taobao.rigel.rap.common.ActionBase;
import com.taobao.rigel.rap.common.SystemVisitorLog;
import com.taobao.rigel.rap.mock.service.MockMgr;
import com.taobao.rigel.rap.project.bo.Action;
import com.taobao.rigel.rap.project.bo.Module;
import com.taobao.rigel.rap.project.bo.Page;
import com.taobao.rigel.rap.project.bo.Project;
import com.taobao.rigel.rap.project.service.ProjectMgr;
import org.apache.logging.log4j.LogManager;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;


public class MockAction extends ActionBase {

	private static final long serialVersionUID = 1L;
	private int id;
	private String pattern;
	private String mockData;
	private int actionId;
	private int projectId;
	private String content;
	private String callback;
	private boolean enable = true;
	private String _c;
	private ProjectMgr projectMgr;
	private List<String> urlList;
	private boolean seajs;
	private String mode;
    private static final org.apache.logging.log4j.Logger logger = LogManager.getFormatterLogger(MockAction.class.getName());
	public List<String> getUrlList() {
		return urlList;
	}
	private String getMethod() {
		return ServletActionContext.getRequest().getMethod();
	}

	public void setUrlList(List<String> urlList) {
		this.urlList = urlList;
	}

	public ProjectMgr getProjectMgr() {
		return projectMgr;
	}

	public int getProjectId() {
		return projectId;
	}

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public void setSeajs(boolean seajs) {
		this.seajs = seajs;
	}

	public boolean isSeajs() {
		return seajs;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public void setProjectMgr(ProjectMgr projectMgr) {
		this.projectMgr = projectMgr;
	}

	public void setMockData(String mockData) {
		this.mockData = mockData;
	}

	public void setActionId(int actionId) {
		this.actionId = actionId;
	}

	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}

	public String get_c() {
		return _c;
	}

	public void set_c(String _c) {
		this._c = _c;
	}

	public String getCallback() {
		return callback;
	}

	public void setCallback(String callback) {
		this.callback = callback;
	}

	private MockMgr mockMgr;

	public MockMgr getMockMgr() {
		return mockMgr;
	}

	public void setMockMgr(MockMgr mockMgr) {
		this.mockMgr = mockMgr;
	}

	public String getContent() {
		return content;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPattern() {
		return pattern;
	}

	/**
	 * force callback or _c to be the last parameter
	 * 
	 * @param pattern
	 */
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public String createData() throws UnsupportedEncodingException {
		boolean isJSON = false;
		SystemVisitorLog.mock(id, "createData", pattern, getCurAccount(), projectMgr);
		Map<String, Object> options = new HashMap<String, Object>();
		String _c = get_c();
		String result = mockMgr.generateData(id, pattern, options);
		if (options.get("callback") != null) {
			_c = (String) options.get("callback");
			callback = (String) options.get("callback");
		}

		if (callback != null && !callback.isEmpty()) {
			setContent(callback + "(" + result + ")");
		} else if (_c != null && !_c.isEmpty()) {
			setContent(_c + "(" + result + ")");
		} else {
			isJSON = true;
			setContent(result);
		}
		if (isJSON) {
			return "json";
		} else {
			return SUCCESS;
		}
	}

	public String createRule() throws UnsupportedEncodingException {
		boolean isJSON = false;
        SystemVisitorLog.mock(id, "createRule", pattern, getCurAccount(), projectMgr);
		Map<String, Object> options = new HashMap<String, Object>();
		String _c = get_c();
		options.put("method", getMethod());

		String result = mockMgr.generateRule(id, pattern, options);
		if (options.get("callback") != null) {
			_c = (String) options.get("callback");
			callback = (String) options.get("callback");
		}
		if (callback != null && !callback.isEmpty()) {
			setContent(callback + "(" + result + ")");
		} else if (_c != null && !_c.isEmpty()) {
			setContent(_c + "(" + result + ")");
		} else {
			isJSON = true;
			setContent(result);
		}
		if (isJSON) {
			return "json";
		} else {
			return SUCCESS;
		}
	}
	
	public String createRuleByActionData() throws UnsupportedEncodingException {
		boolean isJSON = false;
        SystemVisitorLog.mock(id, "createRuleByActionData", pattern, getCurAccount(), projectMgr);
		Map<String, Object> options = new HashMap<String, Object>();
		String _c = get_c();
		String result = mockMgr.generateRule(id, pattern, options);
		if (options.get("callback") != null) {
			_c = (String) options.get("callback");
			callback = (String) options.get("callback");
		}
		if (callback != null && !callback.isEmpty()) {
			setContent(callback + "(" + result + ")");
		} else if (_c != null && !_c.isEmpty()) {
			setContent(_c + "(" + result + ")");
		} else {
			isJSON = true;
			setContent(result);
		}
		if (isJSON) {
			return "json";
		} else {
			return SUCCESS;
		}
	}

	public String modify() {
		setNum(mockMgr.modify(actionId, mockData));
		return SUCCESS;
	}

	public String reset() {
		setNum(mockMgr.reset(projectId));
		return SUCCESS;
	}

	public String createPluginScript() {
        SystemVisitorLog.mock(id, "createPluginScript", pattern, getCurAccount(), projectMgr);
		Map<String, Boolean> _circleRefProtector = new HashMap<String, Boolean>();
		List<String> list = new ArrayList<String>();
		Project p = projectMgr.getProject(projectId);

		loadWhiteList(p, list, _circleRefProtector);
		urlList = list;
		return SUCCESS;
	}

	public String getWhiteList() {
		Map<String, Boolean> _circleRefProtector = new HashMap<String, Boolean>();
		List<String> list = new ArrayList<String>();
		Project p = projectMgr.getProject(projectId);

		loadWhiteList(p, list, _circleRefProtector);
		urlList = list;
		Gson g = new Gson();
		String json = g.toJson(urlList);
		setJson(json);

		return SUCCESS;
	}
	
	private void loadWhiteList(Project p, List<String> list, Map<String, Boolean> map) {
		// prevent circle reference
		if (p == null || map.get(p.getId() + "") != null) {
			return;
		} else {
			map.put(p.getId() + "", true);
		}
		if (p != null) {
			for (Module m : p.getModuleList()) {
				for (Page page : m.getPageList()) {
					for (Action a : page.getActionList()) {
						list.add(a.getRequestUrlRel());
					}
				}
			}
		}
		
		String relatedIds = p.getRelatedIds();
		if (relatedIds != null && !relatedIds.isEmpty()) {
			String[] relatedIdsArr = relatedIds.split(",");
			for (String relatedId : relatedIdsArr) {
				int rId = Integer.parseInt(relatedId);
				Project rP = projectMgr.getProject(rId);
				if (rP != null && rP.getId() > 0)
					loadWhiteList(rP, list, map);
			}
		}
	}

	public String createMockjsData() throws UnsupportedEncodingException {
		boolean isJSON = false;
        SystemVisitorLog.mock(id, "createMockjsData", pattern, getCurAccount(), projectMgr);
		String _c = get_c();
		Map<String, Object> options = new HashMap<String, Object>();
		options.put("method", getMethod());
		String result = mockMgr.generateRuleData(id, pattern, options);
		if (options.get("callback") != null) {
			_c = (String) options.get("callback");
			callback = (String) options.get("callback");
		}
		if (callback != null && !callback.isEmpty()) {
			setContent(callback + "(" + result + ")");
		} else if (_c != null && !_c.isEmpty()) {
			setContent(_c + "(" + result + ")");
		} else {
			isJSON = true;
			setContent(result);
		}
		
		if (isJSON) {
			return "json";
		} else {
			return SUCCESS;
		}
	}
}
