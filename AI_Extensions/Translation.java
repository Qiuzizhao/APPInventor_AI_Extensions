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

@DesignerComponent(version = 1, description = "Translation", category = ComponentCategory.EXTENSION, nonVisible = true, iconName = "images/extension.png")
@UsesPermissions(permissionNames = "android.permission.INTERNET")
@UsesLibraries(libraries = "json.jar")
@SimpleObject(external = true)

public class Translation extends AIXBase {
    private static final String PATH = "/translation";

    private static final List<String> MODELS = Arrays.asList("gemma2","llama3.1","qwen2.5","phi3","mistral","yi","glm4","deepseek-r1");

    private String model = "gemma2";

    //给出的回答
    private String result = "";

    //翻译的目标语言
    private String target_language = "简体中文";

    public Translation(ComponentContainer container) {
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

    //目标语言
    @SimpleProperty(category = PropertyCategory.BEHAVIOR)
    public String TargetLanguage() {
        return this.target_language;
    }

    @DesignerProperty(editorType = "string", defaultValue = "简体中文")
    @SimpleProperty
    public void TargetLanguage(String target_language) {
        this.target_language = target_language;
    }


    //翻译
    @SimpleFunction(description = "Translate")
    public void TranslationToLanguage(String question, String target_language) {
        try {
            //键值对
            Map<String, Object> params = new HashMap<>();

            //操作类型为translation翻译
            params.put("operation", "translation");
            
            //模型
            params.put("model", this.model);
            
            //问题
            params.put("question", question);

            //目标语言
            if(target_language.equals("")){
                //如果函数执行的目标语言为空，则设置为全局目标语言
                target_language = Translation.this.target_language;
            }

            params.put("target_language", target_language);

            post(PATH, params, new AsyncCallbackPair<JSONObject>() {
                public void onSuccess(JSONObject response) {
                    Translation.this.result = response.getString("data");
                    Translation.this.GotResult();
                }

                public void onFailure(String message) {
                    Translation.this.raiseError("Translation.TranslationToLanguage", -1, message);
                }
            });
        } catch (Exception e) {
            raiseError("Translation.TranslationToLanguage", -1, e.toString());
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