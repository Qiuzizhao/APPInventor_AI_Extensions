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

@DesignerComponent(version = 1, description = "TextEmbedding", category = ComponentCategory.EXTENSION, nonVisible = true, iconName = "images/extension.png")
@UsesPermissions(permissionNames = "android.permission.INTERNET")
@UsesLibraries(libraries = "json.jar")
@SimpleObject(external = true)

public class TextEmbedding extends AIXBase {
    private static final String PATH = "/text_embedding";
    
    // 存储嵌入结果
    private JSONArray results;

    public TextEmbedding(ComponentContainer container) {
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

    // 获取文本嵌入
    @SimpleFunction(description = "GetEmbedding")
    public void GetEmbedding(String text) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("operation", "text_embedding");
            params.put("text", text);
            
            post(PATH, params, new AsyncCallbackPair<JSONObject>() {
                public void onSuccess(JSONObject response) {
                    TextEmbedding.this.results = response.getJSONArray("data");
                    TextEmbedding.this.GotResult();
                }

                public void onFailure(String message) {
                    TextEmbedding.this.raiseError("TextEmbedding.GetEmbedding", -1, message);
                }
            });
        } catch (Exception e) {
            raiseError("TextEmbedding.GetEmbedding", -1, e.toString());
        }
    }

    // 返回嵌入结果列表
    @SimpleFunction(description = "Get Embedding List")
    public Object GetEmbeddingList() {
        // 将JSONArray转换为List<Double>
        List<Double> embeddingList = new ArrayList<>();
        for (int i = 0; i < this.results.length(); i++) {
            embeddingList.add(this.results.getDouble(i));
        }
        return YailList.makeList(embeddingList);
    }

    // 返回结果时触发
    @SimpleEvent(description = "Got Result")
    public void GotResult() {
        EventDispatcher.dispatchEvent(getInstance(), "GotResult");
    }
} 