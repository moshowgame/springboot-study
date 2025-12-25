# SpringBoot 学习项目集合

这是一个专注于 SpringBoot 框架学习与实践的综合项目，包含了多个不同技术栈和应用场景的子项目。通过实际项目帮助深入理解 SpringBoot 的核心概念和最佳实践。

```bash
-------------------------------------
- 🚀 Powered by Moshow 郑锴
- 🌟 Might the holy code be with you!
-------------------------------------
🔍 公众号 👉 软件开发大百科
💻 CSDN 👉 https://zhengkai.blog.csdn.net
📂 GitHub 👉 https://github.com/moshowgame
```

## 📚 项目列表

### [SpringBoot + Jetty](./springboot-jetty/)
**状态**: ✅ 已完成  
**描述**: SpringBoot 3.5.8 与 Jetty 12.0.15 服务器集成的演示项目  
**技术栈**: SpringBoot 3.5.8 + Jetty 12.0.15 + Java 17  
**特性**:
- 优化的 Jetty 配置（线程池、连接器、缓冲区等）
- HTTP GET DEMO请求示例
- 监控和日志配置
- [Jetty 官方文档](https://www.eclipse.org/jetty/documentation/)

**快速开始**:
```bash
cd springboot-jetty
mvn spring-boot:run
# 访问: http://localhost:8080
```

---

### [SpringBoot + Netty] 🚧
**状态**: 计划中  
**描述**: SpringBoot 与 Netty 异步网络框架集成  
**技术栈**: SpringBoot + Netty + WebFlux  
**计划功能**:
- 异步非阻塞编程
- WebSocket 实时通信
- 高并发处理
- 响应式编程模式

---


### [SpringBoot + MyBatis] 🚧
**状态**: 计划中  
**描述**: SpringBoot 与 MyBatis 数据持久层框架集成  
**技术栈**: SpringBoot + MyBatis + MySQL/PostgreSQL  
**计划功能**:
- 多数据源配置
- 动态 SQL 优化
- 事务管理
- 缓存集成

---

### [SpringBoot + Security] 🚧
**状态**: 计划中  
**描述**: SpringBoot 安全框架综合应用  
**技术栈**: SpringBoot + Spring Security + JWT  
**计划功能**:
- 认证与授权
- OAuth2 集成
- API 安全防护
- 角色权限管理

---

## 🎯 学习目标

通过本系列项目的学习，您将掌握：

- **SpringBoot 核心原理**: 自动配置、起步依赖、Actuator 监控
- **Web 服务器深度集成**: Jetty、Tomcat、Netty 的特性与调优
- **性能优化技巧**: 线程池配置、连接器调优、内存管理
- **现代部署实践**: 容器化、微服务、CI/CD
- **数据层最佳实践**: ORM 框架集成、数据库优化
- **安全防护体系**: 认证授权、API 安全、漏洞防护
- **缓存策略设计**: 分布式缓存、缓存雪崩防护

## 📋 环境要求

### 基础环境
- **Java**: JDK 17+
- **Maven**: 3.9+
- **IDE**: IntelliJ IDEA / Eclipse / VSCode


## 🚀 快速开始

1. **克隆项目**
```bash
git clone https://github.com/your-username/springboot-study.git
cd springboot-study
```

2. **选择子项目**
```bash
# 例如选择 Jetty 项目
cd springboot-jetty
```

3. **构建运行**
```bash
mvn clean compile
mvn spring-boot:run
```

4. **访问应用**
根据具体项目的说明访问相应的端点

## 🤝 贡献指南

欢迎为这个学习项目做出贡献！您可以：

1. **完善现有项目**: 修复 bug、优化代码、完善文档
2. **添加新项目**: 基于 SpringBoot 的技术实践
3. **分享经验**: 提供学习心得和最佳实践
4. **反馈建议**: 提出改进意见和学习需求

### 贡献步骤
1. Fork 本项目
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启 Pull Request

## 🔗 相关资源

### 官方文档
- [SpringBoot 官方文档](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Maven 官方文档](https://maven.apache.org/guides/)


## 📄 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情

## ⭐ 致谢

感谢所有为 SpringBoot 生态系统做出贡献的开发者们，以及为本学习项目提供帮助的朋友们。

---

**学习永不停止，代码改变世界！** 💪

> 如果您觉得这个项目对您的学习有帮助，请给个 Star 支持一下！