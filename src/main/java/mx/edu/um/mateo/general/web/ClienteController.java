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
import mx.edu.um.mateo.general.dao.ClienteDao;
import mx.edu.um.mateo.general.model.Cliente;
import mx.edu.um.mateo.general.model.Union;
import mx.edu.um.mateo.general.utils.ReporteException;
import org.apache.commons.lang.StringUtils;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author lobo4
 */
@Controller
@RequestMapping(Constantes.PATH_CLIENTE)
public class ClienteController extends BaseController {

    @Autowired
    private ClienteDao clienteDao;

    public ClienteController() {
        log.info("Se ha creado una nueva instancia de ClienteController");
    }

    @RequestMapping
    public String lista(HttpServletRequest request, HttpServletResponse response,
            @RequestParam(required = false) String filtro,
            @RequestParam(required = false) Long pagina,
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) String correo,
            @RequestParam(required = false) String order,
            @RequestParam(required = false) String sort,
            Model modelo) {
        log.debug("Mostrando lista de Clientees");
        Map<String, Object> params = new HashMap<>();
        Long unionId = ((Union) request.getSession().getAttribute(Constantes.UNION_ID)).getId();
        params.put(Constantes.ADDATTRIBUTE_UNION, unionId);
        if (StringUtils.isNotBlank(filtro)) {
            params.put(Constantes.CONTAINSKEY_FILTRO, filtro);
        }
        if (StringUtils.isNotBlank(order)) {
            params.put(Constantes.CONTAINSKEY_ORDER, order);
            params.put(Constantes.CONTAINSKEY_SORT, sort);
        }

        if (StringUtils.isNotBlank(tipo)) {
            params.put(Constantes.CONTAINSKEY_REPORTE, true);
            params = clienteDao.lista(params);
            try {
                generaReporte(tipo, (List<Cliente>) params.get(Constantes.CONTAINSKEY_CLIENTES), response, Constantes.CONTAINSKEY_CLIENTES, Constantes.UNI, null);
                return null;
            } catch (ReporteException e) {
                log.error("No se pudo generar el reporte", e);
                params.remove(Constantes.CONTAINSKEY_REPORTE);
                //errors.reject("error.generar.reporte");
            }
        }


        if (StringUtils.isNotBlank(correo)) {
            params.put(Constantes.CONTAINSKEY_REPORTE, true);
            params = clienteDao.lista(params);

            params.remove(Constantes.CONTAINSKEY_REPORTE);
            try {
                enviaCorreo(correo, (List<Cliente>) params.get(Constantes.CONTAINSKEY_CLIENTES), request, Constantes.CONTAINSKEY_CLIENTES, Constantes.UNI, null);
                modelo.addAttribute(Constantes.CONTAINSKEY_MESSAGE, "lista.enviada.message");
                modelo.addAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{messageSource.getMessage("cliente.lista.label", null, request.getLocale()), ambiente.obtieneUsuario().getUsername()});
            } catch (ReporteException e) {
                log.error("No se pudo enviar el reporte por correo", e);
            }
        }
        params = clienteDao.lista(params);
        modelo.addAttribute(Constantes.CONTAINSKEY_CLIENTES, params.get(Constantes.CONTAINSKEY_CLIENTES));

        this.pagina(params, modelo, Constantes.CONTAINSKEY_CLIENTES, pagina);

        return Constantes.PATH_CLIENTE_LISTA;
    }

    @RequestMapping("/ver/{id}")
    public String ver(@PathVariable Long id, Model modelo) {
        log.debug("Mostrando Cliente {}", id);
        Cliente cliente = clienteDao.obtiene(id);

        modelo.addAttribute(Constantes.ADDATTRIBUTE_CLIENTE, cliente);

        return Constantes.PATH_CLIENTE_VER;
    }

    @RequestMapping("/nueva")
    public String nuevo(Model modelo) {
        log.debug("Nueva Cliente");
        Cliente cliente = new Cliente();
        modelo.addAttribute(Constantes.ADDATTRIBUTE_CLIENTE, cliente);
        return Constantes.PATH_CLIENTE_NUEVO;
    }

    @RequestMapping(value = "/crea", method = RequestMethod.POST)
    public String crea(HttpServletRequest request, HttpServletResponse response, @Valid Cliente cliente, BindingResult bindingResult, Errors errors, Model modelo, RedirectAttributes redirectAttributes) {
        for (String nombre : request.getParameterMap().keySet()) {
            log.debug("Param: {} : {}", nombre, request.getParameterMap().get(nombre));
        }
        if (bindingResult.hasErrors()) {
            log.debug("Hubo algun error en la forma, regresando");
            for (ObjectError error : bindingResult.getAllErrors()) {
                log.debug("Error: {}", error);
            }
            return Constantes.PATH_CLIENTE_NUEVO;
        }
        try {
            cliente = clienteDao.crea(cliente);
        } catch (ConstraintViolationException e) {
            log.error("No se pudo crear el cliente", e);
            return Constantes.PATH_CLIENTE_NUEVO;
        }

        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE, "cliente.creado.message");
        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{cliente.getNombre()});

        return "redirect:" + Constantes.PATH_CLIENTE_VER + "/" + cliente.getId();
    }

    @RequestMapping("/edita/{id}")
    public String edita(@PathVariable Long id, Model modelo) {
        log.debug("Edita Cliente {}", id);
        Cliente cliente = clienteDao.obtiene(id);
        modelo.addAttribute(Constantes.ADDATTRIBUTE_CLIENTE, cliente);

        return Constantes.PATH_CLIENTE_EDITA;
    }

    @RequestMapping(value = "/actualiza", method = RequestMethod.POST)
    public String actualiza(HttpServletRequest request, @Valid Cliente cliente, BindingResult bindingResult, Errors errors, Model modelo, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            log.error("Hubo algun error en la forma, regresando");
            for (ObjectError error : bindingResult.getAllErrors()) {
                log.debug("Error: {}", error);
            }
            return Constantes.PATH_CLIENTE_EDITA;
        }
        try {
            cliente = clienteDao.actualiza(cliente);
        } catch (ConstraintViolationException e) {
            log.error("No se pudo crear el cliente", e);
            return Constantes.PATH_CLIENTE_NUEVO;
        }


        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE, "cliente.actualizado.message");
        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{cliente.getNombre()});

        return "redirect:" + Constantes.PATH_CLIENTE_VER + "/" + cliente.getId();
    }

    @RequestMapping(value = "/elimina", method = RequestMethod.POST)
    public String elimina(HttpServletRequest request, @RequestParam Long id, Model modelo, @ModelAttribute Cliente Cliente, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        log.debug("Elimina Cliente");
        try {

            String nombre = clienteDao.elimina(id);

            redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE, "cliente.eliminado.message");
            redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{nombre});
        } catch (Exception e) {
            log.error("No se pudo eliminar la Cliente " + id, e);
            bindingResult.addError(new ObjectError(Constantes.ADDATTRIBUTE_CLIENTE, new String[]{"cliente.no.eliminado.message"}, null, null));
            return Constantes.PATH_CLIENTE_VER;
        }

        return "redirect:" + Constantes.PATH_CLIENTE;
    }
}
