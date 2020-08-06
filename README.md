# sue-mall Release(部署版本)
##### 其余分支(master sue-jpa sue-mybatis 都属于开发原型)
测试环境-Linux(CentOS 7)

## 虚拟机固定IP设置
    vim /etc/sysconfig/network-scripts/ifcfg-ens33
    IPADDR=192.168.182.152
    DNS1=192.168.2.1（填写自己的DNS解析）
    GATEWAY=192.168.2.1(填写自己的默认网关)
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
   
## nginx反向代理部署
    安装gcc环境:  yum install gcc-c++
    安装PCRE库,用于解析正则表达式:yum install -y pcre pcre-devel
    安装zlib库和解压缩依赖:yum install -y zlib zlib-devel
    安装SSL安全加密套接字的协议层，用于http安全传输，也就是https: yum install -y openssl openssl-devel
    解压nginx压缩文件:tar -zxvf nginx-1.16.1.tar.gz
    编译:
        首先创建nginx临时目录:mkdir /var/temp/nginx -p
        在nginx目录下编译(直接在命令行输入如下指令目的是创建makefile):
                       ./configure \
                       --prefix=/usr/local/nginx \
                       --conf-path=/usr/local/nginx/nginx.conf \
                       --pid-path=/var/run/nginx/nginx.pid \
                       --lock-path=/var/lock/nginx.lock \
                       --error-log-path=/var/log/nginx/error.log \
                       --http-log-path=/var/log/nginx/access.log \
                       --with-http_gzip_static_module \
                       --http-client-body-temp-path=/var/temp/nginx/client \
                       --http-proxy-temp-path=/var/temp/nginx/proxy \
                       --http-fastcgi-temp-path=/var/temp/nginx/fastcgi \
                       --http-uwsgi-temp-path=/var/temp/nginx/uwsgi \
                       --http-scgi-temp-path=/var/temp/nginx/scgizoo_sample.cfg
        命令解释:
                       –prefix	指定nginx安装目录
                       –pid-path	指向nginx的pid
                       –lock-path	锁定安装文件，防止被恶意篡改或误操作
                       –error-log	错误日志
                       –http-log-path	http日志
                       –with-http_gzip_static_module	启用gzip模块，在线实时压缩输出数据流
                       –http-client-body-temp-path	设定客户端请求的临时目录
                       –http-proxy-temp-path	设定http代理临时目录
                       –http-fastcgi-temp-path	设定fastcgi临时目录
                       –http-uwsgi-temp-path	设定uwsgi临时目录
                       –http-scgi-temp-path	设定scgi临时目录
        输入make （安装make install）
    进入sbin目录启动nginx:./nginx  
        停止:./nginx -s stop
        重新加载:./nginx -s reload
    注意事项:
            如果在云服务器安装，需要开启默认的nginx端口：80
            如果在虚拟机安装，需要关闭防火墙
            本地win或mac需要关闭防火墙