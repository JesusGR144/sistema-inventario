package castores.sistema_inventario.enums;

public enum Color {
    NEGRO("Negro"),
    BLANCO("Blanco"),
    AZUL_MARINO("Azul Marino"),
    ROJO("Rojo"),
    NARANJA("Naranja");

    private final String nombre;

    Color(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }
}
