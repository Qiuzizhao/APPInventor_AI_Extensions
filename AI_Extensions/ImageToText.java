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

@DesignerComponent(version = 1, description = "ImageToText", category = ComponentCategory.EXTENSION, nonVisible = true, iconName = "images/extension.png")
@UsesPermissions(permissionNames = "android.permission.INTERNET")
@UsesLibraries(libraries = "json.jar")
@SimpleObject(external = true)

public class ImageToText extends AIXBase {

    private static final String PATH = "/image_to_text";

    // 返回的文本
    private String result = "";

    // 提问的文本
    private String text = "描述这张图片。";

    public ImageToText(ComponentContainer container) {
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

    // 提问的文本
    @SimpleProperty(category = PropertyCategory.BEHAVIOR)
    public String Text() {
        return this.text;
    }

    @DesignerProperty(editorType = "string", defaultValue = "描述这张图片。")
    @SimpleProperty
    public void Text(String text) {
        this.text = text;
    }

    // 图生文
    @SimpleFunction(description = "DescribeImage")
    public void DescribeImage(String inputImage){
        try {
            Map<String, Object> params = new HashMap<>();
            
            //操作类型为image_to_text图生文
            params.put("operation", "image_to_text");

            // 图片
            params.put("image", b64encode(FileUtil.readFile(this.form, inputImage)));

            // 文本
            params.put("text", this.text);

            post(PATH, params, new AsyncCallbackPair<JSONObject>() {
                public void onSuccess(JSONObject resp) {
                    try {
                        ImageToText.this.result = resp.getString("data");
                        ImageToText.this.GotResult();
                    } catch (Exception e) {
                        ImageToText.this.raiseError("ImageToText.DescribeImage", -1, e.toString());
                    }
                }

                public void onFailure(String msg) {
                    ImageToText.this.raiseError("ImageToText.DescribeImage", -1, msg);
                }
            });
        } catch (Exception e) {
            raiseError("ImageToText.DescribeImage", -1, e.toString());
        }

    }

    //返回回答
    @SimpleFunction(description = "Get a description of the picture")
    public String GetDescription() {
        return this.result;
    }


    //返回结果时触发
    @SimpleEvent(description = "Got Result")
    public void GotResult(){
        EventDispatcher.dispatchEvent(getInstance(), "GotResult");
    }

}