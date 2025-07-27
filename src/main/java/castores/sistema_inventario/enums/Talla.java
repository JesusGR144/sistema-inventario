package castores.sistema_inventario.enums;

public enum Talla {
    CH("Chica"),
    M("Mediana"),
    G("Grande"),
    EG("Extra Grande");

    private final String descripcion;

    Talla(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return  descripcion;
    }
}
