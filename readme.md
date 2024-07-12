# MineChatGPT
在Minecraft中与ChatGPT交流

所有的代码都是ChatGPT写的哦

## 功能
- [x] OpenAPI格式
- [x] 自定义模型
- [x] ChatGPT反代
- [x] 指令补全
- [ ] 上下文对话
- [ ] 自定义prompt

## 安装
- 下载插件，放在plugins文件夹中
- 重启服务器
> 为兼容更多版本，插件使用1.13版本进行构建，因此在较高版本加载插件时，控制台会出现以下错误信息，这属于正常现象。  
> ```
> [Server thread/WARN]: Initializing Legacy Material Support. Unless you have legacy plugins and/or data this is a bug!
> [Server thread/WARN]: Legacy plugin MineChatGPT v1.0 does not specify an api-version.
> ```

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
  # 获取 API key 的方法：访问 //platform.openai.com/account/api-keys 并创建一个新的 API key
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
  # Google Gemini
  # - "gemini-pro"
  # - "gemini-1.5-pro"
  # Anthropic Claude
  # - "claude-3-opus"
  # - "claude-3-5-sonnet"
  # 以及更多...
# 默认使用的模型
default_model: "gpt-3.5-turbo"
# 消息相关设置
messages:
  reload: "&a已重新加载配置文件！"
  help: "&e===== MineChatGPT 帮助 ====="
  help_ask: "&e/chatgpt <text> - 向ChatGPT提问"
  help_reload: "&e/chatgpt reload - 重新加载配置文件"
  help_model: "&e/chatgpt model <model_name> - 切换至其他模型"
  help_modellist: "&e/chatgpt modellist - 可用的模型列表"
  usage: "&c输入： /chatgpt model <model_name>"
  model_switch: "&a已切换至模型 %s"
  chatgpt_error: "&c无法联系ChatGPT。"
  chatgpt_response: "&bChatGPT: %s"
  question: "&b你: %s"
  invalid_model: "&c模型无效。使用 /chatgpt modellist 查看可用模型。"
  available_models: "&e可用模型列表："
  no_permission: "&c你没有权限使用这个指令。需要的权限：%s"
```
## 兼容的版本
✔ = 完全支持  
？ = 部分支持  
× = 不支持  
只列出经过测试的版本
|服务端|支持情况|
|-|-|
|Mohist 1.20.1|✔|
|Spigot 1.20.1|✔|
|Spigot 1.12.2|✔|

## 常见问题
### `Failed to contact ChatGPT.` `无法联系ChatGPT。`
检查控制台输出的错误内容。
### `connect timeout` `connect reset`
检查`config.yml`中的`base_url`能否正常访问。如果你无法连接到OpenAI官方的API地址，可以考虑使用其他反代。

## 赞助
![afdian-ddaodan.jpeg](https://i.ddaodan.cn/images/afdian-ddaodan.jpeg)