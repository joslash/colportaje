/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.general.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import mx.edu.um.mateo.Constantes;
import mx.edu.um.mateo.general.dao.AsociadoDao;
import mx.edu.um.mateo.general.dao.UsuarioDao;
import mx.edu.um.mateo.general.model.Asociado;
import mx.edu.um.mateo.general.model.Rol;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.utils.ReporteException;
import org.apache.commons.lang.StringUtils;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author gibrandemetrioo
 *
 */
@Controller
@RequestMapping(Constantes.PATH_ASOCIADO)
public class AsociadoController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(AsociadoController.class);
    @Autowired
    private AsociadoDao asociadoDao;
    @Autowired
    private UsuarioDao usuarioDao;
    @RequestMapping
    public String lista(HttpServletRequest request, HttpServletResponse response,
            @RequestParam(required = false) String filtro,
            @RequestParam(required = false) Long pagina,
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) String correo,
            @RequestParam(required = false) String order,
            @RequestParam(required = false) String sort,
            Usuario usuario,
            Errors errors,
            Model modelo) {
        log.debug("Mostrando lista de Asociado");
        Map<String, Object> params = new HashMap<>();
        //Long asociacionId = (Long) request.getSession().getAttribute("asociacionId");
        //params.put(Constantes.ADDATTRIBUTE_ASOCIACION, asociacionId);

        if (StringUtils.isNotBlank(filtro)) {
            params.put(Constantes.CONTAINSKEY_FILTRO, filtro);
        }
        if (StringUtils.isNotBlank(order)) {
            params.put(Constantes.CONTAINSKEY_ORDER, order);
            params.put(Constantes.CONTAINSKEY_SORT, sort);
        }
        if (StringUtils.isNotBlank(tipo)) {
            params.put(Constantes.CONTAINSKEY_REPORTE, true);
            params = asociadoDao.lista(params);
            try {
                generaReporte(tipo, (List<Asociado>) params.get(Constantes.CONTAINSKEY_ASOCIADOS), response, Constantes.CONTAINSKEY_ASOCIADOS, Constantes.ASO, null);
                return null;
            } catch (ReporteException e) {
                log.error("No se pudo generar el reporte", e);
                params.remove(Constantes.CONTAINSKEY_REPORTE);
                //errors.reject("error.generar.reporte");
            }
        }
        if (StringUtils.isNotBlank(correo)) {
            params.put(Constantes.CONTAINSKEY_REPORTE, true);
            params = asociadoDao.lista(params);
            params.remove(Constantes.CONTAINSKEY_REPORTE);
            try {
                enviaCorreo(correo, (List<Asociado>) params.get(Constantes.CONTAINSKEY_ASOCIADOS), request, Constantes.CONTAINSKEY_ASOCIADOS, Constantes.ASO, null);
                modelo.addAttribute(Constantes.CONTAINSKEY_MESSAGE, "lista.enviada.message");
                modelo.addAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{messageSource.getMessage("asociado.lista.label", null, request.getLocale()), ambiente.obtieneUsuario().getUsername()});
            } catch (ReporteException e) {
                log.error("No se pudo enviar el reporte por correo", e);
            }
        }

        params = asociadoDao.lista(params);
        modelo.addAttribute(Constantes.CONTAINSKEY_ASOCIADOS, params.get(Constantes.CONTAINSKEY_ASOCIADOS));

        this.pagina(params, modelo, Constantes.CONTAINSKEY_ASOCIADOS, pagina);

        return Constantes.PATH_ASOCIADO_LISTA;
    }

    @RequestMapping("/nuevo")
    public String nuevo(Model modelo) {
        log.debug("Nuevo asociado");
        Asociado asociado = new Asociado();
        modelo.addAttribute(Constantes.ADDATTRIBUTE_ASOCIADO, asociado);
        return Constantes.PATH_ASOCIADO_NUEVO;
    }

    @Transactional
    @RequestMapping(value = "/crear", method = RequestMethod.POST)
    public String crea(HttpServletRequest request, HttpServletResponse response, @Valid Asociado asociado, @Valid Usuario usuario, BindingResult bindingResult, Errors errors, Model modelo, RedirectAttributes redirectAttributes) {
        for (String nombre : request.getParameterMap().keySet()) {
            log.debug("Param: {} : {}", nombre, request.getParameterMap().get(nombre));
        }
        if (bindingResult.hasErrors()) {
            log.debug("Hubo algun error en la forma, regresando");
            return Constantes.PATH_ASOCIADO_NUEVO;
        }
        try {
            asociado = new Asociado("test@test.com", "test", "test", "test", "test", 
                   Constantes.STATUS_ACTIVO, Constantes.CLAVE, Constantes.TELEFONO,Constantes.CALLE,Constantes.COLONIA,
                   Constantes.MUNICIPIO);
            Long asociacionId = (Long)request.getSession().getAttribute("asociacionId");
//           
              
            usuario = usuarioDao.crea(asociado, asociacionId,  new String[]{"ROLE_ASO"});
        } catch(ConstraintViolationException e) {
            log.error("No se pudo crear al asociado", e);
            return Constantes.PATH_ASOCIADO_NUEVO;
//                (Exception e){
//            e.printStackTrace();
            
        }

        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE, "asociado.creado.message");
        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{asociado.getColonia()});

        return "redirect:" + Constantes.PATH_ASOCIADO_VER + "/" + asociado.getId();
    }

    @RequestMapping("/ver/{id}")
    public String ver(@PathVariable Long id, Model modelo) {

        log.debug("Mostrando asociado {}", id);
        Asociado asociados = asociadoDao.obtiene(id);

        modelo.addAttribute(Constantes.ADDATTRIBUTE_ASOCIADO, asociados);

        return Constantes.PATH_ASOCIADO_VER;
    }

    @RequestMapping("/edita/{id}")
    public String edita(@PathVariable Long id, Model modelo) {

        log.debug("Edita Asociado {}", id);
        Asociado asociados = asociadoDao.obtiene(id);
        modelo.addAttribute(Constantes.ADDATTRIBUTE_ASOCIADO, asociados);
        return Constantes.PATH_ASOCIADO_EDITA;
    }

    @Transactional
    @RequestMapping(value = "/actualiza", method = RequestMethod.POST)
    public String actualiza(HttpServletRequest request, @Valid Asociado asociados, BindingResult bindingResult, Errors errors, Model modelo, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            log.error("Hubo algun error en la forma, regresando");
            return Constantes.PATH_ASOCIADO_EDITA;
        }
        try {
            asociados = asociadoDao.actualiza(asociados);
        } catch (ConstraintViolationException e) {
            log.error("No se pudo crear al Asociacion", e);
            return Constantes.PATH_ASOCIADO_NUEVO;
        }

        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE, "asociado.actualizado.message");
        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{asociados.getColonia()});

        return "redirect:" + Constantes.PATH_ASOCIADO_VER + "/" + asociados.getId();
    }

    @Transactional
    @RequestMapping(value = "/elimina", method = RequestMethod.POST)
    public String elimina(HttpServletRequest request, @RequestParam Long id, Model modelo, @ModelAttribute Asociado asociados, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        log.debug("Elimina Asociacion");
        try {
            String nombre = asociadoDao.elimina(id);

            redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE, "asociado.eliminado.message");
            redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{nombre});
        } catch (Exception e) {
            log.error("No se pudo eliminar el asociado " + id, e);
            bindingResult.addError(new ObjectError(Constantes.CONTAINSKEY_ASOCIADOS, new String[]{"asociado.no.eliminado.message"}, null, null));
            return Constantes.PATH_ASOCIADO_VER;
        }

        return "redirect:" + Constantes.PATH_ASOCIADO;
    }
}
