  /*
 * The MIT License
 *
 * Copyright 2012 jdmr.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package mx.edu.um.mateo.general.dao; 

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import mx.edu.um.mateo.Constantes;
import mx.edu.um.mateo.general.model.Asociacion;
import mx.edu.um.mateo.general.model.Asociado;
import mx.edu.um.mateo.general.model.Colportor;
import mx.edu.um.mateo.general.model.Usuario;
import org.hibernate.*;
import org.hibernate.criterion.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author wilbert
 */
@Repository
@Transactional
public class ColportorDao {

    private static final Logger log = LoggerFactory.getLogger(ColportorDao.class);
    @Autowired
    private SessionFactory sessionFactory;
    @Autowired
    private RolDao rolDao;

    public ColportorDao() {
        log.info("Nueva instancia de ColportorDao");
    }

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }
    
     public Map<String, Object> lista(Map<String, Object> params) {
        log.debug("Buscando lista de colportores con params {}", params);
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

        if (!params.containsKey("asociacion")) {
            params.put("colportores", new ArrayList());
            params.put("cantidad", 0L);

            return params;
        }
        
        Criteria criteria = currentSession().createCriteria(Colportor.class);
        Criteria countCriteria = currentSession().createCriteria(Colportor.class);

        if (params.containsKey("asociacion")) {
            log.debug("valor de asociacion"+params.get("asociacion"));
            criteria.createCriteria("asociacion").add(Restrictions.eq("id",((Asociacion)params.get("asociacion")).getId()));
            countCriteria.createCriteria("asociacion").add(Restrictions.eq("id",((Asociacion)params.get("asociacion")).getId()));
        }

        if (params.containsKey("filtro")) {
            String filtro = (String) params.get("filtro");
            Disjunction propiedades = Restrictions.disjunction();
            propiedades.add(Restrictions.ilike("username", filtro, MatchMode.ANYWHERE));
            propiedades.add(Restrictions.ilike("nombre", filtro, MatchMode.ANYWHERE));
            propiedades.add(Restrictions.ilike("apellido", filtro, MatchMode.ANYWHERE));
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
        params.put("colportores", criteria.list());

        countCriteria.setProjection(Projections.rowCount());
        params.put("cantidad", (Long) countCriteria.list().get(0));

        return params;
    }

    public Colportor obtiene(Long id) {
        log.debug("Obtiene colportor con id = {}", id);
        Colportor colportor = (Colportor) currentSession().get(Colportor.class, id);
        return colportor;
    }
    
    public Colportor obtiene(String clave) {
        log.debug("Obtiene colportor con clave = {}", clave);
        Criteria criteria = currentSession().createCriteria(Colportor.class);
        criteria.add(Restrictions.eq("clave", clave));
        Colportor colportor = (Colportor) criteria.uniqueResult();
        return colportor;
    }
    
    public Colportor crea(Colportor colportor, String[] nombreDeRoles) {
        log.debug("Creando colportor : {}", colportor);
        colportor.addRol(rolDao.obtiene("ROLE_COL"));
        colportor.setStatus(Constantes.STATUS_ACTIVO);
        currentSession().save(colportor);
        currentSession().flush();
        return colportor;
    }
        public Colportor actualiza(Colportor colportor, String[] nombreDeRoles) {
        Colportor nuevoColportor= (Colportor) currentSession().get(Usuario.class, colportor.getId());
        nuevoColportor.setVersion(colportor.getVersion());
        nuevoColportor.setUsername(colportor.getUsername());
        nuevoColportor.setNombre(colportor.getNombre());
        nuevoColportor.setApellidoP(colportor.getApellidoP());
        nuevoColportor.setApellidoM(colportor.getApellidoM());
        
        log.debug("password"+nuevoColportor.getPassword());


        try {
            currentSession().update(nuevoColportor);
            currentSession().flush();
        } catch (NonUniqueObjectException e) {
            log.warn("Ya hay un objeto previamente cargado, intentando hacer merge", e);
            currentSession().merge(nuevoColportor);
            currentSession().flush();
        }
        return nuevoColportor;
    }
    public Colportor actualiza(Colportor colportor) {
        log.debug("Actualizando colportor {}", colportor);

        //trae el objeto de la DB 
        Colportor nuevo = (Colportor) currentSession().get(Colportor.class, colportor.getId());
        //actualiza el objeto
        BeanUtils.copyProperties(colportor, nuevo);
        //lo guarda en la BD

        currentSession().update(nuevo);
        currentSession().flush();
        return nuevo;
    }

   public String elimina(Long id) {
        log.debug("Eliminando colportor {}", id);

        Colportor colportor = obtiene(id);
        colportor.setStatus(Constantes.STATUS_INACTIVO);
        actualiza(colportor);
        String clave = colportor.getClave();
        return clave;
    }
}