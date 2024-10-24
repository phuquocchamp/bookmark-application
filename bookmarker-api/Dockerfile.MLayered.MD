Dockerfile trên sử dụng **multi-stage builds**, một phương pháp phổ biến để tối ưu kích thước của Docker images và làm cho quá trình build hiệu quả hơn. Hãy giải thích từng phần của file Dockerfile:

### Tổng quan về multi-stage builds
Multi-stage builds là một cách để tách quá trình build thành nhiều giai đoạn (stages). Giai đoạn đầu có thể bao gồm những bước build phức tạp, trong khi các giai đoạn sau sẽ sử dụng kết quả từ giai đoạn trước và chỉ giữ lại những phần cần thiết. Điều này giúp giảm kích thước Docker image cuối cùng bằng cách loại bỏ các phần không cần thiết (như mã nguồn hoặc công cụ build).

### Giai đoạn 1: Extract các layers của ứng dụng
```dockerfile
FROM eclipse-temurin:17-jre-focal AS builder
WORKDIR application
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} application.jar
RUN java -Djarmode=layertools -jar application.jar extract
```

#### Giải thích:

1. **FROM eclipse-temurin:17-jre-focal AS builder**:
    - Sử dụng image base `eclipse-temurin:17-jre-focal` (Java Runtime Environment 17 trên Ubuntu Focal).
    - `AS builder`: Đặt tên cho giai đoạn này là `builder`. Tên này sẽ được sử dụng trong giai đoạn sau để tham chiếu đến các file được tạo ra từ đây.

2. **WORKDIR application**:
    - Thiết lập thư mục làm việc là `/application` bên trong container.

3. **ARG JAR_FILE=target/*.jar**:
    - Khai báo biến `JAR_FILE`, với giá trị mặc định là `target/*.jar`. Đây là đường dẫn của file `.jar` được build từ quá trình build Spring Boot.

4. **COPY ${JAR_FILE} application.jar**:
    - Sao chép file `.jar` từ thư mục host (máy local) vào thư mục `/application` trong container với tên là `application.jar`.

5. **RUN java -Djarmode=layertools -jar application.jar extract**:
    - Chạy lệnh `java` với tùy chọn `-Djarmode=layertools` để sử dụng công cụ Spring Boot Layertools. Lệnh này giúp chia nhỏ file `.jar` thành nhiều lớp (layers) như `dependencies` (phụ thuộc), `snapshot-dependencies`, `spring-boot-loader` và `application`.
    - Mục tiêu là tách các thành phần để có thể xử lý theo từng lớp, giúp cải thiện tốc độ build và caching trong Docker.

### Giai đoạn 2: Copy các lớp đã extract và tạo Docker image nhỏ gọn
```dockerfile
FROM eclipse-temurin:17-jre-focal
WORKDIR application
COPY --from=builder application/dependencies/ ./
COPY --from=builder application/spring-boot-loader/ ./
COPY --from=builder application/snapshot-dependencies/ ./
COPY --from=builder application/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]
```

#### Giải thích:

1. **FROM eclipse-temurin:17-jre-focal**:
    - Bắt đầu giai đoạn mới với cùng một image base là `eclipse-temurin:17-jre-focal`. Giai đoạn này sẽ chỉ giữ lại các lớp cần thiết từ giai đoạn `builder`.

2. **WORKDIR application**:
    - Thiết lập thư mục làm việc là `/application` bên trong container, giống như trong giai đoạn trước.

3. **COPY --from=builder application/dependencies/ ./**:
    - Sao chép thư mục `dependencies/` từ giai đoạn `builder` vào thư mục hiện tại (`./`) trong giai đoạn này.

4. **COPY --from=builder application/spring-boot-loader/ ./**:
    - Sao chép thư mục `spring-boot-loader/` từ giai đoạn `builder`.

5. **COPY --from=builder application/snapshot-dependencies/ ./**:
    - Sao chép thư mục `snapshot-dependencies/` từ giai đoạn `builder`.

6. **COPY --from=builder application/application/ ./**:
    - Sao chép lớp chứa mã ứng dụng (thư mục `application/`) từ giai đoạn `builder`.

7. **ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]**:
    - Thiết lập lệnh khởi động cho container: sử dụng `JarLauncher` của Spring Boot để chạy ứng dụng. `JarLauncher` sẽ đảm nhiệm việc tải và khởi chạy các lớp của ứng dụng.

### Lợi ích của Multi-Stage Build:

1. **Tối ưu dung lượng**: Bằng cách chia nhỏ quá trình build và chỉ giữ lại các file cần thiết (các lớp đã được extract), image cuối cùng nhỏ hơn nhiều so với việc giữ nguyên toàn bộ quá trình build trong một image.
2. **Caching**: Docker có khả năng cache các lớp không thay đổi, chẳng hạn như `dependencies/` hoặc `spring-boot-loader/`. Điều này giúp tối ưu thời gian build khi chỉ có thay đổi ở một phần nhỏ của ứng dụng.
3. **Tách biệt môi trường build và runtime**: Môi trường build (bao gồm các công cụ build) chỉ tồn tại trong giai đoạn đầu. Trong giai đoạn thứ hai, chỉ có các thành phần runtime cần thiết được giữ lại.

### Kết luận
Multi-stage build này giúp tối ưu kích thước Docker image và giảm thời gian build nhờ việc tách các lớp trong file `.jar` của ứng dụng Spring Boot. Phương pháp này rất phù hợp khi triển khai ứng dụng Spring Boot với Docker trong môi trường production.