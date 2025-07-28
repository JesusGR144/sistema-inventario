package castores.sistema_inventario;

import castores.sistema_inventario.enums.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import castores.sistema_inventario.modelo.Producto;
import java.math.BigDecimal;


@SpringBootApplication
public class SistemaInventarioApplication {

	public static void main(String[] args) {
		SpringApplication.run(SistemaInventarioApplication.class, args);
		System.out.println(new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder().encode("admin123"));
		System.out.println(new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder().encode("almacen123"));
		}
}
