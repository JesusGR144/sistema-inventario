# sistema-inventario
Sistema de inventario - Prueba técnica

# DOCUMENTACION
# IDE utilizado
IntelliJ IDEA
# Versión del lenguaje de programación utilizado
java 21.0.8
# DBMS utilizado y su versión
MySQL Server 9.3

# Lista de pasos para correr su aplicación.
-------------BACKEND-----------------------
1. Clonar el repositorio o descargar el proyecto
2. Abrirlo en algún IDE (Intellij IDEA o NetBeans)
3. Configurar el archivo application.properties que esta en la ruta (src\main\resources)
4. Veras, algo como lo siguiente:

-------------------------------------------
spring.application.name=sistema-inventarios
# Conexion a mysql
spring.datasource.url=jdbc:mysql://localhost:3306/nombre_de_tu_bd?createDatabaseIfNotExist=true
spring.datasource.username=tu_usuario
spring.datasource.password=tu_contraseña
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

- Configurar el patron del log (en lugar del archivo logback-spring.xml)
logging.pattern.console=[%thread] %-5level; %logger - %msg%n

- Configurar el nivel de log (INFO)
logging.level.root=INFO

- Opcional, configurar niveles por paquetes
-logging.level.gm.sistema-inventarios=DEBUG
-logging.level.org.springframework=WARN

- Cambiar el puerto del servidor
-server.port=8081
spring.main.banner-mode=off
-------------------------------------------

En donde omitiras los valores de (nombre_de_tu_bd, tu_usuario, tu_contraseña) con tus respectivas credenciales

5. Una vez configuradas tus credenciales puedes presionar el boton verde > RUN 
* O si prefieres abrir una terminal y ejecutar el comando 'mvnw spring-boot:run' sin comillas

DE ESTA FORMA ESTARA CORRIENDO EL BACK

--------------FRONTEND------------------
*De preferencia correrlo en Visual Studio Code con la extension de Go-Live*
1. Un avez abierto Vs Code (de preferencia) abre el archivo index.html
2. Click derecho y presiona en Open with Live Server
*Para esto debes tener almenos un usuario de cada ROL registrado en la bd*

*IMPORTANTE - al ejecutar el BACK, se generan 2 contraseñas codificadas, debes registrar o actualizar dichas contraseñas en la BD*
*las contraseñas por defecto que se codifican son (admin123 y almacen123) respectivamente*

3. Una vez que se registren o actualicen las contraseñas, puedes acceder con los correos de tus usuarios y con sus respectivas contraseñas *sea admin123 o almacen123* segun sea el caso
