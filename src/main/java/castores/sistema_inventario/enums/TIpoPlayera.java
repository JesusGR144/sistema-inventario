package castores.sistema_inventario.enums;

public enum TIpoPlayera {
    MANGA_CORTA("Manga Corta"),
    MANGA_LARGA("Manga Larga");

    private final String descripcion;

    TIpoPlayera(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
