# MineChatGPT
![minechatgpt](https://socialify.git.ci/ddaodan/minechatgpt/image?description=1&descriptionEditable=%E5%9C%A8Minecraft%E4%B8%AD%E4%B8%8EChatGPT%E4%BA%A4%E6%B5%81&font=Inter&issues=1&language=1&name=1&pattern=Solid&stargazers=1&theme=Auto)  
[![modrinth-gallery](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy-minimal/documentation/modrinth-gallery_vector.svg)](https://modrinth.com/plugin/minechatgpt) [![spigot](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy-minimal/supported/spigot_vector.svg)](https://www.spigotmc.org/resources/minechatgpt.118963/) ![paper](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy-minimal/supported/paper_vector.svg) ![java8](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy-minimal/built-with/java8_vector.svg)  
![GitHub Release](https://img.shields.io/github/v/release/ddaodan/minechatgpt?label=version) ![bStats Servers](https://img.shields.io/bstats/servers/22635) ![bStats Players](https://img.shields.io/bstats/players/22635) [![Modrinth Downloads](https://img.shields.io/modrinth/dt/Op2X2eDG?logo=modrinth)](https://modrinth.com/plugin/minechatgpt/versions) [![Spiget Downloads](https://img.shields.io/spiget/downloads/118963?logo=spigotmc)](https://www.spigotmc.org/resources/minechatgpt.118963/)

在Minecraft中与ChatGPT交流  

所有的代码都是ChatGPT写的哦

## 功能
- OpenAPI格式
- 自定义模型
- ChatGPT反代
- 负载均衡
- 指令补全
- 上下文对话
- 多角色
- Folia支持

## 安装
1. 下载插件，放在plugins文件夹中
2. 重启服务器
  > 为兼容更多版本，插件没有规定Bukkit API version，因此在较高版本加载插件时，控制台可能会出现以下错误信息，这属于正常现象。  
  > ```
  > [Server thread/WARN]: Initializing Legacy Material Support. Unless you have legacy plugins and/or data this is a bug!
  > [Server thread/WARN]: Legacy plugin MineChatGPT v1.0 does not specify an api-version.
  > ```
3. 打开配置文件`config.yml`，修改以下设置：
  ```yaml
  # ======================================================
  # API Configuration
  # API 设置
  # ======================================================
  api:
    # Your OpenAI API keys, used for authentication
    # To obtain an API key, visit https://platform.openai.com/account/api-keys and create a new API key
    # 你的 OpenAI API key，用于身份验证
    # 获取 API key 的方法：访问 https://platform.openai.com/account/api-keys 并创建一个新的 API key
    keys:
      - "sk-your_openai_api_key_1"
      # You can add multiple API keys below
      # 可以添加多个API key
      # - "sk-your_openai_api_key_2"
      # - "sk-your_openai_api_key_3"
    
    # API key selection method: "round_robin" or "random"
    # Round Robin: Use each API key in turn
    # Random: Randomly select an API key
    # API key 选择方法："round_robin"（轮询）或 "random"（随机）
    # 轮询：依次使用每个API key
    # 随机：随机选择一个API key
    selection_method: "round_robin"
    
    # The base URL for the OpenAI API, used to construct requests
    # If you cannot access the official API, you can use a proxy service
    # OpenAI API 的基础 URL，用于构建请求
    # 如果你无法访问官方API，可以使用代理服务
    base_url: "https://api.openai.com/v1"
  ```
4. 在控制台中输入`/chatgpt reload`重新加载配置文件

## 截图
- 服务端截图（Spigot 1.20.1）
![](https://i.ddaodan.cc/images/CWindowssystem32cmd.exe_20240712406.png)
- 插件截图
![](https://i.ddaodan.cc/images/Minecraft_1.20.1_-__20240712407.png)
- 对话截图（使用FastGPT训练的自定义知识库）
![](https://i.ddaodan.cc/images/Minecraft_1.20.1_-__20240712408.png)

## 指令与权限
|指令|权限|描述|
|-|-|-|
|`/chatgpt`|minechatgpt.use|查看插件帮助|
|`/chatgpt <text>`|minechatgpt.use|向ChatGPT提问|
|`/chatgpt reload`|minechatgpt.reload|重新加载配置文件|
|`/chatgpt model <model_name>`|minechatgpt.model|切换至其他模型|
|`/chatgpt modellist`|minechatgpt.modellist|查看可用的模型列表|
|`/chatgpt context`|minechatgpt.context|切换连续对话模式|
|`/chatgpt clear`|minechatgpt.clear|清空对话历史|
|`/chatgpt character [character_name]`|minechatgpt.character|列出或切换角色|

## 兼容的版本
只列出经过测试的版本

|服务端|支持情况|
|-|-|
|Luminol 1.21|✔ 支持|
|Mohist 1.20.1|✔ 支持|
|Spigot 1.20.1|✔ 支持|
|Spigot 1.12.2|✔ 支持|
|KCauldron 1.7.10|× 不支持|

## 常见问题
### 提问后显示`Failed to contact ChatGPT.` `无法联系ChatGPT。`
检查控制台输出的错误内容。
### 提问后控制台有`connect timeout` `connect reset`等类似的提示
检查`config.yml`中的`base_url`能否正常访问。如果你无法连接到OpenAI官方的API地址，可以考虑使用其他反代。
### 我可以添加其他模型吗？
可以，只要模型支持OpenAI的API，就可以使用。  
不推荐使用推理模型，因为推理模型的响应时间较长，在没有流式响应的情况下，玩家会认为插件出现了问题。
### 我没有ChatGPT的账号，可以用吗？
可以，目前有很多代理网站，可以很轻松地使用，而且还支持其他模型，费用通常来说也会比官方便宜。如果你愿意，也可以使用我的代理，目前仅在我的QQ群：226385797中提供。
### 是否会支持Folia
理论上插件可以在Folia上运行，但插件的代码并没有针对Folia进行过优化，因此可能会有一些问题。如果你愿意，可以尝试使用Folia运行插件，但不保证插件可以正常运行。
## 赞助
[![](https://i.ddaodan.cn/images/afdian-ddaodan.jpeg)](https://afdian.com/a/ddaodan)
## 统计
[![](https://bstats.org/signatures/bukkit/MineChatGPT.svg)](https://bstats.org/plugin/bukkit/MineChatGPT/22635)