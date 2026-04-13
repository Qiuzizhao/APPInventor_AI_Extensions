# App Inventor AI 扩展组件库 (App Inventor AI Extensions)

本项目提供了一系列适用于 MIT App Inventor 的人工智能扩展组件。通过这些扩展，App Inventor 开发者可以轻松地在他们的移动应用中集成各种强大的 AI 功能，包括大语言模型对话、图像识别、目标检测、文本处理、语音合成以及多模态生成等。

## 📑 目录 (Table of Contents)

- [1. 项目结构](#1-项目结构)
- [2. 代码实现原理](#2-代码实现原理)
- [3. 代码的具体实现结构](#3-代码的具体实现结构)
- [4. 核心功能及效果展示](#4-核心功能及效果展示)
  - [4.1 计算机视觉 (Computer Vision)](#41-计算机视觉-computer-vision)
  - [4.2 大语言模型与对话 (LLM & ChatBot)](#42-大语言模型与对话-llm--chatbot)
  - [4.3 文本与自然语言处理 (NLP)](#43-文本与自然语言处理-nlp)
  - [4.4 语音与音频 (Audio & Speech)](#44-语音与音频-audio--speech)
  - [4.5 图像生成与处理 (Image Generation & Processing)](#45-图像生成与处理-image-generation--processing)
- [5. 演示 DEMO](#5-演示-demo)

---

## 1. 项目结构

整个项目主要分为以下三个主要目录：

```text
App Inventor/
├── AI_Extensions/                # 核心代码库：包含所有 Java 编写的 App Inventor 扩展源码
│   ├── AIXBase.java              # 核心基类：处理 HTTP 网络请求、Base64 编解码及回调分发机制
│   ├── AnimalDetection.java      # 动物识别组件：用于检测和识别图片中的多种动物
│   ├── AnsGen.java               # 文本问答组件：基于输入文本生成精准回答
│   ├── CarPlateDetection.java    # 车牌检测组件：用于识别和定位图片中的车辆牌照
│   ├── Cartoonize.java           # 人像卡通化组件：将真实人像照片转化为动漫风格
│   ├── ChatBot.java              # 聊天机器人组件：支持多种大语言模型(LLM)的多轮对话交互
│   ├── DogDetection.java         # 狗类检测组件：专门用于检测图片中的狗
│   ├── DogLeashDetection.java    # 遛狗绳检测组件：检测画面中的狗是否佩戴牵引绳
│   ├── ExpressionDetection.java  # 面部表情识别组件：检测人脸并分析其情绪/表情
│   ├── FallDetection.java        # 跌倒检测组件：识别画面中人物是否发生跌倒动作
│   ├── FightDetection.java       # 斗殴检测组件：检测视频/图片中是否发生打架斗殴事件
│   ├── FireDetection.java        # 火灾检测组件：识别画面中是否存在明火
│   ├── FlowerDetection.java      # 花卉识别组件：检测并识别不同种类的花朵
│   ├── FruitDetection.java       # 水果识别组件：检测并识别各类水果
│   ├── GarbageDetection.java     # 垃圾检测组件：识别画面中的垃圾目标
│   ├── GarbageSortDetection.java # 垃圾分类检测组件：对识别到的垃圾进行智能分类
│   ├── GestureDetection.java     # 手势识别组件：检测并识别人物的手势动作
│   ├── HelmetDetection.java      # 安全帽检测组件：检测工人是否佩戴安全帽
│   ├── ImageEmbedding.java       # 图像特征提取组件：将图片转化为高维向量用于检索比对
│   ├── ImageToText.java          # 图像视觉理解组件(VQA)：对图片内容进行提问并获取文本描述
│   ├── KnowledgeBase.java        # 知识库管理组件：配合大模型实现知识库增强(RAG)功能
│   ├── MCQGen.java               # 选择题生成组件：根据文本材料自动生成多项选择题
│   ├── MaskDetection.java        # 口罩检测组件：识别人物是否正确佩戴口罩
│   ├── MindMap.java              # 思维导图组件：将文本结构化并输出为思维导图格式数据
│   ├── MusicGen.java             # 音乐生成组件：根据文本描述提示词自动生成音乐片段
│   ├── ObjectDetection.java      # 通用目标检测组件：利用 YOLO 等模型识别常见物体及其坐标框
│   ├── PhoneDetection.java       # 玩手机检测组件：检测人物是否在违规使用手机
│   ├── QuestionGen.java          # 简答题生成组件：根据背景材料自动生成简答题目
│   ├── RemoveBackground.java     # 智能抠图组件：识别图像主体并消除背景
│   ├── RolePlay.java             # 角色扮演组件：赋予 AI 特定人设进行交互对话
│   ├── SmokeDetection.java       # 烟雾检测组件：识别画面中是否出现烟雾
│   ├── TTS.java                  # 语音合成组件：将文本转换为自然流畅的语音音频
│   ├── TextEmbedding.java        # 文本特征提取组件：将文本转化为向量用于语义搜索
│   ├── TextSort.java             # 文本相似度排序组件：计算文本间的相似度并进行重排
│   ├── TextToImage.java          # 文生图组件：根据文本 Prompt 描述生成对应图像
│   ├── Translation.java          # 文本翻译组件：支持多语种之间的高质量互译
│   ├── WorkClothesDetection.java # 工装检测组件：检测工人是否按规定穿着工作服
│   ├── WritingMaster.java        # 写作大师组件：根据提示自动生成长文或创意文本
│   └── ZeroClassification.java   # 零样本分类组件：在无需训练数据的情况下对文本进行分类
├── AI插件演示DEMO/               # 演示项目：提供包含测试代码的 .aia 工程文件
│   ├── Qiu_AudioGen.aia          # 音频生成演示
│   ├── Qiu_ImageAnalysis.aia     # 图像分析演示
│   ├── Qiu_LLM.aia               # 大模型演示
│   ├── Qiu_ObjectDetection.aia   # 目标检测演示
│   └── Qiu_TextAnalysis.aia      # 文本分析演示
└── 效果图/                       # 功能效果展示：保存了各个组件在 App 中运行时的截图
```

![人工智能组件](效果图/人工智能组件.png)

---

## 2. 代码实现原理

本项目基于 **C/S（客户端-服务端）架构**。
由于 App Inventor 运行在移动端（Android），直接在手机上运行庞大的深度学习模型或大语言模型（如 YOLO, LLaMA 等）会有较大的性能瓶颈。因此，该项目采取了“云端推理，本地展示”的设计原理：

1. **服务端提供 AI 接口**：后端部署了基于 Flask/FastAPI 的 HTTP 接口（例如 `http://192.168.1.4:5000/aix`），运行各类 AI 模型，处理推理逻辑。
2. **客户端（扩展组件）请求**：App Inventor 的组件接收用户的输入（文本或本地图片）。如果是图片，组件会通过内置的方法将图片读取并进行 `Base64` 编码。
3. **网络通信**：通过标准的 HTTP POST 请求，组件将 JSON 格式的数据（包含模型名称、图片 Base64 码、提问内容等）发送给服务器。
4. **异步回调与事件触发**：服务器返回推理结果后，组件解析返回的 JSON 数据（提取识别的类别、坐标框、回答文本或生成的图片 Base64 等），然后通过 App Inventor 的事件系统（如 `GotResult`）通知 UI 层更新显示。

---

## 3. 代码的具体实现结构

所有的扩展都遵循了统一的设计模式和代码结构，主要依赖 App Inventor 的组件开发框架（Java 注解）：

### 3.1 核心基类：`AIXBase.java`
- **继承与注解**：继承自 `AndroidNonvisibleComponent`，提供后台运行的能力。
- **网络核心 `post()`**：封装了 HTTP POST 请求，使用 `HttpURLConnection` 和 `AsynchUtil.runAsynchronously` 进行异步网络请求，避免阻塞 UI 线程。
- **回调机制 `AsyncCallbackPair<JSONObject>`**：请求成功后，使用 `activity.runOnUiThread` 切回主线程，通过回调传递 JSON 数据或抛出错误信息。
- **工具方法**：包含 `b64encode()` 和 `b64decode()` 用于图片的 Base64 编解码；`toJSONString()` 用于将 Map 参数转化为 JSON 字符串。

### 3.2 功能子类：(如 `ObjectDetection.java`, `ChatBot.java` 等)
- **继承体系**：继承自 `AIXBase`。
- **组件注解**：使用 `@DesignerComponent` 注册组件在 App Inventor 的设计面板；使用 `@UsesPermissions` 请求网络权限 (`android.permission.INTERNET`)；使用 `@SimpleObject` 暴露为可视化组件。
- **属性配置 (`@SimpleProperty`)**：
  - 提供 `Server`（服务器地址）、`Model`（模型类型）等属性的 Get/Set 方法。
- **功能方法 (`@SimpleFunction`)**：
  - 例如 `DetectObjects(String inputImage)`：读取文件，组装参数 `Map`，调用父类的 `post()` 方法发送请求。
  - 数据获取方法：例如 `GetDetectedCount()`、`GetBoundingBox()`、`GetScore()` 解析并返回特定字段的数据给 App Inventor 拼图使用。
- **事件监听 (`@SimpleEvent`)**：
  - 提供类似 `GotResult()` 的事件，在网络请求 `onSuccess` 并且数据解析完毕后，通过 `EventDispatcher.dispatchEvent()` 触发积木块执行后续的逻辑。

---

## 4. 核心功能及效果展示

本项目集成了丰富的 AI 功能，按应用领域可分为以下几类：

### 4.1 计算机视觉 (Computer Vision)
提供了从通用目标检测到特定场景的目标识别。

- **通用目标检测 (Object Detection)**
  - 功能：识别图片中的多种物体，返回物体名称、置信度和坐标框 (Bounding Box)。
  - ![目标检测1](效果图/目标检测1.png)
  - ![目标检测2](效果图/目标检测2.png)
  - ![目标检测3](效果图/目标检测3.png)

### 4.2 大语言模型与对话 (LLM & ChatBot)
集成了多种开源和商用大语言模型，并支持知识库增强 (RAG)。

- **大模型聊天机器人 (ChatBot)**
  - 功能：支持 Gemma2, Llama3.1, Qwen2.5, DeepSeek-R1 等模型，可进行多轮对话。
  - ![大模型聊天机器人](效果图/大模型聊天机器人.png)
- **知识库问答 (RAG)**
  - 功能：结合外部知识库检索，让大模型能够基于专有知识进行精准回答。
  - ![RAG](效果图/RAG.png)
- **角色扮演 (Role Play)**
  - 功能：通过设定特定的 System Prompt，让 AI 扮演特定的角色与用户对话。
  - ![角色扮演](效果图/角色扮演.png)
- **图像视觉理解 (Image to Text / VQA)**
  - 功能：多模态大模型，上传图片并向 AI 提问图片内容。
  - ![图像视觉理解](效果图/图像视觉理解.png)

### 4.3 文本与自然语言处理 (NLP)
针对特定文本任务的专用功能。

- **写作大师 (Writing Master)**
  - 功能：根据提示词自动生成长篇文章、报告或创意文本。
  - ![写作大师](效果图/写作大师.png)
- **思维导图 (Mind Map)**
  - 功能：输入文本或主题，自动生成思维导图结构（通常配合前端组件渲染）。
  - ![思维导图](效果图/思维导图.png)
- **文本翻译 (Translation)**
  - 功能：多语种之间的高质量互相翻译。
  - ![文本翻译](效果图/文本翻译.png)
- **题目生成 (Question / MCQ Generation)**
  - 功能：输入背景材料，自动生成对应的简答题和选择题。
  - ![简答题生成](效果图/简答题生成.png)
  - ![选择题生成](效果图/选择题生成.png)
- **文本问答 (Answer Generation)**
  - 功能：基于给定文本进行快速精准的问答。
  - ![文本问答](效果图/文本问答.png)
- **文本相似度排序与零样本分类 (Text Sort & Zero Classification)**
  - 功能：计算文本之间的语义相似度并排序，或在无监督情况下对文本进行分类。
  - ![文本相似度排序](效果图/文本相似度排序.png)
  - ![文本相似度排序2](效果图/文本相似度排序2.png)
  - ![零样本分类](效果图/零样本分类.png)
- **文本特征处理 (Text Embedding)**
  - 功能：将文本转化为高维向量，用于检索或相似度比对。
  - ![文本特征处理](效果图/文本特征处理.png)
- **图像特征提取 (Image Embedding)**
  - 功能：提取图像向量，用于图搜图等业务。
  - ![图像特征提取](效果图/图像特征提取.png)

### 4.4 语音与音频 (Audio & Speech)
- **文本转语音 (TTS - Text To Speech)**
  - 功能：将文字合成为自然流畅的语音。
  - ![TTS](效果图/TTS.png)
- **音乐生成 (Music Gen)**
  - 功能：根据文本描述自动生成背景音乐片段。
  - ![音乐生成](效果图/音乐生成.png)

### 4.5 图像生成与处理 (Image Generation & Processing)
- **文生图 (Text To Image)**
  - 功能：输入描述词 (Prompt)，利用扩散模型等生成对应的图像。
  - ![文生图](效果图/文生图.png)
- **背景消除 (Remove Background)**
  - 功能：智能识别主体，一键抠图并去除背景。
  - ![背景消除](效果图/背景消除.png)
- **人像卡通化 (Cartoonize)**
  - 功能：将真实人像照片转化为卡通动漫风格。
  - ![人像卡通化](效果图/人像卡通化.png)

---

## 5. 演示 DEMO

在 `AI插件演示DEMO/` 目录中，我们提供了若干 `.aia` 格式的源码工程。您可以直接导入到 App Inventor (或类似平台) 中进行体验和二次开发：
- `Qiu_LLM.aia`: 大语言模型、聊天机器人及 RAG 演示
- `Qiu_ObjectDetection.aia`: 计算机视觉和目标检测演示
- `Qiu_TextAnalysis.aia`: 文本处理及特征提取演示
- `Qiu_ImageAnalysis.aia`: 图像处理（去背景、卡通化等）演示
- `Qiu_AudioGen.aia`: TTS 与音频生成演示
