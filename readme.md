# MineChatGPT
在Minecraft中与ChatGPT交流

所有的代码都是ChatGPT写的哦

## 功能
- [x] OpenAPI格式
- [x] 自定义模型
- [x] ChatGPT反代
- [ ] 指令补全
- [ ] 上下文对话
- [ ] 自定义prompt

## 安装
- 下载插件，放在plugins文件夹中
- 重启服务器

## 配置文件`config.yml`
```yaml
# API 相关设置
api:
  # 你的 OpenAI API key，用于身份验证
  # 获取 API key 的方法：访问 //platform.openai.com/account/api-keys 并创建一个新的 API key
  key: "your_openai_api_key"
  # OpenAI API 的基础 URL，用于构建请求
  base_url: "https://api.openai.com/v1"
# 支持的模型列表
models:
  # OpenAI ChatGPT
  - "gpt-3.5-turbo"
  - "gpt-4"
# 默认使用的模型
default_model: "gpt-3.5-turbo"
# 消息相关设置
messages:
  reload: "已重新加载配置文件！"
  help: "===== MineChatGPT 帮助 ====="
  help_ask: "/chatgpt <text> - 向ChatGPT提问"
  help_reload: "/chatgpt reload - 重新加载配置文件"
  help_model: "/chatgpt model <model_name> - 切换至其他模型"
  help_modellist: "/chatgpt modellist - 可用的模型列表"
  usage: "输入： /chatgpt model <model_name>"
  model_switch: "已切换至模型 %s"
  chatgpt_error: "无法联系ChatGPT。"
  chatgpt_response: "ChatGPT: %s"
  question: "你: %s"
  invalid_model: "模型无效。使用 /chatgpt modellist 查看可用模型。"
  available_models: "可用模型列表："
  no_permission: "你没有权限使用这个指令。需要的权限：%s"
```


## 常见问题
### `Failed to contact ChatGPT.` `无法联系ChatGPT。`
检查控制台输出的错误内容。
### `connect timeout` `connect reset`
检查`config.yml`中的`base_url`能否正常访问。如果你无法连接到OpenAI官方的API地址，可以考虑使用其他反代。

## 赞助
![afdian-ddaodan.jpeg](https://i.ddaodan.cn/images/afdian-ddaodan.jpeg)