package AI_Extensions;

import com.google.appinventor.components.annotations.*;
import com.google.appinventor.components.runtime.*;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.util.AsyncCallbackPair;
import com.google.appinventor.components.runtime.util.FileUtil;
import com.google.appinventor.components.runtime.util.YailList;



import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@DesignerComponent(version = 1, description = "MindMap", category = ComponentCategory.EXTENSION, nonVisible = true, iconName = "images/extension.png")
@UsesPermissions(permissionNames = "android.permission.INTERNET")
@UsesLibraries(libraries = "json.jar")
@SimpleObject(external = true)

public class MindMap extends AIXBase {
    private static final String PATH = "/mindmap";

    // 返回的图片base64
    private String resultBase64 = "";

    public MindMap(ComponentContainer container) {
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

    //生成思维导图
    @SimpleFunction(description = "DrawMindMap")
    public void DrawMindMap(String text) {
        try {
            //键值对
            Map<String, Object> params = new HashMap<>();

            //操作类型为mindmap
            params.put("operation", "mindmap");
            
            //文本
            params.put("text", text);


            post(PATH, params, new AsyncCallbackPair<JSONObject>() {
                public void onSuccess(JSONObject response) {
                    MindMap.this.resultBase64 = response.getString("data");
                    MindMap.this.GotResult();
                }

                public void onFailure(String message) {
                    MindMap.this.raiseError("MindMap.DrawMindMap", -1, message);
                }
            });
        } catch (Exception e) {
            raiseError("MindMap.DrawMindMap", -1, e.toString());
        }
    }


    //返回图片base64
    @SimpleFunction(description = "GetResultImageBase64")
    public String GetResultImageBase64() {
        return this.resultBase64;
    }


    //返回结果时触发
    @SimpleEvent(description = "GotResult")
    public void GotResult() {
        EventDispatcher.dispatchEvent(getInstance(), "GotResult");
    }

}