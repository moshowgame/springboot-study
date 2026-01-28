# DataHub API - Multi-Region Data Aggregation

一个用于聚合多国（UK/CN/IN）API数据的DataHub项目。

```groovy
-------------------------------------
- 🚀 Powered by Moshow郑锴
- 🌟 Might the holy code be with you!
-------------------------------------
🔍 公众号 👉 软件开发大百科
💻 CSDN 👉 https://zhengkai.blog.csdn.net
📂 GitHub 👉 https://github.com/moshowgame
```

## 项目特性

1. **多国数据聚合**: 同时调用UK、CN、IN三个地区的API，并将数据聚合
2. **可配置的聚合策略**: 支持多种聚合方法（addUp、overwrite、merge等）
3. **灵活的JSONPath配置**: 可以配置聚合的路径和方式
4. **异步非阻塞**: 基于WebFlux实现高性能的异步调用
5. **配置化管理**: 通过YAML文件管理聚合策略

## 快速开始

### 1. 启动项目

```bash
mvn spring-boot:run
```

### 2. 访问API

#### 健康检查
```
GET http://localhost:8080/health
```

#### 3. 通用聚合接口
```
GET http://localhost:8080/datahub/uk/kpi/totalPosition
```
```json
{
"data": {
  "data": {
    "today": 3357,
    "yesterday": 3325,
    "last30daysAvg": 2721
  }
},
"code": 200,
"msg": "Data aggregated successfully"
}
```

### 4. 测试各地区API（演示数据）

```
GET http://localhost:8080/api/uk/kpi/totalPosition
GET http://localhost:8080/api/cn/kpi/totalPosition
GET http://localhost:8080/api/in/kpi/totalPosition
```

```json
{
"data": {
  "today": 1038,
  "yesterday": 1076,
  "last30daysAvg": 857
},
"code": 200,
"msg": "Success - UK Region"
}
```

## 配置说明

### application.properties

```properties
# 各地区API基础URL
regional.api.uk.base-url=http://localhost:8081/api/uk
regional.api.cn.base-url=http://localhost:8082/api/cn
regional.api.in.base-url=http://localhost:8083/api/in

# 聚合配置
aggregation.config.enabled=true
aggregation.config.default-strategy=addUp
aggregation.config.file-path=classpath:aggregation-config.yml
```

### aggregation-config.yml

```yaml
aggregation:
  strategies:
    - name: uk-kpi-totalPosition
      path: /api/uk/kpi/totalPosition
      regions:
        - uk
        - cn
        - in
      aggregation-rules:
        - path: "data"
          strategy: addUp
          merge-mode: deep
      output-path: /datahub/uk/kpi/totalPosition
```

## 聚合策略

### 1. addUp (累加)
- 数值类型: 相加所有值
- 对象类型: 深度合并并累加相同字段的数值

示例:
```json
// UK: {"data": {"today": 1000}}
// CN: {"data": {"today": 1500}}
// IN: {"data": {"today": 800}}
// 结果: {"data": {"today": 3300}}
```

### 2. overwrite (覆盖)
使用最后一个值覆盖前面的值

### 3. merge (合并)
合并所有Map，相同的键被后面的值覆盖

## 项目结构

```
springboot-datahub/
├── src/main/java/com/datahub/
│   ├── DataHubApplication.java          # 主应用类
│   ├── config/                          # 配置类
│   │   ├── RegionalApiProperties.java  # 区域API配置
│   │   ├── AggregationConfigLoader.java # 聚合配置加载器
│   │   └── WebClientConfig.java         # WebClient配置
│   ├── client/
│   │   └── RegionalApiClient.java      # 区域API客户端
│   ├── aggregation/                     # 聚合相关
│   │   ├── AggregationEngine.java      # 聚合引擎
│   │   ├── AggregationStrategy.java    # 聚合策略接口
│   │   ├── AddUpStrategy.java          # 累加策略实现
│   │   └── AggregationStrategyFactory.java # 策略工厂
│   ├── service/
│   │   └── DataHubService.java         # DataHub服务
│   ├── controller/
│   │   └── DataHubController.java      # DataHub控制器
│   └── demo/
│       └── DemoDataController.java     # 演示数据API
├── src/main/resources/
│   ├── application.properties           # 应用配置
│   └── aggregation-config.yml          # 聚合策略配置
└── pom.xml                              # Maven配置
```

## 扩展新的聚合策略

1. 实现 `AggregationStrategy` 接口
2. 在 `AggregationStrategyFactory` 中注册新策略
3. 在 `aggregation-config.yml` 中配置使用

示例：

```java
public class CustomStrategy implements AggregationStrategy {
    @Override
    public Object aggregate(Object[] values) {
        // 自定义聚合逻辑
        return result;
    }
}
```

## 添加新的API聚合

在 `aggregation-config.yml` 中添加新配置：

```yaml
- name: custom-api
  path: /api/{region}/custom/endpoint
  regions:
    - uk
    - cn
    - in
  aggregation-rules:
    - path: "data"
      strategy: addUp
      merge-mode: deep
  output-path: /datahub/{region}/custom/endpoint
```

## 技术栈

- Spring Boot 3.2.0
- Spring WebFlux (响应式编程)
- Jackson (JSON处理)
- Lombok (简化代码)
- Maven (依赖管理)
