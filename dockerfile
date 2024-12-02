FROM eclipse-temurin:17-jdk-alpine as build
   WORKDIR /workspace/app

   COPY mvnw .
   COPY .mvn .mvn
   COPY pom.xml .
   COPY src src

   RUN ./mvnw install -DskipTests
   RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

   FROM eclipse-temurin:17-jre-alpine
   VOLUME /tmp
   ARG DEPENDENCY=/workspace/app/target/dependency
   COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
   COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
   COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app

   ENV SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/CarRentalSystem
   ENV SPRING_DATASOURCE_USERNAME=postgres
   ENV SPRING_DATASOURCE_PASSWORD=C@rRental1System

   ENTRYPOINT ["java","-cp","app:app/lib/*","com.example.CarRentalSystem.CarRentalSystemApplication"]
