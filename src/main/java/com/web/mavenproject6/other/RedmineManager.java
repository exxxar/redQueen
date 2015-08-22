/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.web.mavenproject6.other;

import com.taskadapter.redmineapi.IssueManager;
import com.taskadapter.redmineapi.RedmineException;
import com.taskadapter.redmineapi.RedmineManagerFactory;
import com.taskadapter.redmineapi.bean.CustomField;
import com.taskadapter.redmineapi.bean.Issue;
import com.taskadapter.redmineapi.bean.IssueCategory;
import com.taskadapter.redmineapi.bean.IssueFactory;
import com.taskadapter.redmineapi.bean.Project;
import com.taskadapter.redmineapi.bean.Tracker;
import com.taskadapter.redmineapi.bean.User;
import java.util.Date;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 *
 * @author Aleks
 */
//@Component
public class RedmineManager {

    @Autowired
    private Environment env;

    private com.taskadapter.redmineapi.RedmineManager mgr;
    private IssueManager issueManager;

    public RedmineManager() {
        this.mgr = RedmineManagerFactory.createWithApiKey(
                env.getProperty("redmine.uri"),
                env.getProperty("redmine.api.accessKey")
        );
        this.issueManager = mgr.getIssueManager();
    }

    public List<Issue> getIssues(String projectKey) throws RedmineException {
        return issueManager.getIssues(projectKey, null);
    }

    public Issue newIssue(int projectId, String subject, JSONObject j, Object... o) throws RedmineException, JSONException {
        Issue is = IssueFactory.create(projectId, subject);
        is.setAuthor(mgr.getUserManager().getCurrentUser());
        is.setCreatedOn(new Date());
        if (o != null) {
            for (Object obj : o) {
                if (obj instanceof User) {
                    is.setAssignee((User) obj);
                }
                if (obj instanceof IssueCategory) {
                    is.setCategory((IssueCategory) obj);
                }
                if (obj instanceof Project) {
                    is.setProject((Project) obj);
                }
                if (obj instanceof Tracker) {
                    is.setTracker((Tracker) obj);
                }
            }
        }

        if (j != null) {
            is.setDescription(j.isNull("description") ? "" : j.getString("description"));
            is.setDoneRatio(j.isNull("doneRatio") ? 0 : Integer.parseInt(j.getString("doneRatio")));
            is.setStatusName(j.isNull("statusName") ? "" : j.getString("statusName"));
        }

        return issueManager.createIssue(is);
    }

    public Issue getIssue(Integer id) throws RedmineException {
        return issueManager.getIssueById(id);
    }
    
    public void removerIssue(Integer id) throws RedmineException{
        issueManager.deleteIssue(id);
    }
    
    public com.taskadapter.redmineapi.RedmineManager getManager(){
        return mgr;
    }
    
   
    
}
