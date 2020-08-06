# sue-mall Release(部署版本)
##### 其余分支(master sue-jpa sue-mybatis 都属于开发原型)
测试环境-Linux(CentOS 7)

## 虚拟机固定IP设置
    vim /etc/sysconfig/network-scripts/ifcfg-ens33
    添加:
        IPADDR=192.168.182.152
        DNS1=192.168.2.1（填写自己的DNS解析）
        GATEWAY=192.168.2.1(填写自己的默认网关)
## 虚拟机开放端口
    开启放火墙:systemctl start firewalld 
    开放端口号:firewall-cmd --zone=public --add-port=需要开放的端口号/tcp --permanent
    关闭端口号:firewall-cmd --zone=public --remove-port=需要关闭的端口号/tcp --permanent
    查看所有开放端口号:firewall-cmd --zone=public --list-ports
    重新加载配置文件:firewall-cmd --reload
## sue-mall打包(war)
    将sue-api服务模块的pom文件里增加<packaging>war</packageing>
    （注意:jar包是服务化概念 war包是应用程序概念）
    去除spring-boot-starter-web中的org-springframework-starter-tomcat(为springboot自带tomcat组件)
        <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-web</artifactId>
                <exclusions>
                    <exclusion>
                        <groupId>org.sprinframework.boot</groupId>
                        <artifactId>org-springframework-starter-tomcat</artifactId>
                    </exclusion>
                </exclusions>
        </dependency>
    添加javax.servlet依赖:
        <dependency>
                 <groupId>javax.servlet</groupId>
                 <artifactId>javax.servlet-api</artifactId>
                 <scope>provided</scope>
        </dependency>
    添加war包application类
        public class WarStarterApplication extends SpringBootServletInitializer {
            @Override
            protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
                //指向Application启动类
                return builder.sources(Application.class);
            }
        }

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
    nginx.conf核心配置:
            设置worker进程的用户，指的linux中的用户，会涉及到nginx操作目录或文件的一些权限，默认为nobody
            user root
        worker进程工作数设置，一般来说CPU有几个，就设置几个，或者设置为N-1也行
            worker_processes 1;
        nginx 日志级别debug | info | notice | warn | error | crit | alert | emerg，错误级别从左到右越来越大
        设置nginx进程 pid
            pid        logs/nginx.pid;
        设置工作模式
            events {
                # 默认使用epoll
                use epoll;
                # 每个worker允许连接的客户端最大连接数
                worker_connections  10240;
            }
        http 是指令块，针对http网络传输的一些指令配置
            http{
            }
        include 引入外部配置，提高可读性，避免单个配置文件过大
            include       mime.types;
        设定日志格式，main为定义的格式名称，如此 access_log 就可以直接使用这个变量了
            参数名	参数意义
            $remote_addr	客户端ip
            $remote_user	远程客户端用户名，一般为：’-’
            $time_local	时间和时区
            $request	请求的url以及method
            $status	响应状态码
            $body_bytes_send	响应客户端内容字节数
            $http_referer	记录用户从哪个链接跳转过来的
            $http_user_agent	用户所使用的代理，一般来时都是浏览器
            $http_x_forwarded_for	通过代理服务器来记录客户端的ip
            sendfile使用高效文件传输，提升传输性能。启用后才能使用tcp_nopush，是指当数据表累积一定大小后才发送，提高了效率。
        sendfile使用高效文件传输，提升传输性能。启用后才能使用tcp_nopush，是指当数据表累积一定大小后才发送，提高了效率。
            sendfile        on;
            tcp_nopush      on;
        keepalive_timeout设置客户端与服务端请求的超时时间，保证客户端多次请求的时候不会重复建立新的连接，节约资源损耗。
            #keepalive_timeout  0;
            keepalive_timeout  65;
        gzip启用压缩，html/js/css压缩后传输会更快
            gzip on;
        server可以在http指令块中设置多个虚拟主机
            listen 监听端口
            server_name localhost、ip、域名
            location 请求路由映射，匹配拦截
            root 请求位置
            index 首页设置
                server {
                        listen       88;
                        server_name  localhost;
                
                        location / {
                            root   html;
                            index  index.html index.htm;
                        }
                }
        查看用户请求:
            cd /var/log/nginx
            vim access.log
    nginx的常见错误:
        [error] open() "/var/run/nginx/nginx.pid" failed
        解决:mkdir /var/run/nginx
        [error] invalid PID number "" in "/var/run/nginx/nginx.pid"
        解决:./nginx -c /usr/local/nginx/conf/nginx.conf