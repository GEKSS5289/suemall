# sue-mall Release
测试环境-CentOS 7
## 安装jdk1.8
    tar -zxvf /home/software/jdk-8u191-linux-x64.tar.gz  解压
    export JAVA_HOME=/usr/java/jdk1.8.0_191
    export CLASSPATH=.:%JAVA_HOME%/lib/dt.jar:%JAVA_HOME%/lib/tools.jar  
    export PATH=$PATH:$JAVA_HOME/bin
## 安装tomcat9.0.33
    tar -zxvf /home/software/apache-tomcat-9.0.33.tar.gz 解压
    cd apache-tomcat-9.0.33/bin
    ./startup.sh 启动
    
