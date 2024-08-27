# MineChatGPT
![minechatgpt](https://socialify.git.ci/ddaodan/minechatgpt/image?description=1&descriptionEditable=%E5%9C%A8Minecraft%E4%B8%AD%E4%B8%8EChatGPT%E4%BA%A4%E6%B5%81&font=Inter&issues=1&language=1&name=1&pattern=Solid&stargazers=1&theme=Auto)  
![modrinth-gallery](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy-minimal/documentation/modrinth-gallery_vector.svg) ![spigot](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy-minimal/supported/spigot_vector.svg) ![paper](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy-minimal/supported/paper_vector.svg) ![java8](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy-minimal/built-with/java8_vector.svg)  
![GitHub Release](https://img.shields.io/github/v/release/ddaodan/minechatgpt?label=version) ![bStats Servers](https://img.shields.io/bstats/servers/22635) ![bStats Players](https://img.shields.io/bstats/players/22635) ![Modrinth Downloads](https://img.shields.io/modrinth/dt/Op2X2eDG?logo=modrinth) ![Spiget Downloads](https://img.shields.io/spiget/downloads/118963?logo=spigotmc)

在Minecraft中与ChatGPT交流  
理论支持全版本，欢迎测试

所有的代码都是ChatGPT写的哦

## 功能
- OpenAPI格式
- 自定义模型
- ChatGPT反代
- 指令补全
- 上下文对话
- 自定义prompt
- Folia支持

## 安装
1. 下载插件，放在plugins文件夹中
2. 重启服务器
> 为兼容更多版本，插件没有规定Bukkit API version，因此在较高版本加载插件时，控制台可能会出现以下错误信息，这属于正常现象。  
> ```
> [Server thread/WARN]: Initializing Legacy Material Support. Unless you have legacy plugins and/or data this is a bug!
> [Server thread/WARN]: Legacy plugin MineChatGPT v1.0 does not specify an api-version.
> ```
3. 打开配置文件`config.yml`，修改以下两项设置：
```yaml
# API 相关设置
api:
  # 你的 OpenAI API key，用于身份验证
  # 获取 API key 的方法：访问 https://platform.openai.com/account/api-keys 并创建一个新的 API key
  key: "sk-your_openai_api_key"
  # OpenAI API 的基础 URL，用于构建请求
  base_url: "https://api.openai.com/v1"
```
4. 在控制台中输入`/chatgpt reload`重新加载配置文件

## 截图
- 服务端截图（Spigot 1.20.1）
![](https://i.ddaodan.cn/images/CWindowssystem32cmd.exe_20240712406.png)
- 插件截图
![](https://i.ddaodan.cn/images/Minecraft_1.20.1_-__20240712407.png)
- 对话截图（使用FastGPT训练的自定义知识库）
![](https://i.ddaodan.cn/images/Minecraft_1.20.1_-__20240712408.png)
## 配置文件`config.yml`
```yaml
# API 相关设置
api:
  # 你的 OpenAI API key，用于身份验证
  # 获取 API key 的方法：访问 https://platform.openai.com/account/api-keys 并创建一个新的 API key
  key: "sk-your_openai_api_key"
  # OpenAI API 的基础 URL，用于构建请求
  base_url: "https://api.openai.com/v1"
# 支持的模型列表
models:
  # OpenAI ChatGPT
  - "gpt-3.5-turbo"
  - "gpt-3.5-turbo-instruct"
  - "gpt-4"
  - "gpt-4-turbo"
  - "gpt-4-turbo-preview"
  - "gpt-4o"
  - "gpt-4o-mini"
  # Google Gemini
  # - "gemini-pro"
  # - "gemini-1.5-pro"
  # Anthropic Claude
  # - "claude-3-opus"
  # - "claude-3-5-sonnet"
  # 以及更多...
# 默认使用的模型
default_model: "gpt-3.5-turbo"
conversation:
  # 连续对话开关
  context_enabled: false
  max_history_size: 10
prompt: "You are a helpful assistant.use Chinese."
# 消息相关设置
messages:
  reload: "&a已重新加载配置文件！"
  clear: "&a对话历史已清空！"
  help: "&e===== MineChatGPT 帮助 ====="
  help_ask: "&e/chatgpt <text> - 向ChatGPT提问"
  help_reload: "&e/chatgpt reload - 重新加载配置文件"
  help_model: "&e/chatgpt model <model_name> - 切换至其他模型"
  help_modellist: "&e/chatgpt modellist - 可用的模型列表"
  help_context: "&e/chatgpt context - 切换连续对话模式"
  help_clear: "/chatgpt clear - 清空对话历史"
  context_toggle: "&a连续对话模式已%s。"
  context_toggle_enabled: "开启"
  context_toggle_disabled: "关闭"
  current_model_info: "&e当前模型：%s，输入 /chatgpt model <model_name> 来切换模型。"
  model_switch: "&a已切换至模型 %s"
  chatgpt_error: "&c无法联系ChatGPT。"
  chatgpt_response: "&bChatGPT: %s"
  question: "&b你: %s"
  invalid_model: "&c模型无效。使用 /chatgpt modellist 查看可用模型。"
  available_models: "&e可用模型列表："
  no_permission: "&c你没有权限使用这个指令。需要的权限：%s"
# 如果你不知道这是什么，请不要动
debug: false
```

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
### 我没有ChatGPT的账号，可以用吗？
可以，目前有很多代理网站，可以很轻松地使用，而且还支持其他模型，费用通常来说也会比官方便宜。如果你愿意，也可以使用我的代理，目前仅在我的QQ群：226385797中提供。
### 是否会支持Folia
理论上插件可以在Folia上运行，但插件的代码并没有针对Folia进行过优化，因此可能会有一些问题。如果你愿意，可以尝试使用Folia运行插件，但不保证插件可以正常运行。
## 赞助
[![](https://i.ddaodan.cn/images/afdian-ddaodan.jpeg)](https://afdian.com/a/ddaodan)
## 统计
[![](https://bstats.org/signatures/bukkit/MineChatGPT.svg)](https://bstats.org/plugin/bukkit/MineChatGPT/22635)