# 4nCoV 后端


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
- 未完成的功能、存在的问题需要用 `//TODO 注明`
- 传入后台的VM(ViewModel)对象遵守[JSR-303](https://www.ibm.com/developerworks/cn/java/j-lo-jsr303/index.html)标准，注解`message`字段必填(全局异常处理器会自动返回错误消息)
