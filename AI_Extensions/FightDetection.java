package AI_Extensions;

import com.google.appinventor.components.annotations.*;
import com.google.appinventor.components.runtime.*;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.util.AsyncCallbackPair;
import com.google.appinventor.components.runtime.util.FileUtil;
import com.google.appinventor.components.runtime.util.YailList;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

@DesignerComponent(version = 1, description = "Fight Detection", category = ComponentCategory.EXTENSION, nonVisible = true, iconName = "images/extension.png")
@UsesPermissions(permissionNames = "android.permission.INTERNET")
@UsesLibraries(libraries = "json.jar")
@SimpleObject(external = true)

public class FightDetection extends AIXBase{
    private static final String PATH = "/aix/yolo";

    // 返回的结果
    private JSONObject results;
    
    // 返回的图片base64
    private String resultBase64 = "";

    public FightDetection(ComponentContainer container) {
        super(container);
    }

    //服务器地址
    @SimpleProperty(category = PropertyCategory.BEHAVIOR)
    public String Server() {
        return SERVER;
    }

    @DesignerProperty(editorType = "string", defaultValue = "http://192.168.1.4:8000")
    @SimpleProperty
    public void Server(String url) {
        SERVER = url;
    }

    //检测打架行为
    @SimpleFunction(description = "DetectFight")
    public void DetectFight(String inputImage) {
        try {
            Map<String, Object> params = new HashMap<>();
            
            // 设置AI类型为YoloFight
            params.put("ai_type", "YoloFight");

            // 图片转base64
            params.put("image", b64encode(FileUtil.readFile(this.form, inputImage)));

            post(PATH, params, new AsyncCallbackPair<JSONObject>() {
                public void onSuccess(JSONObject response) {
                    FightDetection.this.results = response.getJSONObject("data");
                    FightDetection.this.resultBase64 = FightDetection.this.results.getString("image");
                    FightDetection.this.GotResult();
                }

                public void onFailure(String message) {
                    FightDetection.this.raiseError("FightDetection.DetectFight", -1, message);
                }
            });
        } catch (Exception e) {
            raiseError("FightDetection.DetectFight", -1, e.toString());
        }
    }

    //返回结果图片base64
    @SimpleFunction(description = "GetResultImageBase64")
    public String GetResultImageBase64() {
        return this.resultBase64;
    }

    //返回原始JSON数据
    @SimpleFunction(description = "GetRawJSONData")
    public String GetRawJSONData() {
        return (this.results != null) ? this.results.toString() : "";
    }

    //获取检测到的目标数量
    @SimpleFunction(description = "GetDetectedCount")
    public int GetDetectedCount() {
        if (this.results != null) {
            JSONArray categoryIds = this.results.getJSONArray("category_id");
            return categoryIds.length();
        }
        return 0;
    }

    //获取第i个检测目标的类别名称
    @SimpleFunction(description = "GetCategoryName")
    public String GetCategoryName(int index) {
        if (this.results != null && index >= 0 && index < GetDetectedCount()) {
            JSONArray categoryIds = this.results.getJSONArray("category_id");
            return categoryIds.getString(index);
        }
        return "";
    }

    //获取第i个检测目标的置信度
    @SimpleFunction(description = "GetScore")
    public double GetScore(int index) {
        if (this.results != null && index >= 0 && index < GetDetectedCount()) {
            JSONArray scores = this.results.getJSONArray("score");
            return scores.getDouble(index);
        }
        return 0.0;
    }

    //获取第i个检测目标的边界框
    @SimpleFunction(description = "GetBoundingBox")
    public YailList GetBoundingBox(int index) {
        if (this.results != null && index >= 0 && index < GetDetectedCount()) {
            JSONArray bboxes = this.results.getJSONArray("bbox");
            JSONArray bbox = bboxes.getJSONArray(index);
            return YailList.makeList(new Object[]{
                bbox.getDouble(0), // x
                bbox.getDouble(1), // y
                bbox.getDouble(2), // width
                bbox.getDouble(3)  // height
            });
        }
        return YailList.makeEmptyList();
    }

    //返回结果时触发
    @SimpleEvent(description = "Got Result")
    public void GotResult() {
        EventDispatcher.dispatchEvent(getInstance(), "GotResult");
    }
} 