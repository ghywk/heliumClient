# Helium Client

![Minecraft 1.0.2](https://img.shields.io/badge/Minecraft-1.0.2-brightgreen)
![MCPBase](https://img.shields.io/badge/Based-MCPGradle-yellow)

基于 MCP Gradle 构建的Minecraft 1.8.9客户端框架，提供模块化游戏修改支持。

MCP Gradle : https://github.com/Physics3r/MCP-Gradle

## 技术架构

### 核心框架
```
├── GradleMCP 基础环境
├── 事件总线系统
├── 模块化管理系统
│   ├── 动态加载/卸载
│   └── 配置持久化
├── 基础渲染钩子
└── 轻量级注入系统
```

### 构建说明
* 依赖标准Gradle MCP
* 直接通过shadowJar构建
* 需要部分第三方依赖项

## 免责声明
* 本项目的开发仅用于以下目的：
* Minecraft游戏机制研究
* Java字节码修改技术学习
* 软件逆向工程教育

## 使用者须知：

本项目不包含任何预置功能模块，所有代码需自行实现

在多人服务器使用可能违反《Minecraft最终用户许可协议》(EULA)

使用本框架导致的账号封禁、服务器封禁等后果自行承担

禁止用于破坏性用途或商业盈利行为

开发者不对任何滥用行为负责

依据《计算机软件保护条例》第十七条规定，本框架仅供学习研究用途，使用者应当在下载后24小时内删除。任何单位或个人不得使用本项目从事违反中国法律法规的活动，包括但不限于：
• 破坏计算机信息系统安全
• 提供侵入、非法控制计算机信息系统的程序工具
• 其他危害网络安全的行为

## 许可协议
MIT License - 详见项目根目录LICENSE文件t

#### README文件由DeepSeek-R1生成并修改