./mvnw clean package && mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar) && docker build -t guillermo/multifile-uploader . && docker run -p 8080:8080  guillermo/multifile-uploader