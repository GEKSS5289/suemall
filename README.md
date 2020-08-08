# sue-mall Release(部署版本)
测试环境-Linux(CentOS 7)

# 虚拟机固定IP设置
    vim /etc/sysconfig/network-scripts/ifcfg-ens33
    添加:
        BOOTPROTO=static
        IPADDR=192.168.182.152
        DNS1=192.168.2.1（填写自己的DNS解析）
        GATEWAY=192.168.2.1(填写自己的默认网关)
# 虚拟机开放端口
    开启放火墙:systemctl start firewalld 
    开放端口号:firewall-cmd --zone=public --add-port=需要开放的端口号/tcp --permanent
    关闭端口号:firewall-cmd --zone=public --remove-port=需要关闭的端口号/tcp --permanent
    查看所有开放端口号:firewall-cmd --zone=public --list-ports
    重新加载配置文件:firewall-cmd --reload
# sue-mall打包(war)
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
# 部署sue-mall
    两台tomcat服务器在同一台计算机节点上
    第一台:tomcat-frontend  (在webapps中存放了前端两个项目)
        foodie-shop 门户网站
        foodie-center 用户中心
    第二台:tomcat-api (在webapps中存放了后端服务程序)
        foodie-dev-api 给前端两个项目提供服务
    cookie问题:部署后发现无法保存cookie 问题是由tomcat-api下的 webapps下的foodie-dev-api服务造成的
        原因:采用了tomcat9.0.33默认的cookie处理器所以会造成此类问题
        解决办法: cd /conf/context.xml 添加    <CookieProcessor classNam="org.apache.tomcat.util.http.LegacyCookieProcessor"/>将cookie处理器替换为曾经老版本的

# 安装jdk1.8
    去甲骨文官网寻找jdk1.8
    tar -zxvf /home/software/jdk-8u191-linux-x64.tar.gz  解压
    export JAVA_HOME=/usr/java/jdk1.8.0_191
    export CLASSPATH=.:%JAVA_HOME%/lib/dt.jar:%JAVA_HOME%/lib/tools.jar  
    export PATH=$PATH:$JAVA_HOME/bin
# 安装tomcat9.0.33
    去www.apache.org官网下载tomcat9.0.33
    tar -zxvf /home/software/apache-tomcat-9.0.33.tar.gz 解压
    cd apache-tomcat-9.0.33/bin
    ./startup.sh 启动
# 安装mysql8.0
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
# 系统多配置文件
    application.yml 主配置文件
    application-dev.yml 开发环境配置文件
    application-prod.yml 生产环境配置文件
# nginx反向代理部署
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
 #  nginx.conf核心配置:
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
  #  nginx的常见错误:
           [error] open() "/var/run/nginx/nginx.pid" failed
           解决:mkdir /var/run/nginx
           [error] invalid PID number "" in "/var/run/nginx/nginx.pid"
           解决:./nginx -c /usr/local/nginx/conf/nginx.conf
  #  nginx手动日志切割:
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
#    nginx日志切割-定时:
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
 # nginx gzip
            开启:gzip on;
            限制最小压缩:gzip_min_length 1;
            压缩级别:gzip_comp_level 3;
            压缩文件类型:
                gzip_typs text/plain application/javascript application/x-javascript text/css
                application/xml text/javascript application/x-httpd-php image/jpeg image/git image/png application/json
 # nginx root 和 alias
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
# nginx location匹配规则
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
 # nginx跨域配置
        #允许跨域请求的域，*代表所有
        add_header 'Access-Control-Allow-Origin' *;
        #允许带上cookie请求
        add_header 'Access-Control-Allow-Credentials' 'true';
        #允许请求的方法，比如 GET/POST/PUT/DELETE
        add_header 'Access-Control-Allow-Methods' *;
        #允许请求的header
        add_header 'Access-Control-Allow-Headers' *;
 # nginx防盗链
        #对源站点验证
        valid_referers *.imooc.com; 
        #非法引入会进入下方判断
        if ($invalid_referer) {
            return 404;
        }
  # nginx负载均衡
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
 # nginx指令参数max_conns
       max_conns:限制每台最大连接数
       worker进程设置1个，便于测试观察成功的连接数
       worker_processes  1;
       
       upstream tomcats {
               server 192.168.1.173:8080 max_conns=2;
               server 192.168.1.174:8080 max_conns=2;
               server 192.168.1.175:8080 max_conns=2；
       }
  # nginx指令参数weight=1
        weight:每台服务器分配到的权重
      
        upstream tomcats {
                server 192.168.1.173:8080 weight=1;
                server 192.168.1.174:8080 weight=2;
                server 192.168.1.175:8080 weight=3；
        }
  # nginx指令参数slow_start
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
   # nginx指令参数down
         指定某台服务器节点不可用
            upstream tomcats {
                    server 192.168.1.173:8080 down;
            #       server 192.168.1.190:8080;
                    server 192.168.1.174:8080 weight=1;
                    server 192.168.1.175:8080 weight=1;
            }
   # nginx指令参数backup
         当服务器节点中的所有机器宕机，启动被backup标记的备用机
               upstream tomcats {
                       server 192.168.1.173:8080 backup;
               #       server 192.168.1.190:8080;
                       server 192.168.1.174:8080 weight=1;
                       server 192.168.1.175:8080 weight=1;
               }
         注意:
             该参数不能使用在hash和random load balancing中
   # upstream 指令参数 max_fails、fail_timeout
         max_fails：表示失败几次，则标记server已宕机，剔出上游服务。
         fail_timeout：表示失败的重试时间。
         假设目前设置如下:
           max_fails=2 fail_timeout=15s 
           则代表在15秒内请求某一server失败达到2次后，
           则认为该server已经挂了或者宕机了，随后再过15秒，
           这15秒内不会有新的请求到达刚刚挂掉的节点上，
           而是会请求到正常运作的server，
           15秒后会再有新请求尝试连接挂掉的server，如果还是失败，重复上一过程，直到恢复。
   # nginx keepalive
         keepalive:设置长连接处理数量(提供吞吐量)
         proxy_http_version 1.1; 设置长连接版本为1.1
         proxy_set_header Connection ""; 清除connction header信息
   # nginx ip_hash
         ip_hash 通过用户访问的本地ip来做负载均衡
         upstream tomcats {
                 ip_hash;
                 
                 server 192.168.1.173:8080;
                 server 192.168.1.174:8080 down;
                 server 192.168.1.175:8080;
         }
   # nginx url_hash 与 least_conn
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
   # nginx缓存
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
   # nginx反向代理缓存
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
   # nginx配置安全域名连接
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
   # nginx最终部署配置(nginx.conf)
         
            user root;
            worker_processes 2;
            
            #error_log  logs/error.log;
            #error_log  logs/error.log  notice;
            #error_log  logs/error.log  info;
            
            #pid        logs/nginx.pid;
            
            
            events {
                #use epoll liunx默认使用
                #每个worker进程客户端最大连接数
                worker_connections  10240;
            }
            
            
            http {
                include       mime.types;
                default_type  application/octet-stream;
            
            
            
                sendfile        on;
            
                keepalive_timeout  65;
            
               upstream api.z.mukewang.com{
                #server 192.168.182.151:8080;
                #server 192.168.182.152:8080;
                #server 192.168.182.153:8080;	
                server 192.168.182.150:8088;	
               }
                
              
               server{
                listen 80;
                server_name api.z.mukewang.com;
                
                location / {
                        proxy_pass http://api.z.mukewang.com;
                }
            
               }
                server {
                    listen       80;
                    server_name  shop.z.mukewang.com;
            
                    add_header 'Access-Control-Allow-Origin' *;
                    add_header 'Access-Control-Allow-Credentials' 'true';
                    add_header 'Access-Control-Allow-Methods' *;
                    add_header 'Access-Control-Allow-Headers' *;
            
                    location / {
                        root /home/webapps/foodie-shop;
                        index index.html;
                    }
                
                    error_page   500 502 503 504  /50x.html;
                    location = /50x.html {
                        root   html;
                    }
            
                }
                server {
                    listen       80;
                    server_name  center.z.mukewang.com;
            
                    add_header 'Access-Control-Allow-Origin' *;
                    add_header 'Access-Control-Allow-Credentials' 'true';
                    add_header 'Access-Control-Allow-Methods' *;
                    add_header 'Access-Control-Allow-Headers' *;
            
                    location / {
                        root /home/webapps/foodie-center;
                        index index.html;
                    }
            
                    error_page   500 502 503 504  /50x.html;
                    location = /50x.html {
                        root   html;
                    }
            
                }
            
            }
# keepalived 2.0.20安装
    解压:tar -zxvf keepalived-2.0.20.tar.gz
    到keepalived安装目录下输入:./configure --prefix=/usr/local/keepalived --sysconf=/etc
        prefix：keepalived安装的位置
        sysconf：keepalived核心配置文件所在位置，固定位置，改成其他位置则keepalived启动不了，/var/log/messages中会报错
    配置过程中可能会出现警告信息，如下所示:
        *** WARNING - this build will not support IPVS with IPv6. Please install libnl/libnl-3 dev libraries to support 
        则:yum -y install libnl libnl-devel  
        (重新./configure --prefix=/usr/local/keepalived --sysconf=/etc)
    安装
       make && makeinstall
# keepalived配置文件(master) 双机主备模式
    ! Configuration File for keepalived
    
    global_defs {
       #路由id：当前安装keepalived的节点主机标识符，保证全局唯一
       router_id keep_171
    }
    
    vrrp_instance VI_1 {
        state MASTER
        interface ens33
        virtual_router_id 51
        priority 100
        advert_int 1
        authentication {
            auth_type PASS
            auth_pass 1111
        }
        virtual_ipaddress {
            192.168.182.161
        }
    }
# keepalived配置文件(backup) 双机主备模式
    ! Configuration File for keepalived
    
    global_defs {
       #路由id：当前安装keepalived的节点主机标识符，保证全局唯一
       router_id keep_172
    }
    
    vrrp_instance VI_1 {
        state BACKUP
        interface ens33
        virtual_router_id 51
        priority 80
        advert_int 1
        authentication {
            auth_type PASS
            auth_pass 1111
        }
        virtual_ipaddress {
            192.168.182.161
        }
    }
# keepalived 配置nginx自动重启
    Keepalived配置Nginx自动重启
    1. 增加Nginx重启检测脚本
    vim /etc/keepalived/check_nginx_alive_or_not.sh
    #!/bin/bash
    
    A=`ps -C nginx --no-header |wc -l`
    # 判断nginx是否宕机，如果宕机了，尝试重启
    if [ $A -eq 0 ];then
        /usr/local/nginx/sbin/nginx
        # 等待一小会再次检查nginx，如果没有启动成功，则停止keepalived，使其启动备用机
        sleep 3
        if [ `ps -C nginx --no-header |wc -l` -eq 0 ];then
            killall keepalived
        fi
    fi
    增加运行权限
    
    chmod +x /etc/keepalived/check_nginx_alive_or_not.sh
    
    2. 配置keepalived监听nginx脚本
    vrrp_script check_nginx_alive {
        script "/etc/keepalived/check_nginx_alive_or_not.sh"
        interval 2 # 每隔两秒运行上一行脚本
        weight 10 # 如果脚本运行成功，则升级权重+10
        # weight -10 # 如果脚本运行失败，则升级权重-10
    }
    3. 在vrrp_instance中新增监控的脚本
    track_script {
        check_nginx_alive   # 追踪 nginx 脚本
    }
    4. 重启Keepalived使得配置文件生效
    systemctl restart keepalived
# keepalived 双机热备模式
    主节点配置文件(/etc/keepalived/keepalived.conf):
            global_defs {
               router_id keep_171
            }
            
            vrrp_instance VI_1 {
                state MASTER
                interface ens33
                virtual_router_id 51
                priority 100
                advert_int 1
                authentication {
                    auth_type PASS
                    auth_pass 1111
                }
                virtual_ipaddress {
                    192.168.182.161
                }
            }
            
            vrrp_instance VI_2 {
                state BACKUP
                interface ens33
                virtual_router_id 52
                priority 80
                advert_int 1
                authentication {
                    auth_type PASS
                    auth_pass 1111
                }
                virtual_ipaddress {
                    192.168.182.162
                }
            }
     备节点配置文件(/etc/keepalived/keepalived.conf):
                 global_defs {
                    router_id keep_172
                 }
                 
                 vrrp_instance VI_1 {
                     state BACKUP
                     interface ens33
                     virtual_router_id 51
                     priority 80
                     advert_int 1
                     authentication {
                         auth_type PASS
                         auth_pass 1111
                     }
                     virtual_ipaddress {
                         192.168.182.161
                     }
                 }
                 
                 vrrp_instance VI_2 {
                     state MASTER
                     interface ens33
                     virtual_router_id 52
                     priority 100
                     advert_int 1
                     authentication {
                         auth_type PASS
                         auth_pass 1111
                     }
                     virtual_ipaddress {
                         192.168.182.162
                     }
                 }
  # LVS-DR模式 配置LVS节点与ipvsadm
                前期准备
                服务器与ip规划
                    LVS - 1台
                    VIP（虚拟IP）：192.168.1.150
                    DIP（转发者IP/内网IP）：192.168.1.151
                    Nginx - 2台（RealServer）
                    RIP（真实IP/内网IP）：192.168.1.171
                    RIP（真实IP/内网IP）：192.168.1.172
                所有计算机节点关闭网络配置管理器，因为有可能会和网络接口冲突：
                        systemctl stop NetworkManager 
                        systemctl disable NetworkManager
                创建子接口
                进入到网卡配置目录，找到咱们的ens33 复制 ens33:1
                        cd /etc/sysconfig/network-scripts
                        cp ifcfg-ens33 ifcfg-ens33:1
                        vim ifcfg-ens33:1
                            修改内容如下:
                                   DEVICE="ens33:1"
                                   ONBOOT="yes"
                                   IPADDR=192.168.182.140 (虚拟IP)
                                   NETMASK=255.255.255.0
                                   BOOTPROTO=static
                                   注：配置中的 192.168.1.150 就是咱们的vip，是提供给外网用户访问的ip地址，道理和nginx+keepalived那时讲的vip是一样的。
                        重启网络服务
                              service network restart
                 安装ipvsadm: yum install -y ipvsadm
                 检查:ipvsadm -Ln
                    LVS常用指令:
                           ipvsadm的用法和格式如下：
                           ipvsadm -A|E -t|u|f virutal-service-address:port [-s scheduler] [-p [timeout]] [-M netmask]
                           ipvsadm -D -t|u|f virtual-service-address
                           ipvsadm -C
                           ipvsadm -R
                           ipvsadm -S [-n]
                           ipvsadm -a|e -t|u|f service-address:port -r real-server-address:port [-g|i|m] [-w weight]
                           ipvsadm -d -t|u|f service-address -r server-address
                           ipvsadm -L|l [options]
                           ipvsadm -Z [-t|u|f service-address]
                           ipvsadm --set tcp tcpfin udp
                           ipvsadm --start-daemon state [--mcast-interface interface]
                           ipvsadm --stop-daemon
                           ipvsadm -h
                           
                           命令选项解释：
                           有两种命令选项格式，长的和短的，具有相同的意思。在实际使用时，两种都可以。
                           
                           -A --add-service 在内核的虚拟服务器表中添加一条新的虚拟服务器记录。也就是增加一台新的虚拟服务器。
                           
                           -E --edit-service 编辑内核虚拟服务器表中的一条虚拟服务器记录。
                           
                           -D --delete-service 删除内核虚拟服务器表中的一条虚拟服务器记录。
                           
                           -C --clear 清除内核虚拟服务器表中的所有记录。
                           
                           -R --restore 恢复虚拟服务器规则
                           
                           -S --save 保存虚拟服务器规则，输出为-R选项可读的格式
                           
                           -a --add-server 在内核虚拟服务器表的一条记录里添加一条新的真实服务器记录。也就是在一个虚拟服务器中增加一台新的真实服务器
                           
                           -e --edit-server 编辑一条虚拟服务器记录中的某条真实服务器记录
                           
                           -d --delete-server 删除一条虚拟服务器记录中的某条真实服务器记录
                           
                           -L|-l --list 显示内核虚拟服务器表
                           
                           -Z --zero 虚拟服务表计数器清零（清空当前的连接数量等）
                           
                           --set tcp tcpfin udp 设置连接超时值
                           
                           --start-daemon 启动同步守护进程。他后面可以是master或backup，用来说明LVS Router是master或是backup。在这个功能上也可以采用keepalived的VRRP功能。
                           
                           --stop-daemon 停止同步守护进程
                           
                           -h --help 显示帮助信息
                           
                           其他的选项:
                           -t --tcp-service service-address 说明虚拟服务器提供的是tcp的服务[vip:port] or [real-server-ip:port]
                           
                           -u --udp-service service-address 说明虚拟服务器提供的是udp的服务[vip:port] or [real-server-ip:port]
                           
                           -f --fwmark-service fwmark 说明是经过iptables标记过的服务类型。
                           
                           -s --scheduler scheduler 使用的调度算法，有这样几个选项rr|wrr|lc|wlc|lblc|lblcr|dh|sh|sed|nq,
                           默认的调度算法是： wlc.
                           
                           -p --persistent [timeout] 持久稳固的服务。这个选项的意思是来自同一个客户的多次请求，将被同一台真实的服务器处理。timeout的默认值为300秒。
                           
                           -M --netmask netmask persistent granularity mask
                           
                           -r --real-server server-address 真实的服务器[Real-Server:port]
                           
                           -g --gatewaying 指定LVS的工作模式为直接路由模式（也是LVS默认的模式）
                           
                           -i --ipip 指定LVS的工作模式为隧道模式
                           
                           -m --masquerading 指定LVS的工作模式为NAT模式
                           
                           -w --weight weight 真实服务器的权值
                           
                           --mcast-interface interface 指定组%B
   # 搭建LVS-DR模式 为两台RS配置虚拟IP
            配置虚拟网络子接口(回环接口)
                     cd /etc/sysconfig/network-scripts
                     cp ifcfg-lo ifcfg-lo:1
                     vim ifcfg-lo:1
                        修改内容如下:
                            DEVICE=lo:1
                            IPADDR=192.168.182.140 (虚拟IP)
                            NEWMASK=255.255.255.255(倒数第二个)
                            (文件中其他内容不要修改)
    
   # 搭建LVS-DR模式 为两台RS配置arp
            ARP响应级别与通告行为 的概念
                arp-ignore：ARP响应级别（处理请求）
                0：只要本机配置了ip，就能响应请求
                1：请求的目标地址到达对应的网络接口，才会响应请求
                arp-announce：ARP通告行为（返回响应）
                0：本机上任何网络接口都向外通告，所有的网卡都能接受到通告
                1：尽可能避免本网卡与不匹配的目标进行通告
                2：只在本网卡通告
            配置ARP
                打开sysctl.conf
                    vim /etc/sysctl.conf
                配置所有网卡、默认网卡以及虚拟网卡的arp响应级别和通告行为，分别对应：all，default，lo:
                    # configration for lvs
                    net.ipv4.conf.all.arp_ignore = 1
                    net.ipv4.conf.default.arp_ignore = 1
                    net.ipv4.conf.lo.arp_ignore = 1
                    net.ipv4.conf.all.arp_announce = 2
                    net.ipv4.conf.default.arp_announce = 2
                    net.ipv4.conf.lo.arp_announce = 2
                刷新配置文件：
                    sysctl -p
                增加一个网关，用于接收数据报文，当有请求到本机后，会交给lo去处理：
                    route add -host 192.168.182.140 dev lo:1
                防止重启失效，做如下处理，用于开机自启动：
                    echo "route add -host 192.168.1.150 dev lo:1" >> /etc/rc.local                
 # 搭建LVS-DR模式 使用ipvsadm配置集群规则
            创建LVS节点，用户访问的集群调度者
                ipvsadm -A -t 192.168.182.140:80 -s rr -p 5
                -A：添加集群
                -t：tcp协议
                ip地址：设定集群的访问ip，也就是LVS的虚拟ip
                -s：设置负载均衡的算法，rr表示轮询
                -p：设置连接持久化的时间
            创建2台RS真实服务器
            ipvsadm -a -t 192.168.182.140:80 -r 192.168.1.171:80 -g
            ipvsadm -a -t 192.168.182.140:80 -r 192.168.1.172:80 -g
                -a：添加真实服务器
                -t：tcp协议
                -r：真实服务器的ip地址
                -g：设定DR模式
            保存到规则库，否则重启失效
                ipvsadm -S
            检查集群
                查看集群列表
                    ipvsadm -Ln
                查看集群状态
                    ipvsadm -Ln --stats
            其他命令
                # 重启ipvsadm，重启后需要重新配置
                service ipvsadm restart
                # 查看持久化连接
                ipvsadm -Ln --persistent-conn
                # 查看连接请求过期时间以及请求源ip和目标ip
                ipvsadm -Lnc
                # 设置tcp tcpfin udp 的过期时间（一般保持默认）
                ipvsadm --set 1 1 1
                # 查看过期时间
                ipvsadm -Ln --timeout
            更详细的帮助文档：
                ipvsadm -h
                man ipvsadm
 # Redis安装
        解压: 
            tar -zxvf redis-5.0.8.tar.gz
        安装gcc-c++:
            yum install -y gcc-c++
        进入redis-5.0.8目录
            make && make install
        进入目录下utils下
            复制 redis_init_script
                cp redis_init_script /etc/init.d/
        在/usr/local/下创建redis文件夹
            拷贝目录下redis-5.0.8安装目录下的redis.conf 到 /usr/local/redis/文件下
        修改redis.conf
            daemonize yes
            dir /usr/local/redis/working (记得在相应地址创建working)
            bind 0.0.0.0 (让本机以外的计算机节点连接redis)
            requirepass shushun (redis登录密码)
        修改/etc/init.d/redis_init_script 文件
            CONF="/usr/local/redis/redis.conf"
        给文件赋予权限
            chmod 777 redis_init_script
        运行redis
            ./redis_init_script start
        设置redis开机自启动 修改redis_init_script
            vim redis_init_script
                在REDISPORT=6379上方空白处添加:
                    #chkconfig: 22345 10 90
                    #description: Start and Stop redis
                    随后保存:wq退出
            执行命令:
                chkconfig redis_init_script on
# Redis命令行客户端
        redis-cli -a password shutdown：关闭redis
        ./redis_init_script stop：关闭redis
 
        redis-cli：进入到redis客户端
        auth pwd：输入密码
        
        set key value：设置缓存
        get key：获得缓存
        del key：删除缓存
        
        redis-cli -a password ping：查看是否存活
# Redis 数据类型 string
        string 字符串
        string: 最简单的字符串类型键值对缓存，也是最基本的
        
        key相关
            keys *：查看所有的key (不建议在生产上使用，有性能影响)
        
        type key：key的类型
        
            string类型
            get/set/del：查询/设置/删除
            set rekey data：设置已经存在的key，会覆盖
            setnx rekey data：设置已经存在的key，不会覆盖
            
            set key value ex time：设置带过期时间的数据
            expire key：设置过期时间
            ttl：查看剩余时间，-1永不过期，-2过期
            
            append key：合并字符串
            strlen key：字符串长度
            
            incr key：累加1
            decr key：类减1
            incrby key num：累加给定数值
            decrby key num：累减给定数值
            
            getrange key start end：截取数据，end=-1 代表到最后
            setrange key start newdata：从start位置开始替换数据
            
            mset：连续设值
            mget：连续取值
            msetnx：连续设置，如果存在则不设置
        其他
            select index：切换数据库，总共默认16个
            flushdb：删除当前下边db中的数据
            flushall：删除所有db中的数据
 # Redis 数据类型 hash
        hash
        hash：类似map，存储结构化数据结构，比如存储一个对象（不能有嵌套对象）
        
        使用
            hset key property value：
            -> hset user name imooc
            -> 创建一个user对象，这个对象中包含name属性，name值为imooc
            
            hget user name：获得用户对象中name的值
            
            hmset：设置对象中的多个键值对
            -> hset user age 18 phone 139123123
            hmsetnx：设置对象中的多个键值对，存在则不添加
            -> hset user age 18 phone 139123123
            
            hmget：获得对象中的多个属性
            -> hmget user age phone
            
            hgetall user：获得整个对象的内容
            
            hincrby user age 2：累加属性
            hincrbyfloat user age 2.2：累加属性
            
            hlen user：有多少个属性
            
            hexists user age：判断属性是否存在
            
            hkeys user：获得所有属性
            hvals user：获得所有值
            
            hdel user：删除对象
# Redis 数据类型 list
        list
            list：列表，[a, b, c, d, …]
        
        使用
            lpush userList 1 2 3 4 5：构建一个list，从左边开始存入数据
            rpush userList 1 2 3 4 5：构建一个list，从右边开始存入数据
            lrange list start end：获得数据
            
            lpop：从左侧开始拿出一个数据
            rpop：从右侧开始拿出一个数据
            
            pig cow sheep chicken duck
            
            llen list：list长度
            lindex list index：获取list下标的值
            
            lset list index value：把某个下标的值替换
            
            linsert list before/after value：插入一个新的值
            
            lrem list num value：删除几个相同数据
            
            ltrim list start end：截取值，替换原来的list
# Redis 数据类型 zset
        sorted set：
            sorted set：排序的set，可以去重可以排序，比如可以根据用户积分做排名，积分作为set的一个数值，根据数值可以做排序。set中的每一个memeber都带有一个分数
        
        使用
            zadd zset 10 value1 20 value2 30 value3：设置member和对应的分数
            
            zrange zset 0 -1：查看所有zset中的内容
            zrange zset 0 -1 withscores：带有分数
            
            zrank zset value：获得对应的下标
            zscore zset value：获得对应的分数
            
            zcard zset：统计个数
            zcount zset 分数1 分数2：统计个数
            
            zrangebyscore zset 分数1 分数2：查询分数之间的member(包含分数1 分数2)
            zrangebyscore zset (分数1 (分数2：查询分数之间的member（不包含分数1 和 分数2）
            zrangebyscore zset 分数1 分数2 limit start end：查询分数之间的member(包含分数1 分数2)，获得的结果集再次根据下标区间做查询
            
            zrem zset value：删除member