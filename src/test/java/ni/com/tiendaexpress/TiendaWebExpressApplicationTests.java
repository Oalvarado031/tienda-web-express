package ni.com.tiendaexpress;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Disabled("Requiere PostgreSQL en ejecucion. Habilitar cuando la BD este disponible.")
class TiendaWebExpressApplicationTests {

    @Test
    void contextLoads() {
    }
}
