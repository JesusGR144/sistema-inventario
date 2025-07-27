package castores.sistema_inventario.modelo;

import castores.sistema_inventario.enums.Color;
import castores.sistema_inventario.enums.TIpoPlayera;
import castores.sistema_inventario.enums.Talla;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "productos")
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProducto;

    @NotNull(message = "La talla es obligatoria")
    @Enumerated(EnumType.STRING)
    @Column(name = "talla", nullable = false, length = 10)
    private Talla talla;

    @NotNull(message = "El color es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(name = "color", nullable = false, length = 20)
    private Color color;

    @NotNull(message = "EL tipo es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false, length = 20)
    private TIpoPlayera tipo;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "EL precio debe ser mayor a 0")
    @Digits(integer = 3, fraction = 2, message = "Formato de precio inv{alido")
    @Column(name = "precio", nullable = false, precision = 5, scale = 2)
    private BigDecimal precio;

    @Min(value = 0, message = "La cantidad no puede ser negativa")
    @Column(name = "cantidad_actual", nullable = false)
    private Integer cantidadActual = 0;

    @Column(name = "activo", nullable = false)
    private Boolean activo = true;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        fechaActualizacion = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        fechaActualizacion = LocalDateTime.now();
    }

    // Métodos de negocio
    public void aumentarInventario(Integer cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
        }
        this.cantidadActual += cantidad;
    }

    public void disminuirInventario(Integer cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
        }
        if (this.cantidadActual < cantidad) {
            throw new IllegalArgumentException("No hay suficiente inventario disponible");
        }
        this.cantidadActual -= cantidad;
    }

    public void darDeBaja() {
        this.activo = false;
    }

    public void reactivar() {
        this.activo = true;
    }

    // Nombre generado dinámicamente (no se guarda en la BD)
    @Transient
    public String getNombre() {
        return String.format("Playera %s %s %s",
                color.getNombre(),
                talla.name(),
                tipo.getDescripcion());
    }
}
