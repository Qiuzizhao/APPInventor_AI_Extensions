package AI_Extensions;

import com.google.appinventor.components.annotations.*;
import com.google.appinventor.components.runtime.*;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.util.AsyncCallbackPair;
import com.google.appinventor.components.runtime.util.FileUtil;
import com.google.appinventor.components.runtime.util.YailList;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@DesignerComponent(version = 1, description = "Zero Classification", category = ComponentCategory.EXTENSION, nonVisible = true, iconName = "images/extension.png")
@UsesPermissions(permissionNames = "android.permission.INTERNET")
@UsesLibraries(libraries = "json.jar")
@SimpleObject(external = true)

public class ZeroClassification extends AIXBase {
    private static final String PATH = "/zero_classification";

    // 存储分类结果
    private JSONArray classificationResults = null;


    public ZeroClassification(ComponentContainer container) {
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

    //分类图片
    @SimpleFunction(description = "ClassifyImage")
    public void ClassifyImage(String inputImage, String labels) {
        try {
            Map<String, Object> params = new HashMap<>();
            
            //操作类型为zero_classification
            params.put("operation", "zero_classification");

            // 图片
            params.put("image", b64encode(FileUtil.readFile(this.form, inputImage)));

            // 标签
            params.put("labels", labels);

            post(PATH, params, new AsyncCallbackPair<JSONObject>() {
                public void onSuccess(JSONObject response) {
                    ZeroClassification.this.classificationResults = response.getJSONArray("data");
                    ZeroClassification.this.GotResult();
                }

                public void onFailure(String message) {
                    ZeroClassification.this.raiseError("ZeroClassification.ClassifyImage", -1, message);
                }
            });
        } catch (Exception e) {
            raiseError("ZeroClassification.ClassifyImage", -1, e.toString());
        }
    }

    // 返回结果
    @SimpleFunction(description = "GetResult")
    public Object GetResult() {
        // 将JSONArray转换为List<String>
        List<String> resultList = new ArrayList<>();
        for (int i = 0; i < this.classificationResults.length(); i++) {
            resultList.add(this.classificationResults.getString(i));
        }
        return YailList.makeList(resultList);
    }

    //返回结果时触发
    @SimpleEvent(description = "Got Result")
    public void GotResult() {
        EventDispatcher.dispatchEvent(getInstance(), "GotResult");
    }

} 