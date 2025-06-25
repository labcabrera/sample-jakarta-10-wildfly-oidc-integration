#!/bin/sh

if [ -f /usr/share/nginx/html/assets/env.js ]; then
  envsubst < /usr/share/nginx/html/assets/env.js > /usr/share/nginx/html/assets/env.js.tmp
  mv /usr/share/nginx/html/assets/env.js.tmp /usr/share/nginx/html/assets/env.js
fi

exec "$@"