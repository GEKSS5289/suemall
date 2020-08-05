# sue-mall Release(部署版本)
##### 其余分支(master sue-jpa sue-mybatis 都属于开发原型)
测试环境-Linux(CentOS 7)

## sue-mall打包
    将sue-api服务模块的pom文件里增加<packaging>war</packageing>
    （注意:jar包是服务化概念 war包是应用程序概念）
    去除spring-boot-starter-web中的org-springframework-starter-tomcat(为springboot自带tomcat组件)
    添加javax.servlet依赖:
    <dependency>
             <groupId>javax.servlet</groupId>
             <artifactId>javax.servlet-api</artifactId>
             <scope>provided</scope>
    </dependency>
## 安装jdk1.8
    去甲骨文官网寻找jdk1.8
    tar -zxvf /home/software/jdk-8u191-linux-x64.tar.gz  解压
    export JAVA_HOME=/usr/java/jdk1.8.0_191
    export CLASSPATH=.:%JAVA_HOME%/lib/dt.jar:%JAVA_HOME%/lib/tools.jar  
    export PATH=$PATH:$JAVA_HOME/bin
## 安装tomcat9.0.33
    去www.apache.org官网下载tomcat9.0.33
    tar -zxvf /home/software/apache-tomcat-9.0.33.tar.gz 解压
    cd apache-tomcat-9.0.33/bin
    ./startup.sh 启动
## 安装mysql8.0
    检查:yum -y remove MySQL-*
    如果系统中存在:find / -name mysql
    删除配置文件:rm -rf /etc/my.cnf
    删除mysql默认密码:rm -rf /root/.mysql_sercret
    安装源:rpm -Uvh https://dev.mysql.com/get/mysql80-community-release-el7-3.noarch.rpm
    安装mysql8.0:yum --enablerepo=mysql80-community install mysql-community-server
    启动服务:service mysqld start
    查看状态:service mysqld status
    查看临时密码:grep "A temporary password" /var/log/mysqld.log
    更改临时密码:ALTER USER 'root'@'localhost' IDENTIFIED BY 'new password';
    配置远程访问:GRANT ALL ON *.* TO 'root'@'%';
## 系统多配置文件
    application.yml 主配置文件
    application-dev.yml 开发环境配置文件
    application-prod.yml 生产环境配置文件 
## 部署sue-mall
    两台tomcat服务器在同一台计算机节点上
    第一台:tomcat-frontend  (在webapps中存放了前端两个项目)
        foodie-shop 门户网站
        foodie-center 用户中心
    第二台:tomcat-api (在webapps中存放了后端服务程序)
        foodie-dev-api 给前端两个项目提供服务
    cookie问题:部署后发现无法保存cookie 问题是由tomcat-api下的 webapps下的foodie-dev-api服务造成的
        原因:采用了tomcat9.0.33默认的cookie处理器所以会造成此类问题
        解决办法: cd /conf/context.xml 添加    <CookieProcessor classNam="org.apache.tomcat.util.http.LegacyCookieProcessor"/>将cookie处理器替换为曾经老版本的
   
