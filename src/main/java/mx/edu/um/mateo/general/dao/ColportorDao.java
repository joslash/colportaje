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
import mx.edu.um.mateo.general.model.Colportor;
import mx.edu.um.mateo.general.model.Usuario;
import org.hibernate.Criteria;
import org.hibernate.Query;
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
 * @author wilbert
 */
@Repository
@Transactional
public class ColportorDao {

    private static final Logger log = LoggerFactory.getLogger(ColportorDao.class);
    @Autowired
    private SessionFactory sessionFactory;

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

//    public Map<String, Object> lista(Map<String, Object> params) {
//        log.debug("Buscando lista de colportor con params {}", params);
//        if (params == null) {
//            params = new HashMap<>();
//        }
//
//        if (!params.containsKey(Constantes.CONTAINSKEY_MAX)) {
//            params.put(Constantes.CONTAINSKEY_MAX, 10);
//        } else {
//            params.put(Constantes.CONTAINSKEY_MAX, Math.min((Integer) params.get(Constantes.CONTAINSKEY_MAX), 100));
//        }
//
//        if (params.containsKey(Constantes.CONTAINSKEY_PAGINA)) {
//            Long pagina = (Long) params.get(Constantes.CONTAINSKEY_PAGINA);
//            Long offset = (pagina - 1) * (Integer) params.get(Constantes.CONTAINSKEY_MAX);
//            params.put(Constantes.CONTAINSKEY_OFFSET, offset.intValue());
//        }
//
//        if (!params.containsKey(Constantes.CONTAINSKEY_OFFSET)) {
//            params.put(Constantes.CONTAINSKEY_OFFSET, 0);
//        }
//        Criteria criteria = currentSession().createCriteria(Colportor.class);
//        Criteria countCriteria = currentSession().createCriteria(Colportor.class);
//
//
//        
//         if (params.containsKey(Constantes.ADDATTRIBUTE_ASOCIACION)) {
//            criteria.createCriteria(Constantes.ADDATTRIBUTE_ASOCIACION).add(Restrictions.idEq(params.get(Constantes.ADDATTRIBUTE_ASOCIACION)));
//            countCriteria.createCriteria(Constantes.ADDATTRIBUTE_ASOCIACION).add(Restrictions.idEq(params.get(Constantes.ADDATTRIBUTE_ASOCIACION)));
//        }
//
//        if (params.containsKey(Constantes.CONTAINSKEY_FILTRO)) {
//            String filtro = (String) params.get(Constantes.CONTAINSKEY_FILTRO);
//            filtro = "%" + filtro + "%";
//            Disjunction propiedades = Restrictions.disjunction();
//            propiedades.add(Restrictions.ilike("status", filtro));
//            propiedades.add(Restrictions.ilike("clave", filtro));
//            criteria.add(propiedades);
//            countCriteria.add(propiedades);
//        }
//
//        if (params.containsKey(Constantes.CONTAINSKEY_ORDER)) {
//            String campo = (String) params.get(Constantes.CONTAINSKEY_ORDER);
//            if (params.get(Constantes.CONTAINSKEY_SORT).equals(Constantes.CONTAINSKEY_DESC)) {
//                criteria.addOrder(Order.desc(campo));
//            } else {
//                criteria.addOrder(Order.asc(campo));
//            }
//        }
//
//        if (!params.containsKey(Constantes.CONTAINSKEY_REPORTE)) {
//            criteria.setFirstResult((Integer) params.get(Constantes.CONTAINSKEY_OFFSET));
//            criteria.setMaxResults((Integer) params.get(Constantes.CONTAINSKEY_MAX));
//        }
//        params.put(Constantes.CONTAINSKEY_COLPORTORES, criteria.list());
//        countCriteria.setProjection(Projections.rowCount());
//        params.put(Constantes.CONTAINSKEY_CANTIDAD, (long) countCriteria.list().size());
//
//        return params;
//    }

    public Colportor obtiene(Long id) {
        log.debug("Obtiene colportor con id = {}", id);
        Colportor colportor = (Colportor) currentSession().get(Colportor.class, id);
        return colportor;
    }
    
      public Object obtienePorUsuario(Long id) {
        log.debug("Obtiene cuenta de colportor con id = {}", id);
        String hql = "SELECT "
                + "u.id, c.status, c.clave, c.telefono, c.calle, c.colonia, c.municipio "
                + "FROM "
                + "usuarios u, roles r, usuarios_roles ur, colportores c "
                + "WHERE "
                + "u.colportor_id = :id AND ur.rol_id = r.id AND r.authority = 'ROLE_COL'";
        Query query = currentSession().createSQLQuery(hql);
        query.setLong("id", id);
        return query.uniqueResult();
    }
    
    
    

    public Colportor crea(Colportor colportor) {
        log.debug("Creando colportor : {}", colportor);
        colportor.setStatus(Constantes.STATUS_ACTIVO);
        currentSession().save(colportor);
        currentSession().flush();
        return colportor;
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