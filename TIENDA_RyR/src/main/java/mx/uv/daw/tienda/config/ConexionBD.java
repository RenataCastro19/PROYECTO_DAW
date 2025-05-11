package mx.uv.daw.tienda.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;

@Component
public class ConexionBD implements ApplicationRunner {

    private final DataSource dataSource;

    public ConexionBD(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        try (Connection conn = dataSource.getConnection()) {
            if (conn.isValid(1)) {
                System.out.println("Conexión exitosa a la base de datos.");
            } else {
                System.err.println("No se pudo validar la conexión a la base de datos.");
            }
        }
    }
}
