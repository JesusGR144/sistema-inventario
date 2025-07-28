package castores.sistema_inventario.modelo;

import castores.sistema_inventario.enums.TipoRol;
import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "correo", nullable = false, length = 50, unique = true)
    private String correo;

    @Column(name = "contrasena", nullable = false, length = 25)
    private String contrasena;

    @Enumerated(EnumType.STRING)
    @Column(name = "rol", nullable = false, length = 15)
    private TipoRol rol;

    @Column(name = "estatus", nullable = false)
    private Boolean estatus = true;
}
