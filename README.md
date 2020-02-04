# 4nCoV 后端

4nCoV平台的后端服务.

## 环境

### 运行环境
- Java 8
- Spring Boot 2.1.0.RELEASE
- MyBatis 3.1.0
- Flyway 用于数据库DDL(Data Definition Language)脚本版本控制
- MySQL 5.7
- Docker


### 测试框架
- JUnit 5 jupiter
- Mockito 3


## 本地构建与测试
- `mvn clean test # 跑测试`
- `mvn clean package # 构建,构建完成的包位于 ./target 目录下`


## 部署与启动

### Plain JRE
```
java -jar -Dspring.profiles.active=${ACTIVE_PROFILE} \
    -Dspring.datasource.username=${DB_USERNAME} \
    -Dspring.datasource.password=${DB_PASSWORD} \
    -Dspring.datasource.url=${DB_URL} \
    -Dsecurity.jwtSecret=${JWT_SECRET} \
    -Doss.endpoint=${OSS_ENDPOINT} \
    -Doss.region=${OSS_REGION} \
    -Doss.bucketName=${OSS_BUCKET_NAME} \
    -Doss.accessKey=${OSS_ACCESS_KEY} \
    -Doss.secretKey=${OSS_SECRET_KEY} \
    target/4ncov-0.0.1-SNAPSHOT.jar 
# 通过传入ACTIVE_PROFILE的值确定当前环境配置,可选项:dev,prod
```

### Docker
- 打包docker镜像: `docker build -t 4ncov/service . -f Dockerfile`
- 部署docker容器: 
```
docker run -dp 8000:8000 --name 4ncov-service \
    -e SPRING_PROFILES_ACTIVE=${SPRING_PROFILES_ACTIVE} \ # dev,prod
    -e DB_USERNAME=${DB_USERNAME} \ # database username
    -e DB_PASSWORD=${DB_PASSWORD} \ # database password
    -e DB_HOST=${DB_HOST} \ # database host
    -e DB_PORT=${DB_PORT} \ # database port
    -e JWT_SECRET=${JWT_SECRET} \ # jwt secret
    -e OSS_ENDPOINT=${OSS_ENDPOINT} \ # oss endpoint
    -e OSS_REGION=${OSS_REGION} \ # oss region
    -e OSS_BUCKET_NAME=${OSS_BUCKET_NAME} \ # oss bucket name
    -e OSS_ACCESS_KEY=${OSS_ACCESS_KEY} \ # oss access key
    -e OSS_SECRET_KEY=${OSS_SECRET_KEY} \ # oss secret key
    4ncov/service
```


## 数据库版本控制
使用`**flyway**`进行数据库脚本的版本控制,服务启动时会自动进行数据库脚本的migration.  
所有需要在数据库执行的脚本请放置于: `src/main/resources/db/migration`,命名方式为:`Vyyyy_MM_dd_HH_mm__Name_Of_Script.sql`,如`V2020_01_31_20_01__Create_4ncov_Tables.sql`.  
通过运行`FlywayApplication`可以单独将数据库脚本migrate到所连接的数据库当中.

## 项目进度与版本控制
- 看板: [GitHub Projects](https://github.com/4ncov/service-4ncov/projects/1)
- 开始开发前请自觉将看板的issue分配给自己并从`TODO`挪入`IN PROGRESS`,开发并验证完成之后挪入`DONE`
- 创建短时的分支来进行开发
- 分支命名
  - 故事 `story-${issueNo}-${description}`, 如 `story-8-requester-signup-endpoint`
  - 任务 `task-${issueNo}-${description}`, 如`task-15-api-documentation`
- 合并回master分支之前,需要提PR(Pull Request)申请将分支合并回master,并分配PR给**除自己之外**的collaborator进行code review
- Code review可直接在PR当中进行


## 编码规约
- 严格遵照plugin: `Alibaba Java Coding Guidelines`.
- 4空格缩进、禁止使用制表符`Tab`
- 单行代码禁止超过121个字符、过长需要按照实际情况换行（以`.` `,`等为换行分隔符）。
- 方法长度禁止超过80行，长方法应适当的通过方法调用减少单个方法长度，并在调用方法上给出明确的方法文档。
- 严格的间隔规定： 例如
```java
class Example {
    public static void main(String[] args) {
        for (int i = 0; i < n; i++) {
            // 规范代码
        }
    }
}
```
```java
class Example {
    public static void main(String[] args){
        for(int i=0;i<n;i++){
            // 错误的间隔示例 可使用IDE中CODE FORMAT功能进行修复
        }
    }
}
```
- 代码文件格式为`UTF-8`
- 类的成员变量永远在类的顶部区域，类的重载和被重载的方法应在一起，类的重写方法应该总是在一起，类的私有方法总在类的最下部。
顺序如下：
```java
class DemoService {
    // （final）常量

    // （field）成员变量

    //  构造方法

    // （public）对外提供的service方法，以及重载方法必须和被重载方法在一起

    // （override）重写方法

    // （private）对内提供的封装方法 或 getter setter方法
}
```
- 空语句块也要换行，且需要注释空异常捕获块理由，例如：
```java
try {
    // ...省略代码
    // 正确格式
} catch(Exception e) {
    // ignore
}
try {
    // ...省略代码
    // 错误格式
} catch(Exception e) {}
```
- 禁止单行声明多个变量
- Java变量命名遵从驼峰命名法，SQL命名以下划线分隔
- 包名全部小写、类名首字母大写、常量名全大写下划线分隔
- 重写方法总是使用`@Override`注解
- 静态成员变量、方法要通过类名访问而不是实例对象
- 工具静态方法以及接口需要有Javadoc方法注释


## 开发原则
- 杜绝过深的递归方法，防止栈溢出
- 能在用一遍循环解决的问题，谢绝在两个循环，所有代码需考虑性能问题
- 不用的大对象及时释放，并注明 `//Help GC`
- 尽量使用Stream流提升代码可读性
- 禁止过度抽象而影响代码可读性
- 代码提交必须经过测试
- 未完成的功能、存在的问题需要用 `//TODO` 注明
- 传入后台的VM(ViewModel)对象遵守[JSR-303](https://www.ibm.com/developerworks/cn/java/j-lo-jsr303/index.html)标准，注解`message`字段必填(全局异常处理器会自动返回错误消息)
- 除非有必要,否则所有的数据库ID字段使用MySQL `BIGINT`,java代码当中使用`Long`.
- 本地开发应使用适当的本地配置如`application-local.yml`,并使用`local`作为active profile. 可复制`src/main/resources/config/application-local.yml.template`创建一个本地开发配置
- 单元测试如有必要请使用mockito对依赖进行mock
- **切勿在测试代码当中连接dev/prod数据库**
- **切勿将任何线上环境的数据库用户名密码或其它敏感信息提交到本项目**


## 接口设计原则

- 尽量遵循**RESTful**风格
