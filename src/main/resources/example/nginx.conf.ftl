user  nginx;
worker_processes  1;

error_log  /var/log/nginx/error.log warn;
pid        /var/run/nginx.pid;


events {
    worker_connections  1024;
}


http {
    include       /etc/nginx/mime.types;
    default_type  application/octet-stream;

    log_format  main  '$remote_addr - $remote_user [$time_local] "$request" '
                      '$status $body_bytes_sent "$http_referer" '
                      '"$http_user_agent" "$http_x_forwarded_for"';

    upstream web-app {
        server $hostName:8080;
    }

    access_log  /var/log/nginx/access.log  main;

    sendfile        on;
    #tcp_nopush     on;

    keepalive_timeout  65;

    #gzip  on;

    <#if !https>
    server {
            listen 80;

            server_name _;

            location /.well-known/acme-challenge/ {
                root /var/www/certbot;
            }

            location / {
                <#if !forceSSL>
                    proxy_pass http://web-app;
                </#if>
                <#if forceSSL>
                    return 301 https://web-app;
                </#if>
            }
    }
    </#if>
    <#if https>
        server {
            listen 443 ssl;

            server_name _;
            server_tokens off;

            ssl_certificate /etc/letsencrypt/live/$domain/fullchain.pem;
            ssl_certificate_key /etc/letsencrypt/live/example.org/privkey.pem;
            include /etc/letsencrypt/options-ssl-nginx.conf;
            ssl_dhparam /etc/letsencrypt/ssl-dhparams.pem;

            location / {
                proxy_pass http://web-app;
                proxy_set_header    Host                $http_host;
                proxy_set_header    X-Real-IP           $remote_addr;
                proxy_set_header    X-Forwarded-For     $proxy_add_x_forwarded_for;
            }
        }
    </#if>
}
