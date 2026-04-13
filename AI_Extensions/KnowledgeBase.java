package AI_Extensions;

import com.google.appinventor.components.annotations.*;
import com.google.appinventor.components.runtime.*;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.util.AsyncCallbackPair;
import com.google.appinventor.components.runtime.util.FileUtil;



import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

@DesignerComponent(version = 1, description = "KnowledgeBase", category = ComponentCategory.EXTENSION, nonVisible = true, iconName = "images/extension.png")
@UsesPermissions(permissionNames = "android.permission.INTERNET, android.permission.READ_EXTERNAL_STORAGE")
@UsesLibraries(libraries = "json.jar")
@SimpleObject(external = true)

public class KnowledgeBase extends AIXBase {
    private static final String PATH = "/knowledge_base";

    public KnowledgeBase(ComponentContainer container) {
        super(container);
    }

    //服务器地址
    @SimpleProperty(category = PropertyCategory.BEHAVIOR)
    public String Server() {
        return SERVER;
    }

    @DesignerProperty(editorType = "string", defaultValue = "http://192.168.1.4:5000/aix")
    @SimpleProperty
    public void Server(String url) {
        SERVER = url;
    }

    //构建知识库
    @SimpleFunction(description = "Build KnowledgeBase")
    public void Build() {
        try {
            //键值对
            Map<String, Object> params = new HashMap<>();
            //操作类型为build构建知识库
            params.put("operation", "build");
            post(PATH, params, new AsyncCallbackPair<JSONObject>() {
                public void onSuccess(JSONObject response) {
                    KnowledgeBase.this.BuildSuccess();
                }

                public void onFailure(String message) {
                    KnowledgeBase.this.raiseError("KnowledgeBase.Build", -1, message);
                }
            });
        } catch (Exception e) {
            raiseError("KnowledgeBase.Build", -1, e.toString());
        }
    }

    //重置知识库
    @SimpleFunction(description = "Reset KnowledgeBase")
    public void Reset() {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("operation", "reset");
            post(PATH, params, new AsyncCallbackPair<JSONObject>() {
                public void onSuccess(JSONObject response) {
                    KnowledgeBase.this.ResetSuccess();
                }

                public void onFailure(String message) {
                    KnowledgeBase.this.raiseError("KnowledgeBase.Reset", -1, message);
                }
            });
        } catch (Exception e) {
            raiseError("KnowledgeBase.Reset", -1, e.toString());
        }
    }

    //添加文本数据
    @SimpleFunction(description = "Add Text Data")
    public void AddText(String text) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("operation", "add_text");
            params.put("text", text);
            post(PATH, params, new AsyncCallbackPair<JSONObject>() {
                public void onSuccess(JSONObject response) {
                    KnowledgeBase.this.AddDataSuccess();
                }

                public void onFailure(String message) {
                    KnowledgeBase.this.raiseError("KnowledgeBase.AddTextData", -1, message);
                }
            });
        } catch (Exception e) {
            raiseError("KnowledgeBase.AddTextData", -1, e.toString());
        }
    }

    //添加网页数据
    @SimpleFunction(description = "Add Web Data")
    public void AddWeb(String url) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("operation", "add_web");
            params.put("url", url);
            post(PATH, params, new AsyncCallbackPair<JSONObject>() {
                public void onSuccess(JSONObject response) {
                    KnowledgeBase.this.AddDataSuccess();
                }

                public void onFailure(String message) {
                    KnowledgeBase.this.raiseError("KnowledgeBase.AddWebData", -1, message);
                }
            });
        } catch (Exception e) {
            raiseError("KnowledgeBase.AddWebData", -1, e.toString());
        }
    }


    //当知识库构建完成时触发
    @SimpleEvent(description = "Build Success")
    public void BuildSuccess() {
        EventDispatcher.dispatchEvent(getInstance(), "BuildSuccess");
    }

    //当知识库重置完成时触发
    @SimpleEvent(description = "Reset Success")
    public void ResetSuccess() {
        EventDispatcher.dispatchEvent(getInstance(), "ResetSuccess");
    }

    //当知识库添加数据完成时触发
    @SimpleEvent(description = "Add Data Success")
    public void AddDataSuccess() {
        EventDispatcher.dispatchEvent(getInstance(), "AddDataSuccess");
    }

}