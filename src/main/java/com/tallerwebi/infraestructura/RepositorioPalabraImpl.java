import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tallerwebi.dominio.Palabra;
import com.tallerwebi.dominio.RepositorioPalabra;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

@Repository("repositorioPalabra")
public class RepositorioPalabraImpl implements RepositorioPalabra {

    private SessionFactory sessionFactory;

    @Autowired
    public RepositorioPalabraImpl(SessionFactory sessionFacyory) {
        this.sessionFactory = sessionFacyory;
    }

    @Override
    public Palabra obtenerUnaPalabraAleatoriaNoAdivinada() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'obtenerUnaPalabraAleatoria'");
    }

}