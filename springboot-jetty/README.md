# SpringBoot 3.5.8 + Jetty Demo Project

这是一个展示SpringBoot 3.5.8与Jetty服务器集成的演示项目，包含优化的Jetty配置和HTTP请求示例。

## 项目特性

- SpringBoot 3.5.8 + Jetty 12.0.15
- 优化的Jetty配置（线程池、连接器、缓冲区等）
- 完整的HTTP GET/POST请求示例
- 异步请求处理
- 监控和日志配置
- 支持SSL配置

## 快速开始

### 环境要求

- Java 17+
- Maven 3.6+

### 运行项目

1. 克隆或下载项目
2. 进入项目目录
3. 执行以下命令：

```bash
# 编译项目
mvn clean compile

# 运行项目
mvn spring-boot:run

# 或者打包运行
mvn clean package
java -jar target/springboot-jetty-demo.jar
```

### 访问应用

- 应用主页: http://localhost:8080
- 健康检查: http://localhost:8080/actuator/health
- 应用信息: http://localhost:8080/actuator/info

## API 接口示例

### GET 请求

#### 1. Hello接口
```bash
# 基础请求
GET http://localhost:8080/api/demo/hello

# 带参数
GET http://localhost:8080/api/demo/hello?name=Jetty

```

## Jetty 配置优化

### application.yml 主要配置项

```yaml
  jetty:
    # 线程池配置
    threads:
      # 接受线程数，处理新连接，默认为-1（CPU核心数）
      acceptors: 1
      # 选择线程数，处理IO事件，默认为-1（CPU核心数的倍数）
      selectors: 4
      # 线程池最大队列容量
      max-queue-capacity: 1000
      # 线程池最大线程数
      max: 1000
      # 线程池最小线程数
      min: 20
      # 线程空闲超时时间（毫秒）
      idle-timeout: 30000
    # HTTP响应头最大大小
    max-http-response-header-size: 1MB
    # HTTP表单POST请求最大大小
    max-http-form-post-size: 1MB
    # 连接空闲超时时间（毫秒）
    connection-idle-timeout: 30000
```

## 监控和管理

### Actuator端点

- **健康检查**: `/actuator/health`
- **应用信息**: `/actuator/info`
- **指标监控**: `/actuator/metrics`
- **环境信息**: `/actuator/env`

## 性能优化建议

### 1. 线程池调优
- 根据实际并发量调整`max-threads`
- 监控线程池使用情况，避免线程阻塞

### 2. 连接器优化
- `acceptors`: 通常设置为CPU核心数的一半
- `selectors`: 通常设置为CPU核心数
- `idle-timeout`: 根据业务需求设置

### 3. 缓冲区配置
- `request-buffer-size`: 根据请求体大小调整
- `response-buffer-size`: 根据响应内容大小调整

### 4. 压缩配置
- 对大文本响应启用压缩
- 设置合理的`min-response-size`避免小文件压缩

## 故障排查

### 常见问题

1. **端口占用**: 检查8080端口是否被占用
2. **内存不足**: 调整JVM参数 `-Xms512m -Xmx2g`
3. **线程池满**: 监控并调整线程池大小
4. **连接超时**: 检查`idle-timeout`设置

### 日志查看

- 应用日志: `logs/jetty-demo.log`
- 控制台日志: 包含Jetty配置和请求日志
- Actuator监控: 通过`/actuator/metrics`查看

## 构建和部署

### Docker部署

```dockerfile
FROM openjdk:17-jdk-slim
COPY target/springboot-jetty-demo.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

### 性能测试

使用JMeter或类似工具进行压力测试：

```bash
# 100个并发用户，持续60秒
jmeter -n -t test.jmx -l results.jtl
```

## 版本信息

- SpringBoot: 3.5.8
- Jetty: 12.0.15
- Java: 17+
- Maven: 3.9+