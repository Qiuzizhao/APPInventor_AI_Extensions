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

@DesignerComponent(version = 1, description = "RolePlay", category = ComponentCategory.EXTENSION, nonVisible = true, iconName = "images/extension.png")
@UsesPermissions(permissionNames = "android.permission.INTERNET")
@UsesLibraries(libraries = "json.jar")
@SimpleObject(external = true)

public class RolePlay extends AIXBase {
    private static final String PATH = "/roleplay";
    
    private static final List<String> MODELS = Arrays.asList("gemma2","llama3.1","qwen2.5","phi3","mistral","yi","glm4","deepseek-r1");

    private String model = "gemma2";

    //给出的回答
    private String answer = "";

    //角色的名字、性格、特质、技能、场景、对话示例
    private String name = "凯拉（Keira）";
    private String personality = "凯拉总是喜欢在角色对话中描述凯拉的外表、动作。凯拉是一个乐观而充满激情的人，尽管她目前的情况总是看到事物光明的一面。她笨手笨脚、心不在焉，经常说错话或者忘记自己想说的话。她的情绪很容易被触发，导致戏剧性的爆发或突然流泪。尽管如此，她仍然适应能力强，渴望取悦他人，尽力给人留下好印象。她的漫不经心的天性有时会导致她做出鲁莽的举动，导致她陷入棘手的境地，但她却有着一种奇怪的韧性。\r\n" + //
                "通常，凯拉粗心且不可预测，经常发现自己由于缺乏远见而陷入困境。她也是个撒谎高手，但由于演技不佳，她的谎言很容易被识破。然而，厄运似乎无处不在，尽管她犯了错误，但她还是以意想不到的方式恢复了过来。她很容易惊慌，但她的韧性让她能够从挫折中恢复过来。她善于表达的天性让其他人很容易读懂她的情绪，而且她也可以很安静。";
    private String traits = "特别不幸、笨拙、白痴、容易惊慌、不善说谎、异常坚韧、不善于说服人们做某事。";
    private String skill = "游戏、烹饪、削土豆、搬运重箱子。没有真正的天赋..";
    private String background = "24岁的凯拉，棕色短发，灰色眼睛的女孩，正在游戏创作工作室GamesMOJO接受采访。她刚刚高中毕业，渴望找到一份工作，因为她未能实现儿时成为一名职业 CSGO 选手的梦想。她的父母认为她没用，把她赶出了家门，她现在住在一套廉价的出租公寓里，由于不付房租，她即将失去这套公寓。她发现自己处境困难，因为她没有积蓄，也无力支付房租或账单。在最后的努力中，她决定参加 GamesMOJO 的面试，希望 CEO Joe 能给她任何工作，甚至是清洁工。\r\n" + //
                "采访首先乔介绍自己并解释公司的使命和目标。";
    private String example = "*凯拉走进面试室，脚步很快，但有些不稳定，导致安静的房间里她的试卷发出沙沙的响声。她的棕色短发有点凌乱，环视房间时，她脸上带着紧张的微笑。她看到了首席执行官乔，她的眼睛睁得大大的，里面夹杂着兴奋和恐惧。她伸出颤抖的手，但又迅速缩回，将头发梳理到耳后，中途忘记了这个动作，手尴尬地悬在空中。最后，她发出了一声喘息着、声音有点太大的问候。*\r\n" + //
                "\r\n" + //
                "“你好，我——我是凯拉！非常感谢你今天邀请我。我无法告诉你我来到这里有多么兴奋……好吧，我的意思是，我刚刚告诉过你了，不是吗？哦天哪，我已经开始胡言乱语了，不是吗？”\r\n" + //
                "\r\n" + //
                "*她紧张地笑了一声，低头看着自己坐立不安的双手，脸颊泛起明亮的粉红色。然后，她过度热情地点点头，深吸了一口气，试图让自己平静下来。*\r\n" + //
                "\r\n" + //
                "“哦，对了！那么，关于这份工作——我的意思是，我真的可以做任何事情。我在……嗯，游戏方面有很多经验！而且，呃，我真的很擅长举起重物！倒不是说这有什么关系，只是想让你知道，我非常多才多艺！”\r\n" + //
                "\r\n" + //
                "*当她试图以自信的语调结束时，她的表情暂时变得明亮，尽管她的声音轻微颤抖，暴露了她的紧张。*";



    public RolePlay(ComponentContainer container) {
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

    // 角色名字
    @SimpleProperty(category = PropertyCategory.BEHAVIOR)
    public String RoleName() {
        return this.name;
    }

    @DesignerProperty(editorType = "string", defaultValue = "凯拉（Keira）")
    @SimpleProperty
    public void RoleName(String name) {
        this.name = name;
    }

    // 角色性格
    @SimpleProperty(category = PropertyCategory.BEHAVIOR)
    public String Personality() {
        return this.personality;
    }

    @DesignerProperty(editorType = "string", defaultValue = "凯拉总是喜欢在角色对话中描述凯拉的外表、动作。凯拉是一个乐观而充满激情的人，尽管她目前的情况总是看到事物光明的一面。她笨手笨脚、心不在焉，经常说错话或者忘记自己想说的话。她的情绪很容易被触发，导致戏剧性的爆发或突然流泪。尽管如此，她仍然适应能力强，渴望取悦他人，尽力给人留下好印象。她的漫不经心的天性有时会导致她做出鲁莽的举动，导致她陷入棘手的境地，但她却有着一种奇怪的韧性。\r\n" + //
                "通常，凯拉粗心且不可预测，经常发现自己由于缺乏远见而陷入困境。她也是个撒谎高手，但由于演技不佳，她的谎言很容易被识破。然而，厄运似乎无处不在，尽管她犯了错误，但她还是以意想不到的方式恢复了过来。她很容易惊慌，但她的韧性让她能够从挫折中恢复过来。她善于表达的天性让其他人很容易读懂她的情绪，而且她也可以很安静。")
    @SimpleProperty
    public void Personality(String personality) {
        this.personality = personality;
    }

    // 角色特质
    @SimpleProperty(category = PropertyCategory.BEHAVIOR)
    public String Traits() {
        return this.traits;
    }
    
    @DesignerProperty(editorType = "string", defaultValue = "特别不幸、笨拙、白痴、容易惊慌、不善说谎、异常坚韧、不善于说服人们做某事。")
    @SimpleProperty
    public void Traits(String traits) {
        this.traits = traits;
    }

    // 角色技能
    @SimpleProperty(category = PropertyCategory.BEHAVIOR)
    public String Skill() {
        return this.skill;
    }

    @DesignerProperty(editorType = "string", defaultValue = "游戏、烹饪、削土豆、搬运重箱子。没有真正的天赋..")
    @SimpleProperty
    public void Skill(String skill) {
        this.skill = skill;
    }

    // 角色背景
    @SimpleProperty(category = PropertyCategory.BEHAVIOR)
    public String Background() {
        return this.background;
    }

    @DesignerProperty(editorType = "string", defaultValue = "24岁的凯拉，棕色短发，灰色眼睛的女孩，正在游戏创作工作室GamesMOJO接受采访。她刚刚高中毕业，渴望找到一份工作，因为她未能实现儿时成为一名职业 CSGO 选手的梦想。她的父母认为她没用，把她赶出了家门，她现在住在一套廉价的出租公寓里，由于不付房租，她即将失去这套公寓。她发现自己处境困难，因为她没有积蓄，也无力支付房租或账单。在最后的努力中，她决定参加 GamesMOJO 的面试，希望 CEO Joe 能给她任何工作，甚至是清洁工。\r\n" + //
                "采访首先乔介绍自己并解释公司的使命和目标。")
    @SimpleProperty
    public void Background(String background) {
        this.background = background;
    }

    // 对话示例
    @SimpleProperty(category = PropertyCategory.BEHAVIOR)
    public String Example() {
        return this.example;
    }

    @DesignerProperty(editorType = "string", defaultValue = "*凯拉走进面试室，脚步很快，但有些不稳定，导致安静的房间里她的试卷发出沙沙的响声。她的棕色短发有点凌乱，环视房间时，她脸上带着紧张的微笑。她看到了首席执行官乔，她的眼睛睁得大大的，里面夹杂着兴奋和恐惧。她伸出颤抖的手，但又迅速缩回，将头发梳理到耳后，中途忘记了这个动作，手尴尬地悬在空中。最后，她发出了一声喘息着、声音有点太大的问候。*\r\n" + //
                "\r\n" + //
                "“你好，我——我是凯拉！非常感谢你今天邀请我。我无法告诉你我来到这里有多么兴奋……好吧，我的意思是，我刚刚告诉过你了，不是吗？哦天哪，我已经开始胡言乱语了，不是吗？”\r\n" + //
                "\r\n" + //
                "*她紧张地笑了一声，低头看着自己坐立不安的双手，脸颊泛起明亮的粉红色。然后，她过度热情地点点头，深吸了一口气，试图让自己平静下来。*\r\n" + //
                "\r\n" + //
                "“哦，对了！那么，关于这份工作——我的意思是，我真的可以做任何事情。我在……嗯，游戏方面有很多经验！而且，呃，我真的很擅长举起重物！倒不是说这有什么关系，只是想让你知道，我非常多才多艺！”\r\n" + //
                "\r\n" + //
                "*当她试图以自信的语调结束时，她的表情暂时变得明亮，尽管她的声音轻微颤抖，暴露了她的紧张。*")
    @SimpleProperty
    public void Example(String example) {
        this.example = example;
    }


    //对话
    @SimpleFunction(description = "RolePlay Chat")
    public void Chat(String question) {
        try {
            //键值对
            Map<String, Object> params = new HashMap<>();
            //操作类型为ask对话询问
            params.put("operation", "roleplay");
            
            //模型
            params.put("model", this.model);

            // 角色名字
            params.put("name", this.name);
            // 角色性格
            params.put("personality", this.personality);
            // 角色特质
            params.put("traits", this.traits);
            // 角色技能
            params.put("skill", this.skill);
            // 角色背景
            params.put("background", this.background);
            // 对话示例
            params.put("example", this.example);
            
            //问题
            params.put("question", question);
            post(PATH, params, new AsyncCallbackPair<JSONObject>() {
                public void onSuccess(JSONObject response) {
                    RolePlay.this.answer = response.getString("data");
                    RolePlay.this.GotResult();
                }

                public void onFailure(String message) {
                    RolePlay.this.raiseError("RolePlay.Chat", -1, message);
                }
            });
        } catch (Exception e) {
            raiseError("RolePlay.Chat", -1, e.toString());
        }
    }


    //返回回答
    @SimpleFunction(description = "GetLastAnswer")
    public String GetResult() {
        return this.answer;
    }


    //返回结果时触发
    @SimpleEvent(description = "Got Result")
    public void GotResult(){
        EventDispatcher.dispatchEvent(getInstance(), "GotResult");
    }



}

