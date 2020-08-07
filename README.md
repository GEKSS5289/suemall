# sue-mall Release(部署版本)
##### 其余分支(master sue-jpa sue-mybatis 都属于开发原型)
测试环境-Linux(CentOS 7)

## 虚拟机固定IP设置
    vim /etc/sysconfig/network-scripts/ifcfg-ens33
    添加:
        BOOTPROTO=static
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
                       --conf-path=/usr/local/nginx/conf/nginx.conf \
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
                       --with-http_ssl_module
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
        输入make
        输入make install
    进入sbin目录启动nginx:./nginx  
        停止:./nginx -s stop
        重新加载:./nginx -s reload
    注意事项:
            如果在云服务器安装，需要开启默认的nginx端口：80
            如果在虚拟机安装，需要关闭防火墙
            本地win或mac需要关闭防火墙
 #####  nginx.conf核心配置:
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
  ##### nginx的常见错误:
           [error] open() "/var/run/nginx/nginx.pid" failed
           解决:mkdir /var/run/nginx
           [error] invalid PID number "" in "/var/run/nginx/nginx.pid"
           解决:./nginx -c /usr/local/nginx/conf/nginx.conf
  #####  nginx手动日志切割:
          创建cut_my_log.sh
            #!/bin/bash
            LOG_PATH="/var/log/nginx/"
            RECORD_TIME=$(date -d "yesterday" +%Y-%m-%d+%H:%M)
            PID=/var/run/nginx/nginx.pid
            mv ${LOG_PATH}/access.log ${LOG_PATH}/access.${RECORD_TIME}.log
            mv ${LOG_PATH}/error.log ${LOG_PATH}/error.${RECORD_TIME}.log
            #向Nginx主进程发送信号，用于重新打开日志文件
            kill -USR1 `cat $PID`
          添加可执行权限
            chmod +x cut_my_log.sh
          测试效果
            ./cut_my_log.sh
#####    nginx日志切割-定时:
        安装定时任务:
            yum install crontabs
        crontab -e 编辑并且添加一行新的任务:
            */1 * * * * /usr/local/nginx/sbin/cut_my_log.sh
        重启定时任务:service crond restart
        常用定时任务列表:
            service crond start         //启动服务
            service crond stop          //关闭服务
            service crond restart       //重启服务
            service crond reload        //重新载入配置
            crontab -e                  // 编辑任务
            crontab -l                  // 查看任务列表
        定时任务表达式：
        Cron表达式是，分为5或6个域，每个域代表一个含义，如下所示：
        分	时	日	月	星期几	年（可选）
        取值范围	0-59	0-23	1-31	1-12	1-7	2019/2020/2021/…
        常用表达式:
        每分钟执行:
        */1 * * * *
        每日凌晨（每天晚上23:59）执行
        59 23 * * *
        每日凌晨1点执行:
        0 1 * * *
 ##### nginx gzip
            开启:gzip on;
            限制最小压缩:gzip_min_length 1;
            压缩级别:gzip_comp_level 3;
            压缩文件类型:
                gzip_typs text/plain application/javascript application/x-javascript text/css
                application/xml text/javascript application/x-httpd-php image/jpeg image/git image/png application/json
 ##### nginx root 和 alias
        假如服务器路径为：/home/imooc/files/img/face.png
        root 路径完全匹配访问
        配置的时候为:
        location /imooc {
            root /home
        }
        用户访问的时候请求为：url:port/imooc/files/img/face.png
        alias 可以为你的路径做一个别名，对用户透明
        配置的时候为:
        location /hello {
            alias /home/imooc
        }
        用户访问的时候请求为：url:port/hello/files/img/face.png，如此相当于为目录imooc做一个自定义的别名。
##### nginx location匹配规则
       location 的匹配规则
       空格：默认匹配，普通匹配
       location / {
            root /home;
       }
       
       =：精确匹配
       location = /imooc/img/face1.png {
           root /home;
       }
       
       ~*：匹配正则表达式，不区分大小写
       #符合图片的显示
       location ~* .(GIF|jpg|png|jpeg) {
           root /home;
       }
       
       ~：匹配正则表达式，区分大小写
       #GIF必须大写才能匹配到
       location ~ .(GIF|jpg|png|jpeg) {
           root /home;
       }
       
       ^~：以某个字符路径开头
       location ^~ /imooc/img {
           root /home;
       }
 ##### nginx跨域配置
        #允许跨域请求的域，*代表所有
        add_header 'Access-Control-Allow-Origin' *;
        #允许带上cookie请求
        add_header 'Access-Control-Allow-Credentials' 'true';
        #允许请求的方法，比如 GET/POST/PUT/DELETE
        add_header 'Access-Control-Allow-Methods' *;
        #允许请求的header
        add_header 'Access-Control-Allow-Headers' *;
 ##### nginx防盗链
        #对源站点验证
        valid_referers *.imooc.com; 
        #非法引入会进入下方判断
        if ($invalid_referer) {
            return 404;
        }
  ##### nginx负载均衡
           配置上有服务器
            upstream tomcats{
                 server 192.168.182.151:8080;
                 server 192.168.182.152:8080;
                 server 192.168.182.153:8080;
            }
            配置反向代理监听端口
            server {
                    listen       80;
                    server_name  shop.z.mukewang.com;
          
                    location / {
                        proxy_pass http://tomcats;
                    }
            }
 ##### nginx指令参数max_conns
       max_conns:限制每台最大连接数
       worker进程设置1个，便于测试观察成功的连接数
       worker_processes  1;
       
       upstream tomcats {
               server 192.168.1.173:8080 max_conns=2;
               server 192.168.1.174:8080 max_conns=2;
               server 192.168.1.175:8080 max_conns=2；
       }
  ##### nginx指令参数weight=1
        weight:每台服务器分配到的权重
      
        upstream tomcats {
                server 192.168.1.173:8080 weight=1;
                server 192.168.1.174:8080 weight=2;
                server 192.168.1.175:8080 weight=3；
        }
  ##### nginx指令参数slow_start
        商业版
            upstream tomcats {
                    server 192.168.1.173:8080 weight=6 slow_start=60s;
            #       server 192.168.1.190:8080;
                    server 192.168.1.174:8080 weight=2;
                    server 192.168.1.175:8080 weight=2;
            }
        注意:
            该参数不能使用在hash和random load balancing中
            如果在upstream中只有一台server，则该参数失败
   ##### nginx指令参数down
         指定某台服务器节点不可用
            upstream tomcats {
                    server 192.168.1.173:8080 down;
            #       server 192.168.1.190:8080;
                    server 192.168.1.174:8080 weight=1;
                    server 192.168.1.175:8080 weight=1;
            }
   ##### nginx指令参数backup
         当服务器节点中的所有机器宕机，启动被backup标记的备用机
               upstream tomcats {
                       server 192.168.1.173:8080 backup;
               #       server 192.168.1.190:8080;
                       server 192.168.1.174:8080 weight=1;
                       server 192.168.1.175:8080 weight=1;
               }
         注意:
             该参数不能使用在hash和random load balancing中
   ##### upstream 指令参数 max_fails、fail_timeout
         max_fails：表示失败几次，则标记server已宕机，剔出上游服务。
         fail_timeout：表示失败的重试时间。
         假设目前设置如下:
           max_fails=2 fail_timeout=15s 
           则代表在15秒内请求某一server失败达到2次后，
           则认为该server已经挂了或者宕机了，随后再过15秒，
           这15秒内不会有新的请求到达刚刚挂掉的节点上，
           而是会请求到正常运作的server，
           15秒后会再有新请求尝试连接挂掉的server，如果还是失败，重复上一过程，直到恢复。
   ##### nginx keepalive
         keepalive:设置长连接处理数量(提供吞吐量)
         proxy_http_version 1.1; 设置长连接版本为1.1
         proxy_set_header Connection ""; 清除connction header信息
   ##### nginx ip_hash
         ip_hash 通过用户访问的本地ip来做负载均衡
         upstream tomcats {
                 ip_hash;
                 
                 server 192.168.1.173:8080;
                 server 192.168.1.174:8080 down;
                 server 192.168.1.175:8080;
         }
   ##### nginx url_hash 与 least_conn
         url_hash 根据每次请求url地址，hash后访问到固定的服务器节点
         least_conn 最少连接数
         upstream tomcats {
             # url hash
             hash $request_uri;
             # 最少连接数
             # least_conn
         
             server 192.168.1.173:8080;
             server 192.168.1.174:8080;
             server 192.168.1.175:8080;
         }
         
         server {
             listen 80;
             server_name www.tomcats.com;
         
             location / {
                 proxy_pass  http://tomcats;
             }
         }  
   ##### nginx缓存
            浏览器缓存：            
            加速用户访问，提升单个用户（浏览器访问者）体验，缓存在本地
            Nginx缓存
            缓存在nginx端，提升所有访问到nginx这一端的用户
            提升访问上游（upstream）服务器的速度
            用户访问仍然会产生请求流量
            控制浏览器缓存：
            location /files {
                alias /home/imooc;
                # expires 10s;
                # expires @22h30m;
                # expires -1h;
                # expires epoch;
                # expires off;
                expires max;
            }
   ##### nginx反向代理缓存
            # proxy_cache_path 设置缓存目录
            #       keys_zone 设置共享内存以及占用空间大小
            #       max_size 设置缓存大小
            #       inactive 超过此时间则被清理
            #       use_temp_path 临时目录，使用后会影响nginx性能
            proxy_cache_path /usr/local/nginx/upstream_cache keys_zone=mycache:5m max_size=1g inactive=1m use_temp_path=off;
            
            location / {
                proxy_pass  http://tomcats;
            
                # 启用缓存，和keys_zone一致
                proxy_cache mycache;
                # 针对200和304状态码缓存时间为8小时
                proxy_cache_valid   200 304 8h;
            }
   ##### nginx配置安全域名连接
           1. 安装SSL模块
           要在nginx中配置https，就必须安装ssl模块，也就是: http_ssl_module。
           
           进入到nginx的解压目录： /home/software/nginx-1.16.1
           
           新增ssl模块(原来的那些模块需要保留)
           
           ./configure \
           --prefix=/usr/local/nginx \
           --pid-path=/var/run/nginx/nginx.pid \
           --lock-path=/var/lock/nginx.lock \
           --error-log-path=/var/log/nginx/error.log \
           --http-log-path=/var/log/nginx/access.log \
           --with-http_gzip_static_module \
           --http-client-body-temp-path=/var/temp/nginx/client \
           --http-proxy-temp-path=/var/temp/nginx/proxy \
           --http-fastcgi-temp-path=/var/temp/nginx/fastcgi \
           --http-uwsgi-temp-path=/var/temp/nginx/uwsgi \
           --http-scgi-temp-path=/var/temp/nginx/scgi  \
           --with-http_ssl_module
           编译和安装
           
           make
           
           make install
           2. 配置HTTPS
           把ssl证书 *.crt 和 私钥 *.key 拷贝到/usr/local/nginx/conf目录中。
           
           新增 server 监听 443 端口：
           
           server {
               listen       443;
               server_name  www.imoocdsp.com;
           
               # 开启ssl
               ssl     on;
               # 配置ssl证书
               ssl_certificate      1_www.imoocdsp.com_bundle.crt;
               # 配置证书秘钥
               ssl_certificate_key  2_www.imoocdsp.com.key;
           
               # ssl会话cache
               ssl_session_cache    shared:SSL:1m;
               # ssl会话超时时间
               ssl_session_timeout  5m;
           
               # 配置加密套件，写法遵循 openssl 标准
               ssl_protocols TLSv1 TLSv1.1 TLSv1.2;
               ssl_ciphers ECDHE-RSA-AES128-GCM-SHA256:HIGH:!aNULL:!MD5:!RC4:!DHE;
               ssl_prefer_server_ciphers on;
               
               location / {
                   proxy_pass http://tomcats/;
                   index  index.html index.htm;
               }
            }
           3. reload nginx
           ./nginx -s reload   
        
