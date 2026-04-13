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

@DesignerComponent(version = 1, description = "WritingMaster", category = ComponentCategory.EXTENSION, nonVisible = true, iconName = "images/extension.png")
@UsesPermissions(permissionNames = "android.permission.INTERNET")
@UsesLibraries(libraries = "json.jar")
@SimpleObject(external = true)

public class WritingMaster extends AIXBase {
    private static final String PATH = "/writing_master";

    private static final List<String> MODELS = Arrays.asList("gemma2","llama3.1","qwen2.5","phi3","mistral","yi","glm4","deepseek-r1");

    private String model = "gemma2";

    //给出的回答
    private String result = "";

    public WritingMaster(ComponentContainer container) {
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

    //模型
    @SimpleProperty(category = PropertyCategory.BEHAVIOR)
    public String Model() {
        return this.model;
    }

    @DesignerProperty(editorType = "string", defaultValue = "gemma2")
    @SimpleProperty
    public void Model(String model) {
        this.model = model;
    }

    //获取支持的模型列表
    @SimpleFunction(description = "GetSupportedModelsList")
    public static Object GetSupportedModelsList() {
        return YailList.makeList(MODELS);
    }


    //写作大师
    @SimpleFunction(description = "Writing")
    public void Writing(String question) {
        try {
            //键值对
            Map<String, Object> params = new HashMap<>();

            //操作类型为writing_master写作大师
            params.put("operation", "writing_master");
            
            //模型
            params.put("model", this.model);
            
            //问题
            params.put("question", question);


            post(PATH, params, new AsyncCallbackPair<JSONObject>() {
                public void onSuccess(JSONObject response) {
                    WritingMaster.this.result = response.getString("data");
                    WritingMaster.this.GotResult();
                }

                public void onFailure(String message) {
                    WritingMaster.this.raiseError("WritingMaster.Writing", -1, message);
                }
            });
        } catch (Exception e) {
            raiseError("WritingMaster.Writing", -1, e.toString());
        }
    }

    //返回回答
    @SimpleFunction(description = "Get translation results")
    public String GetResult() {
        return this.result;
    }


    //返回结果时触发
    @SimpleEvent(description = "Got Result")
    public void GotResult(){
        EventDispatcher.dispatchEvent(getInstance(), "GotResult");
    }

}