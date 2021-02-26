# default-core
springboot-web一些默认实现

# 使用说明

## 一、[引入依赖](https://search.maven.org/artifact/com.github.wpyuan/default-core/0.0.1/jar)

以`maven`管理依赖的项目举例
```xml
<dependency>
  <groupId>com.github.wpyuan</groupId>
  <artifactId>default-core</artifactId>
  <version>0.0.1</version>
</dependency>
```

## 二、springboot启动类添加mapper扫描路径

```java
@SpringBootApplication
@MapperScan({"com.github.mybatis.crud.mapper"})
public class TestApplication {
    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class, args);
    }
}
```
注意：`com.github.mybatis.crud.mapper`这是[mybatis-crud插件](https://wpyuan.github.io/mybatis-crud-source/)的默认mapper，需要注册，注册方法很多，自行选择用自己喜欢的方式注册，这里只举个栗子

## 三、controller层继承使用

```java
@RestController
@RequestMapping("/employee")
public class EmployeeController extends DefaultController<Employee>{
}
```
注意：`com.github.defaultcore.controller.DefaultController`

## 四、service层继承使用

如果有接口层，则一样继承使用
```java
public interface EmployeeService extends DefaultService<Employee> {
}
```
和serviceImpl
```java
@Service
public class EmployeeServiceImpl extends DefaultServiceImpl<Employee> implements EmployeeService {
}
```
注意：`com.github.defaultcore.service.DefaultService`和`com.github.defaultcore.service.impl.DefaultServiceImpl`

## 五、mapper或dao层继承使用

```java
public interface EmployeeMapper extends DefaultMapper<Employee>, BatchInsertMapper<Employee> {
}
```
注意：`com.github.mybatis.crud.mapper.DefaultMapper`这个是必须继承的，`com.github.mybatis.crud.mapper.BatchInsertMapper`这是批量插入实现，可选择性继承使用

## 六、测试使用

上面示例一顿操作的结果是，直接完成了crud的后端restful风格的接口，具体如下：
- 新建接口，接收返回数据格式`json`

POST http://ip:port/employee/create
- 新建接口，接收数据格式`multipart/form-data`或`queryString`，返回数据格式`json`

POST http://ip:port/employee/create-form
- 删除接口，接收数据格式`queryString`，返回数据格式`json`

DELETE http://ip:port/employee/remove
- 更新接口，接收数据格式`queryString`，返回数据格式`json`

PUT http://ip:port/employee/update
- 单条记录明细接口，接收数据格式`queryString`，返回数据格式`json`

GET http://ip:port/employee/detail
- 列表查询接口，接收数据格式`queryString`，返回数据格式`json`

GET http://ip:port/employee/list

接下来直接注入上述例子bean使用即可
