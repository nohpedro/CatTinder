# ===== BUILD STAGE =====
FROM node:20-alpine AS build

WORKDIR /app

# Copiamos los archivos de dependencias
COPY package.json package-lock.json ./

# Instalamos dependencias
RUN npm ci

# Copiamos todo el código fuente
COPY . .

# Build de producción
RUN npm run build

# ===== PRODUCTION STAGE =====
FROM nginx:alpine

# Copiamos los archivos de build al contenedor de nginx
COPY --from=build /app/dist /usr/share/nginx/html

# ===== CRUCIAL: Copiar tu configuración de nginx =====
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Exponemos el puerto
EXPOSE 5174

# Iniciamos nginx
CMD ["nginx", "-g", "daemon off;"]