# ─── Stage 1: Build Angular ───────────────────────────────────────────────────
FROM node:24-alpine AS builder

WORKDIR /app

COPY package*.json ./
RUN npm ci

COPY . .
RUN npm run build

# ─── Stage 2: Serve with nginx ────────────────────────────────────────────────
FROM nginx:alpine AS runtime

# Copy Angular build output
COPY --from=builder /app/dist/frontend/browser /usr/share/nginx/html

# Nginx config: proxy /api → backend, SPA fallback
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80

