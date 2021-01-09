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
        listen [::]:80;

        root /var/www/html;
        index index.html index.htm index.nginx-debian.html;

        server_name _;

        location / {
            proxy_pass http://web-app;
        }

        location ~ /.well-known/acme-challenge {
            allow all;
            root /var/www/html;
        }
    }
    </#if>
    <#if https>
    server {
            listen 443 ssl http2;
            listen [::]:443 ssl http2;
            server_name example.com www.example.com;

            server_tokens off;

            ssl_certificate /etc/letsencrypt/live/example.com/fullchain.pem;
            ssl_certificate_key /etc/letsencrypt/live/example.com/privkey.pem;

            ssl_buffer_size 8k;

            ssl_dhparam /etc/ssl/certs/dhparam-2048.pem;

            ssl_protocols TLSv1.2 TLSv1.1 TLSv1;
            ssl_prefer_server_ciphers on;

            ssl_ciphers ECDH+AESGCM:ECDH+AES256:ECDH+AES128:DH+3DES:!ADH:!AECDH:!MD5;

            ssl_ecdh_curve secp384r1;
            ssl_session_tickets off;

            ssl_stapling on;
            ssl_stapling_verify on;
            resolver 8.8.8.8;

            location / {
                try_files $uri @nodejs;
            }

            location @nodejs {
                proxy_pass http://nodejs:8080;
                add_header X-Frame-Options "SAMEORIGIN" always;
                add_header X-XSS-Protection "1; mode=block" always;
                add_header X-Content-Type-Options "nosniff" always;
                add_header Referrer-Policy "no-referrer-when-downgrade" always;
                add_header Content-Security-Policy "default-src * data: 'unsafe-eval' 'unsafe-inline'" always;
                # add_header Strict-Transport-Security "max-age=31536000; includeSubDomains; preload" always;
                # enable strict transport security only if you understand the implications
            }

            root /var/www/html;
            index index.html index.htm index.nginx-debian.html;
    }
    </#if>
}
