# --- BƯỚC 1: Build file WAR bằng Maven ---
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
# Chạy lệnh Maven để build ra file WAR (bỏ qua test cho nhanh)
RUN mvn clean package -DskipTests

# --- BƯỚC 2: Chạy trên Tomcat 10 ---
FROM tomcat:10.1-jdk17
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy file WAR vừa build ở bước 1 vào thư mục webapps của Tomcat và đổi tên thành ROOT.war
COPY --from=build /app/target/*.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080
CMD ["catalina.sh", "run"]
