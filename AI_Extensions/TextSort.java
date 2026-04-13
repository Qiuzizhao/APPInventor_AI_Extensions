package AI_Extensions;

import com.google.appinventor.components.annotations.*;
import com.google.appinventor.components.runtime.*;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.util.AsyncCallbackPair;
import com.google.appinventor.components.runtime.util.YailList;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

@DesignerComponent(version = 1, description = "TextSort", category = ComponentCategory.EXTENSION, nonVisible = true, iconName = "images/extension.png")
@UsesPermissions(permissionNames = "android.permission.INTERNET")
@UsesLibraries(libraries = "json.jar")
@SimpleObject(external = true)

public class TextSort extends AIXBase {
    private static final String PATH = "/text_sort";
    
    // 存储排序结果
    private JSONArray results;

    public TextSort(ComponentContainer container) {
        super(container);
    }

    // 服务器地址
    @SimpleProperty(category = PropertyCategory.BEHAVIOR)
    public String Server() {
        return SERVER;
    }

    @DesignerProperty(editorType = "string", defaultValue = "http://192.168.1.4:5000/aix")
    @SimpleProperty
    public void Server(String url) {
        SERVER = url;
    }

    // 获取文本排序
    @SimpleFunction(description = "GetSort")
    public void GetSort(String query, String documents) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("operation", "text_sort");
            params.put("query", query);
            params.put("documents", documents);
            
            
            post(PATH, params, new AsyncCallbackPair<JSONObject>() {
                public void onSuccess(JSONObject response) {
                    TextSort.this.results = response.getJSONArray("data");
                    TextSort.this.GotResult();
                }

                public void onFailure(String message) {
                    TextSort.this.raiseError("TextSort.GetSort", -1, message);
                }
            });
        } catch (Exception e) {
            raiseError("TextSort.GetSort", -1, e.toString());
        }
    }

    // 返回排序结果列表
    @SimpleFunction(description = "Get Sort List")
    public Object GetSortList() {
        // 将JSONArray转换为List<String>
        List<String> sortList = new ArrayList<>();
        for (int i = 0; i < this.results.length(); i++) {
            sortList.add(this.results.getString(i));
        }
        return YailList.makeList(sortList);
    }

    // 返回结果时触发
    @SimpleEvent(description = "Got Result")
    public void GotResult() {
        EventDispatcher.dispatchEvent(getInstance(), "GotResult");
    }
}