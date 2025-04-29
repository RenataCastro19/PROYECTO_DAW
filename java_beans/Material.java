import java.io.Serializable;

public class Material implements Serializable {
    private static final long serialVersionUID = 1L;

    private int idMaterial;
    private String nombreMaterial;

    public Material() {}

    public Material(int idMaterial, String nombreMaterial) {
        this.idMaterial = idMaterial;
        this.nombreMaterial = nombreMaterial;
    }

    public int getIdMaterial() { return idMaterial; }
    public void setIdMaterial(int idMaterial) { this.idMaterial = idMaterial; }

    public String getNombreMaterial() { return nombreMaterial; }
    public void setNombreMaterial(String nombreMaterial) { this.nombreMaterial = nombreMaterial; }

}