package castores.sistema_inventario.enums;

public enum TipoPlayera {
    MANGA_CORTA("Manga Corta"),
    MANGA_LARGA("Manga Larga");

    private final String descripcion;

    TipoPlayera(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
