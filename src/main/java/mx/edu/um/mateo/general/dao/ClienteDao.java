package mx.edu.um.mateo.general.dao;

import java.util.HashMap;
import java.util.Map;
import mx.um.edu.mateo.Constantes;
import mx.edu.um.mateo.general.model.Cliente;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.utils.UltimoException;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author nujev
 */
@Repository
@Transactional
public class ClienteDao {   

    private static final Logger log = LoggerFactory.getLogger(ClienteDao.class);
    @Autowired
    private SessionFactory sessionFactory;

    public ClienteDao() {
        log.info("Nueva instancia de ClienteDao");
    }

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    public Map<String, Object> lista(Map<String, Object> params) {
        log.debug("Buscando lista de cliente con params {}", params);
        if (params == null) {
            params = new HashMap<>();
        }

        if (!params.containsKey(Constantes.CONTAINSKEY_MAX)) {
            params.put(Constantes.CONTAINSKEY_MAX, 10);
        } else {
            params.put(Constantes.CONTAINSKEY_MAX, Math.min((Integer) params.get(Constantes.CONTAINSKEY_MAX), 100));
        }

        if (params.containsKey(Constantes.CONTAINSKEY_PAGINA)) {
            Long pagina = (Long) params.get(Constantes.CONTAINSKEY_PAGINA);
            Long offset = (pagina - 1) * (Integer) params.get(Constantes.CONTAINSKEY_MAX);
            params.put(Constantes.CONTAINSKEY_OFFSET, offset.intValue());
        }

        if (!params.containsKey(Constantes.CONTAINSKEY_OFFSET)) {
            params.put(Constantes.CONTAINSKEY_OFFSET, 0);
        }
        Criteria criteria = currentSession().createCriteria(Cliente.class);
        Criteria countCriteria = currentSession().createCriteria(Cliente.class);
        
        if (params.containsKey(Constantes.CONTAINSKEY_ASOCIACION)) {
            criteria.createCriteria(Constantes.CONTAINSKEY_ASOCIACION).add(Restrictions.idEq(params.get(Constantes.CONTAINSKEY_ASOCIACION)));
            countCriteria.createCriteria(Constantes.CONTAINSKEY_ASOCIACION).add(Restrictions.idEq(params.get(Constantes.CONTAINSKEY_ASOCIACION)));
        }
        
        if (params.containsKey(Constantes.CONTAINSKEY_FILTRO)) {
            String filtro = (String) params.get(Constantes.CONTAINSKEY_FILTRO);
            Disjunction propiedades = Restrictions.disjunction();
            propiedades.add(Restrictions.ilike("nombre", filtro, MatchMode.ANYWHERE));
            propiedades.add(Restrictions.ilike("apellidoP", filtro, MatchMode.ANYWHERE));
            criteria.add(propiedades);
            countCriteria.add(propiedades);
        }

        if (params.containsKey(Constantes.CONTAINSKEY_ORDER)) {
            String campo = (String) params.get(Constantes.CONTAINSKEY_ORDER);
            if (params.get(Constantes.CONTAINSKEY_SORT).equals(Constantes.CONTAINSKEY_DESC)) {
                criteria.addOrder(Order.desc(campo));
            } else {
                criteria.addOrder(Order.asc(campo));
            }
        }

        if (!params.containsKey(Constantes.CONTAINSKEY_REPORTE)) {
            criteria.setFirstResult((Integer) params.get(Constantes.CONTAINSKEY_OFFSET));
            criteria.setMaxResults((Integer) params.get(Constantes.CONTAINSKEY_MAX));
        }
        params.put(Constantes.CONTAINSKEY_CLIENTES, criteria.list());

        countCriteria.setProjection(Projections.rowCount());
        params.put(Constantes.CONTAINSKEY_CANTIDAD, (Long) countCriteria.list().get(0));

        return params;
    }

    public Cliente obtiene(Long id) {
        log.debug("Obtiene cliente con id = {}", id);
        Cliente cliente = (Cliente) currentSession().get(Cliente.class, id);
        return cliente;
    }

    public Cliente crea(Cliente cliente) {
        return crea(cliente, null);
    }

    public Cliente crea(Cliente cliente, Usuario usuario) {
        log.debug("Creando cliente : {}", cliente);
        if (usuario != null) {
            //cliente.setAsociacion(usuario.getAsociacion());
        }
        currentSession().save(cliente);
        currentSession().flush();
        return cliente;
    }

    public Cliente actualiza(Cliente cliente) {
        return actualiza(cliente, null);
    }

    public Cliente actualiza(Cliente cliente, Usuario usuario) {
        log.debug("Actualizando cliente {}", cliente);
        
        Cliente nueva = (Cliente)currentSession().get(Cliente.class, cliente.getId());
        BeanUtils.copyProperties(cliente, nueva);
        
        if (usuario != null) {
            //nueva.setOrganizacion(usuario.getAsociacion());
        }
        currentSession().update(nueva);
        currentSession().flush();
        return nueva;
    }

    public String elimina(Long id) throws UltimoException {
        log.debug("Eliminando cliente con id {}", id);
        Cliente cliente = obtiene(id);
        currentSession().delete(cliente);
        currentSession().flush();
        String nombre = cliente.getNombre();
        return nombre;
    }
}
