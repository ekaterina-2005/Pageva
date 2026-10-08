FROM node:18-alpine AS builder
WORKDIR /app

COPY package*.json ./
RUN npm install

COPY . .

RUN npm run build -- --configuration=production

FROM nginx:alpine

RUN rm -rf /etc/nginx/conf.d/default.conf

COPY --from=builder /app/dist/front/browser /usr/share/nginx/html

COPY nginx.conf /etc/nginx/conf.d

EXPOSE 4200

CMD ["nginx", "-g", "daemon off;"]
