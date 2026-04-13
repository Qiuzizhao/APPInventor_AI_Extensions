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

@DesignerComponent(version = 1, description = "MCQGen", category = ComponentCategory.EXTENSION, nonVisible = true, iconName = "images/extension.png")
@UsesPermissions(permissionNames = "android.permission.INTERNET")
@UsesLibraries(libraries = "json.jar")
@SimpleObject(external = true)


//选择题生成
public class MCQGen extends AIXBase {
    private static final String PATH = "/mcq_gen";

    private static final List<String> MODELS = Arrays.asList("gemma2","llama3.1","qwen2.5","phi3","mistral","yi","glm4","deepseek-r1");

    private String model = "gemma2";

    // 返回的结果
    private JSONObject results;

    // 主题、学科、题目数量、难度
    private String question = "";
    private String subject = "知识";
    private String number = "3";
    private String difficulty = "中等";
    
    public MCQGen(ComponentContainer container) {
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


    //生成选择题
    @SimpleFunction(description = "GenerateMCQ")
    public void Generate() {
        try {
            //键值对
            Map<String, Object> params = new HashMap<>();
            //操作类型为mcq_gen生成简答题
            params.put("operation", "mcq_gen");

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
                    MCQGen.this.results = response.getJSONObject("data");
                    MCQGen.this.GotResult();
                }

                public void onFailure(String message) {
                    MCQGen.this.raiseError("MCQGen.Generate", -1, message);
                }
            });

        } catch (Exception e) {
            raiseError("MCQGen.Generate", -1, e.toString());
        }
    }  

    // 返回选择题Json
    @SimpleFunction(description = "RawJSONData")
    public String RawJSONData() {
        return (this.results != null) ? this.results.toString() : "";
    }

    // 结果获取real

    // 返回第i个问题
    public String real_getQuestion(String i){
        return this.results.getJSONObject(i).getString("mcq");
    }

    // 返回第i个问题的答案
    public String real_getCorrectAnswer(String i){
        return this.results.getJSONObject(i).getString("correct");
    }

    // 返回第i个问题的选项
    public String real_getOptions(String i){
        String option_a = "a." + this.results.getJSONObject(i).getJSONObject("options").getString("a");
        String option_b = "b." + this.results.getJSONObject(i).getJSONObject("options").getString("b");
        String option_c = "c." + this.results.getJSONObject(i).getJSONObject("options").getString("c");
        String option_d = "d." + this.results.getJSONObject(i).getJSONObject("options").getString("d");
        return option_a + "\n" + option_b + "\n" + option_c + "\n" + option_d;
    }

    // 返回第i个问题的选项a
    public String real_getOption_a(String i){
        return this.results.getJSONObject(i).getJSONObject("options").getString("a");
    }

    // 返回第i个问题的选项b
    public String real_getOption_b(String i){
        return this.results.getJSONObject(i).getJSONObject("options").getString("b");
    }

    // 返回第i个问题的选项c
    public String real_getOption_c(String i){
        return this.results.getJSONObject(i).getJSONObject("options").getString("c");
    }

    // 返回第i个问题的选项d
    public String real_getOption_d(String i){
        return this.results.getJSONObject(i).getJSONObject("options").getString("d");
    }

    // 返回第i个问题+选项
    public String real_getQuestion_Options(String i){
        return this.results.getJSONObject(i).getString("mcq") + "\n" + real_getOptions(i);
    }

    // 返回第i个问题+选项+答案
    public String real_getQuestion_Options_Correct(String i){
        return real_getQuestion_Options(i) + "\n" + "答案：" + real_getCorrectAnswer(i);
    }

    // 返回全部问题和选项
    public String real_getAllQuestion_Options(){
        String result = "";
        for(int i = 0; i < this.results.length(); i++){
            //将i+1转换为String
            String index = String.valueOf(i+1);
            result += real_getQuestion_Options(index) + "\n\n";
        }
        return result;
    }

    // 返回全部问题和选项和答案
    public String real_getAllQuestion_Options_Correct(){
        String result = "";
        for(int i = 0; i < this.results.length(); i++){
            //将i+1转换为String
            String index = String.valueOf(i+1);
            result += real_getQuestion_Options_Correct(index) + "\n\n";
        }
        return result;
    }

    //返回结果组件

    // 返回第i个问题
    @SimpleFunction(description = "Get i-th Question")
    public String GetQuestion(String i){
        return real_getQuestion(i);
    }

    // 返回第i个问题的答案
    @SimpleFunction(description = "Get i-th Correct Answer")
    public String GetCorrectAnswer(String i){
        return real_getCorrectAnswer(i);
    }

    // 返回第i个问题的选项
    @SimpleFunction(description = "Get i-th Options")
    public String GetOptions(String i){
        return real_getOptions(i);
    }

    // 返回第i个问题的选项a
    @SimpleFunction(description = "Get i-th Option a")
    public String GetOption_a(String i){
        return real_getOption_a(i);
    }

    // 返回第i个问题的选项b
    @SimpleFunction(description = "Get i-th Option b")
    public String GetOption_b(String i){
        return real_getOption_b(i);
    }

    // 返回第i个问题的选项c
    @SimpleFunction(description = "Get i-th Option c")
    public String GetOption_c(String i){
        return real_getOption_c(i);
    }

    // 返回第i个问题的选项d
    @SimpleFunction(description = "Get i-th Option d")
    public String GetOption_d(String i){
        return real_getOption_d(i);
    }

    // 返回第i个问题+选项
    @SimpleFunction(description = "Get i-th Question_Options")
    public String GetQuestion_Options(String i){
        return real_getQuestion_Options(i);
    }

    // 返回第i个问题+选项+答案
    @SimpleFunction(description = "Get i-th Question_Options_Correct")
    public String GetQuestion_Options_Correct(String i){
        return real_getQuestion_Options_Correct(i);
    }

    // 返回全部问题和选项
    @SimpleFunction(description = "Get All Question_Options")
    public String GetAllQuestion_Options(){
        return real_getAllQuestion_Options();
    }

    // 返回全部问题和选项和答案
    @SimpleFunction(description = "Get All Question_Options_Correct")
    public String GetAllQuestion_Options_Correct(){
        return real_getAllQuestion_Options_Correct();
    }


    //返回结果时触发
    @SimpleEvent(description = "Got Result")
    public void GotResult(){
        EventDispatcher.dispatchEvent(getInstance(), "GotResult");
    }


}