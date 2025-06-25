FROM node:24 AS build

WORKDIR /app

COPY package*.json ./
RUN npm install

COPY . .
RUN npm run build -- --output-path=dist

FROM nginx:alpine

# Copia el build de Angular al directorio de Nginx
COPY --from=build /app/dist/browser /usr/share/nginx/html

# Copia configuración personalizada de Nginx si la tienes
# COPY nginx.conf /etc/nginx/nginx.conf

# Copia el script de entrada para variables de entorno
# COPY docker-entrypoint.sh /docker-entrypoint.sh
# RUN chmod +x /docker-entrypoint.sh

# ENTRYPOINT ["/docker-entrypoint.sh"]
# CMD ["nginx", "-g", "daemon off;"]

CMD ["nginx", "-g", "daemon off;"]