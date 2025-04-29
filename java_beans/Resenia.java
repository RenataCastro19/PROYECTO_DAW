import java.io.Serializable;
import java.sql.*;

public class Resenia implements Serializable {
    private static final long serialVersionUID = 1L;

    private int idResenia;
    private int rUsuario;
    private int rProducto;
    private int calificacion;
    private String comentario;
    private java.sql.Timestamp fechaResenia;

    public Resenia() {}

    public Resenia(int idResenia, int rUsuario, int rProducto, int calificacion, String comentario, java.sql.Timestamp fechaResenia) {
        this.idResenia = idResenia;
        this.rUsuario = rUsuario;
        this.rProducto = rProducto;
        this.calificacion = calificacion;
        this.comentario = comentario;
        this.fechaResenia = fechaResenia;
    }

    public int getIdResenia() { return idResenia; }
    public void setIdResenia(int idResenia) { this.idResenia = idResenia; }

    public int getRUsuario() { return rUsuario; }
    public void setRUsuario(int rUsuario) { this.rUsuario = rUsuario; }

    public int getRProducto() { return rProducto; }
    public void setRProducto(int rProducto) { this.rProducto = rProducto; }

    public int getCalificacion() { return calificacion; }
    public void setCalificacion(int calificacion) { this.calificacion = calificacion; }

    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }

    public java.sql.Timestamp getFechaResenia() { return fechaResenia; }
    public void setFechaResenia(java.sql.Timestamp fechaResenia) { this.fechaResenia = fechaResenia; }

}