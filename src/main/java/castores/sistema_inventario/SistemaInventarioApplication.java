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

		// ==== Prueba rápida de Lombok ====
		Producto producto = new Producto(
				null,
				Talla.M,
				Color.NEGRO,
				TIpoPlayera.MANGA_CORTA,
				new BigDecimal("249.99"),
				15,
				true,
				null,
				null
		);

		System.out.println("Producto:");
		System.out.println(producto); // toString() de Lombok
		System.out.println("Nombre generado: " + producto.getNombre()); // Método @Transient

		producto.setCantidadActual(25);
		System.out.println("Nueva cantidad: " + producto.getCantidadActual());

	}
}
