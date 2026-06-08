# 项目名称
AI 驱动校园电商安全与议价系统（暂定名）

# 简介
一个面向校园的二手交易平台，支持商品发布、浏览、AI 审核（待接入）、智能议价（待开发）。

# 技术栈
后端：Spring Boot 3.x / MyBatis-Plus / MySQL
前端：Vue 3 (CDN) + Axios + 原生 CSS
中间件：无（计划引入 Redis、FastAPI/Python）
# 当前完成的功能
用户注册、登录（Session）

商品发布（自动绑定发布者）

商品列表（分页）、商品详情

我的发布

逻辑删除

统一返回格式 R 对象

登录拦截器

# 如何运行项目

后端：导入 IDEA，配置 application.yml 中的数据库连接，运行 CampusTradeApplication

前端：用 Live Server 打开 frontend/index.html（或直接放在 resources/static 下同源访问）

需要先执行 src/main/resources/sql/init.sql 建表并插入测试数据（如果你整理过）

# 项目结构说明

```text
com.suzumiya.campustrade
├── config       # 跨域、分页、拦截器配置
├── controller   # REST 接口
├── entity       # 实体类及 R 包装类
├── handler      # 自动填充处理器
├── interceptor  # 登录拦截器
├── mapper       # MyBatis-Plus Mapper
└── service      # 业务层
```

# 待办事项 (TODO)

可以把你路线文档中 7 月、8 月要做的内容简化写几行（AI 审核、智能议价、JWT、部署上线等）。

演示视频 & 部署链接（等到 8 月底上线后再补）

暂时空着或写“待更新”。

# 作者 & 致谢

你自己的信息，以及感谢 AI 工具协助（DeepSeek / Kimi）等等。