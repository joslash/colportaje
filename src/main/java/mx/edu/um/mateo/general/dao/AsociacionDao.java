/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.general.dao;

import java.util.HashMap;
import java.util.Map;
import mx.edu.um.mateo.Constantes;
import mx.edu.um.mateo.general.model.Asociacion;
import mx.edu.um.mateo.general.model.Usuario;
import org.hibernate.Criteria;
import org.hibernate.NonUniqueObjectException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author gibrandemetrioo
 */
@Repository
@Transactional
public class AsociacionDao {

    private static final Logger log = LoggerFactory.getLogger(AsociacionDao.class);
    @Autowired
    private SessionFactory sessionFactory;

    public AsociacionDao() {
        log.info("Nueva instancia de AsociacionDao");
    }

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    public Map<String, Object> lista(Map<String, Object> params) {
        log.debug("Buscando lista de asociaciones con params {}", params);
        if (params == null) {
            params = new HashMap<>();
        }

        if (!params.containsKey("max")) {
            params.put("max", 10);
        } else {
            params.put("max", Math.min((Integer) params.get("max"), 100));
        }

        if (params.containsKey("pagina")) {
            Long pagina = (Long) params.get("pagina");
            Long offset = (pagina - 1) * (Integer) params.get("max");
            params.put("offset", offset.intValue());
        }

        if (!params.containsKey("offset")) {
            params.put("offset", 0);
        }
        Criteria criteria = currentSession().createCriteria(Asociacion.class);
        Criteria countCriteria = currentSession().createCriteria(Asociacion.class);
        
        if (params.containsKey("union")) {
            criteria.createCriteria("union").add(Restrictions.idEq(params.get("union")));
            countCriteria.createCriteria("union").add(Restrictions.idEq(params.get("union")));
        }

        if (params.containsKey("filtro")) {
            String filtro = (String) params.get("filtro");
            filtro = "%" + filtro + "%";
            Disjunction propiedades = Restrictions.disjunction();
            propiedades.add(Restrictions.ilike("nombre", filtro));
            propiedades.add(Restrictions.ilike("nombreCompleto", filtro));
            criteria.add(propiedades);
            countCriteria.add(propiedades);
        }

        if (params.containsKey("order")) {
            String campo = (String) params.get("order");
            if (params.get("sort").equals("desc")) {
                criteria.addOrder(Order.desc(campo));
            } else {
                criteria.addOrder(Order.asc(campo));
            }
        }

        if (!params.containsKey("reporte")) {
            criteria.setFirstResult((Integer) params.get("offset"));
            criteria.setMaxResults((Integer) params.get("max"));
        }
        params.put("asociaciones", criteria.list());

        countCriteria.setProjection(Projections.rowCount());
        params.put("cantidad", (Long) countCriteria.list().get(0));

        return params;
    }

    public Asociacion obtiene(Long id) {
        Asociacion asociacion = (Asociacion) currentSession().get(Asociacion.class, id);
        return asociacion;
    }

    public Asociacion crea(Asociacion asociacion) {
        return this.crea(asociacion, null);
    }

    public Asociacion crea(Asociacion asociacion, Usuario usuario) {
        Session session = currentSession();
        if (usuario != null) {
            asociacion.setUnion(usuario.getAsociacion().getUnion());
        }
        session.save(asociacion);
        if (usuario != null) {
            usuario.setAsociacion(asociacion);
        }
        session.flush();
        return asociacion;
    }

    public Asociacion actualiza(Asociacion asociacion) {
        return this.actualiza(asociacion, null);
    }

    public Asociacion actualiza(Asociacion asociacion, Usuario usuario) {
        Session session = currentSession();
        if (usuario != null) {
            asociacion.setUnion(usuario.getAsociacion().getUnion());
        }
        try {
            // Actualiza la asociacion
            log.debug("Actualizando asociacion");
            session.update(asociacion);
            session.flush();

        } catch (NonUniqueObjectException e) {
            try {
                session.merge(asociacion);
            } catch (Exception ex) {
                log.error("No se pudo actualizar la asociacion", ex);
                throw new RuntimeException("No se pudo actualizar la asociacion", ex);
            }
        }
        if (usuario != null) {
            usuario.setAsociacion(asociacion);
        }
        session.flush();
        return asociacion;
    }

    public String elimina(Long id, Long unionId) {
        log.debug("Eliminando asociación {}", id);
        Asociacion asociacion = obtiene(id);
        String nombre = asociacion.getNombre();
        asociacion.setStatus(Constantes.STATUS_INACTIVO);
        actualiza(asociacion);
        return nombre;
    }
}
