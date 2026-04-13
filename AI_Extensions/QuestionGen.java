package AI_Extensions;

import com.google.appinventor.components.annotations.*;
import com.google.appinventor.components.runtime.*;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.util.AsyncCallbackPair;
import com.google.appinventor.components.runtime.util.FileUtil;
import com.google.appinventor.components.runtime.util.JsonUtil;
import com.google.appinventor.components.runtime.util.YailList;



import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@DesignerComponent(version = 1, description = "QuestionGen", category = ComponentCategory.EXTENSION, nonVisible = true, iconName = "images/extension.png")
@UsesPermissions(permissionNames = "android.permission.INTERNET")
@UsesLibraries(libraries = "json.jar")
@SimpleObject(external = true)

// 简答题生成
public class QuestionGen extends AIXBase {
    private static final String PATH = "/question_gen";

    private static final List<String> MODELS = Arrays.asList("gemma2","llama3.1","qwen2.5","phi3","mistral","yi","glm4");

    private String model = "gemma2";

    // 返回的结果
    private JSONArray results;

    // 主题、学科、题目数量、难度
    private String question = "";
    private String subject = "知识";
    private String number = "3";
    private String difficulty = "中等";

    public QuestionGen(ComponentContainer container) {
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

    // 主题
    @SimpleProperty(category = PropertyCategory.BEHAVIOR)
    public String Question() {
        return this.question;
    }

    @DesignerProperty(editorType = "string", defaultValue = "")
    @SimpleProperty
    public void Question(String question) {
        this.question = question;
    }

    // 学科
    @SimpleProperty(category = PropertyCategory.BEHAVIOR)
    public String Subject() {
        return this.subject;
    }

    @DesignerProperty(editorType = "string", defaultValue = "知识")
    @SimpleProperty
    public void Subject(String subject) {
        this.subject = subject;
    }


    // 题目数量
    @SimpleProperty(category = PropertyCategory.BEHAVIOR)
    public String Number() {
        return this.number;
    }

    @DesignerProperty(editorType = "string", defaultValue = "3")
    @SimpleProperty
    public void Number(String number) {
        this.number = number;
    }

    // 难度
    @SimpleProperty(category = PropertyCategory.BEHAVIOR)
    public String Difficulty() {
        return this.difficulty;
    }

    @DesignerProperty(editorType = "string", defaultValue = "中等")
    @SimpleProperty
    public void Difficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    // 生成简答题
    @SimpleFunction(description = "QuestionGen")
    public void Generate() {
        try {
            //键值对
            Map<String, Object> params = new HashMap<>();
            //操作类型为question_gen生成简答题
            params.put("operation", "question_gen");

            //模型
            params.put("model", this.model);
            
            //主题
            params.put("question", this.question);

            //学科
            params.put("subject", this.subject);

            //题目数量
            params.put("number", this.number);

            //难度
            params.put("difficulty", this.difficulty);

            post(PATH, params, new AsyncCallbackPair<JSONObject>() {
                public void onSuccess(JSONObject response) {
                    
                    QuestionGen.this.results = response.getJSONArray("data");
                    QuestionGen.this.GotResult();
                }
                
                public void onFailure(String message) {
                    QuestionGen.this.raiseError("QuestionGen.Generate", -1, message);
                }
            });
        } catch (Exception e) {
            raiseError("QuestionGen.Generate", -1, e.toString());
        }
    }

    // 返回第i个问题
    @SimpleFunction(description = "Get i-th Question")
    public String GetQuestion(int i) {
        return this.results.getString(i-1);
    }

    // 返回问题列表
    @SimpleFunction(description = "Get Question List")
    public Object GetQuestionList() {
        // 将JSONArray转换为List<String>
        List<String> questionList = new ArrayList<>();
        for (int i = 0; i < this.results.length(); i++) {
            questionList.add(this.results.getString(i));
        }
        return YailList.makeList(questionList);
        // return JsonUtil.getListFromJsonArray(results, false)
    }

    //返回结果时触发
    @SimpleEvent(description = "Got Result")
    public void GotResult(){
        EventDispatcher.dispatchEvent(getInstance(), "GotResult");
    }



}    