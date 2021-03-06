/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.general.web;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import mx.edu.um.mateo.Constantes;
import mx.edu.um.mateo.general.dao.AlmacenDao;
import mx.edu.um.mateo.general.dao.AsociacionDao;
import mx.edu.um.mateo.general.dao.AsociadoDao;
import mx.edu.um.mateo.general.model.Almacen;
import mx.edu.um.mateo.general.model.Asociacion;
import mx.edu.um.mateo.general.model.Asociado;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.utils.Ambiente;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.apache.commons.lang.StringUtils;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
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
 * @author wilbert
 */
@Controller
@RequestMapping(Constantes.PATH_ALMACEN)
public class AlmacenController {
    
    private static final Logger log = LoggerFactory.getLogger(AlmacenController.class);
    @Autowired
    private AlmacenDao AlmacenDao;
    @Autowired
    private AsociacionDao asociacionDao;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private ResourceBundleMessageSource messageSource;
    @Autowired
    private Ambiente ambiente;
    
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
        log.debug("Mostrando lista de almacenes");
        Map<String, Object> params = new HashMap<>();
        if (StringUtils.isNotBlank(filtro)) {
            params.put(Constantes.CONTAINSKEY_FILTRO, filtro);
}
        if (pagina != null) {
            params.put(Constantes.CONTAINSKEY_PAGINA, pagina);
            modelo.addAttribute(Constantes.CONTAINSKEY_PAGINA, pagina);
        } else {
            pagina = 1L;
            modelo.addAttribute(Constantes.CONTAINSKEY_PAGINA, pagina);
        }
        if (StringUtils.isNotBlank(order)) {
            params.put(Constantes.CONTAINSKEY_ORDER, order);
            params.put(Constantes.CONTAINSKEY_SORT, sort);
        }
        
        if (StringUtils.isNotBlank(tipo)) {
            params.put(Constantes.CONTAINSKEY_REPORTE, true);
            params = AlmacenDao.lista(params);
            try {
                generaReporte(tipo, (List<Almacen>) params.get(Constantes.CONTAINSKEY_ALMACENES), response);
                return null;
            } catch (JRException | IOException e) {
                log.error("No se pudo generar el reporte", e);
                params.remove(Constantes.CONTAINSKEY_REPORTE);
                //errors.reject("error.generar.reporte");
            }
        }
        
        if (StringUtils.isNotBlank(correo)) {
            params.put(Constantes.CONTAINSKEY_REPORTE, true);
            params = AlmacenDao.lista(params);
            
            params.remove(Constantes.CONTAINSKEY_REPORTE);
            try {
                enviaCorreo(correo, (List<Almacen>) params.get(Constantes.CONTAINSKEY_ALMACENES), request);
                modelo.addAttribute(Constantes.CONTAINSKEY_MESSAGE, "lista.enviada.message");
                modelo.addAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{messageSource.getMessage("almacen.lista.label", null, request.getLocale()), ambiente.obtieneUsuario().getUsername()});
            } catch (JRException | MessagingException e) {
                log.error("No se pudo enviar el reporte por correo", e);
            }
        }
        params = AlmacenDao.lista(params);
        modelo.addAttribute(Constantes.CONTAINSKEY_ALMACENES, params.get(Constantes.CONTAINSKEY_ALMACENES));

        // inicia paginado
        Long cantidad = (Long) params.get(Constantes.CONTAINSKEY_CANTIDAD);
        Integer max = (Integer) params.get(Constantes.CONTAINSKEY_MAX);
        Long cantidadDePaginas = cantidad / max;
        List<Long> paginas = new ArrayList<>();
        long i = 1;
        do {
            paginas.add(i);
        } while (i++ < cantidadDePaginas);
        List<Almacen> almacenes = (List<Almacen>) params.get(Constantes.CONTAINSKEY_ALMACENES);
        Long primero = ((pagina - 1) * max) + 1;
        Long ultimo = primero + (almacenes.size() - 1);
        String[] paginacion = new String[]{primero.toString(), ultimo.toString(), cantidad.toString()};
        modelo.addAttribute(Constantes.CONTAINSKEY_PAGINACION, paginacion);
        modelo.addAttribute(Constantes.CONTAINSKEY_PAGINAS, paginas);
        // termina paginado

        return Constantes.PATH_ALMACEN_LISTA;
    }
    
    @RequestMapping("/ver/{id}")
    public String ver(@PathVariable Long id, Model modelo) {
        log.debug("Mostrando almacen {}", id);
        Almacen almacenes = AlmacenDao.obtiene(id);
        
        modelo.addAttribute(Constantes.ADDATTRIBUTE_ALMACEN, almacenes);
        
        return Constantes.PATH_ALMACEN_VER;
    }
    
    @RequestMapping("/nuevo")
    public String nuevo(Model modelo) {
        log.debug("Nuevo almacen");
        Almacen almacenes = new Almacen();
        modelo.addAttribute(Constantes.ADDATTRIBUTE_ALMACEN, almacenes);
        Map<String, Object> asociacion = asociacionDao.lista(null);
        modelo.addAttribute(Constantes.CONTAINSKEY_ASOCIACIONES, asociacion.get(Constantes.CONTAINSKEY_ASOCIACIONES));
        return Constantes.PATH_ALMACEN_NUEVO;
    }
    
    @Transactional
    @RequestMapping(value = "/crea", method = RequestMethod.POST)
    public String crea(HttpServletRequest request, HttpServletResponse response, @Valid Almacen almacenes, BindingResult bindingResult, Errors errors, Model modelo, RedirectAttributes redirectAttributes) {
        for (String nombre : request.getParameterMap().keySet()) {
            log.debug("Param: {} : {}", nombre, request.getParameterMap().get(nombre));
        }
        if (bindingResult.hasErrors()) {
            log.debug("Hubo algun error en la forma, regresando");
            return Constantes.PATH_ALMACEN_NUEVO;
        }
        
        try {
             Asociacion asociacion = asociacionDao.obtiene(almacenes.getAsociacion().getId());
            log.info("asociacion>>>>>>>>>" + asociacion);
           almacenes.setAsociacion(asociacion);
            
            almacenes = AlmacenDao.crea(almacenes);
        } catch (ConstraintViolationException e) {
            log.error("No se pudo crear la almacen", e);
            return Constantes.PATH_ALMACEN_NUEVO;
        }
        
        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE, "almacen.creado.message");
        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{almacenes.getNombre()});
        
        return "redirect:" + Constantes.PATH_ALMACEN_VER + "/" + almacenes.getId();
    }
    
    @RequestMapping("/edita/{id}")
    public String edita(@PathVariable Long id, Model modelo) {
        log.debug("Editar almacen {}", id);
        Almacen almacenes = AlmacenDao.obtiene(id);
        modelo.addAttribute(Constantes.ADDATTRIBUTE_ALMACEN, almacenes);
        
        Map<String, Object> asociacion = asociacionDao.lista(null);
        modelo.addAttribute(Constantes.CONTAINSKEY_ASOCIACIONES, asociacion.get(Constantes.CONTAINSKEY_ASOCIACIONES));
        
        
        return Constantes.PATH_ALMACEN_EDITA;
    }
    
    @Transactional
    @RequestMapping(value = "/actualiza", method = RequestMethod.POST)
    public String actualiza(HttpServletRequest request, @Valid Almacen almacenes, BindingResult bindingResult, Errors errors, Model modelo, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            log.error("Hubo algun error en la forma, regresando");
            return Constantes.PATH_ALMACEN_EDITA;
        }
        try {
            Asociacion asociacion = asociacionDao.obtiene(almacenes.getAsociacion().getId());
 //           almacenes.setAsociacion((Asociacion)request.getSession().getAttribute(Constantes.SESSION_ASOCIACION)); //esta

            almacenes.setAsociacion(asociacion);
            almacenes = AlmacenDao.actualiza(almacenes);
        } catch (ConstraintViolationException e) {
            log.error("No se pudo actualizar el almacen", e);
  //          errors.rejectValue("almacen" , "Almacen no encontrada");                                          //y esta
            return Constantes.PATH_ALMACEN_NUEVO;
        }
        
        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE, "almacen.actualizado.message");
        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{almacenes.getNombre()});
        
        return "redirect:" + Constantes.PATH_ALMACEN_VER + "/" + almacenes.getId();
    }
    
    @Transactional
    @RequestMapping(value = "/elimina", method = RequestMethod.POST)
    public String elimina(HttpServletRequest request, @RequestParam Long id, Model modelo, @ModelAttribute Almacen almacenes, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        log.debug("Elimina almacen");
        try {
            String nombre = AlmacenDao.elimina(id);
            
            redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE, "almacen.eliminado.message");
            redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{nombre});
        } catch (Exception e) {
            log.error("No se pudo eliminar el almacen " + id, e);
            bindingResult.addError(new ObjectError(Constantes.ADDATTRIBUTE_ALMACEN, new String[]{"almacen.no.eliminado.message"}, null, null));
            return Constantes.PATH_ALMACEN_VER;
        }
        
        return "redirect:" + Constantes.PATH_ALMACEN;
    }
    
    private void generaReporte(String tipo, List<Almacen> almacenes, HttpServletResponse response) throws JRException, IOException {
        log.debug("Generando reporte {}", tipo);
        byte[] archivo = null;
        switch (tipo) {
            case Constantes.TIPO_DOCUMENTO_PDF:
                archivo = generaPdf(almacenes);
                response.setContentType("application/pdf");
                response.addHeader("Content-Disposition", "attachment; filename=Almacenes.pdf");
                break;
            case Constantes.TIPO_DOCUMENTO_CSV:
                archivo = generaCsv(almacenes);
                response.setContentType("text/csv");
                response.addHeader("Content-Disposition", "attachment; filename=Almacenes.csv");
                break;
            case Constantes.TIPO_DOCUMENTO_XLS:
                archivo = generaXls(almacenes);
                response.setContentType("application/vnd.ms-excel");
                response.addHeader("Content-Disposition", "attachment; filename=Almacenes.xls");
        }
        if (archivo != null) {
            response.setContentLength(archivo.length);
            try (BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream())) {
                bos.write(archivo);
                bos.flush();
            }
        }
        
    }
    
    private void enviaCorreo(String tipo, List<Almacen> almacenes, HttpServletRequest request) throws JRException, MessagingException {
        log.debug("Enviando correo {}", tipo);
        byte[] archivo = null;
        String tipoContenido = null;
        switch (tipo) {
            case Constantes.TIPO_DOCUMENTO_PDF:
                archivo = generaPdf(almacenes);
                tipoContenido = "application/pdf";
                break;
            case Constantes.TIPO_DOCUMENTO_CSV:
                archivo = generaCsv(almacenes);
                tipoContenido = "text/csv";
                break;
            case Constantes.TIPO_DOCUMENTO_XLS:
                archivo = generaXls(almacenes);
                tipoContenido = "application/vnd.ms-excel";
        }
        
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(ambiente.obtieneUsuario().getUsername());
        String titulo = messageSource.getMessage("almacen.lista.label", null, request.getLocale());
        helper.setSubject(messageSource.getMessage("envia.correo.titulo.message", new String[]{titulo}, request.getLocale()));
        helper.setText(messageSource.getMessage("envia.correo.contenido.message", new String[]{titulo}, request.getLocale()), true);
        helper.addAttachment(titulo + "." + tipo, new ByteArrayDataSource(archivo, tipoContenido));
        mailSender.send(message);
    }
    
    private byte[] generaPdf(List almacenes) throws JRException {
        Map<String, Object> params = new HashMap<>();
        JasperDesign jd = JRXmlLoader.load(this.getClass().getResourceAsStream("/mx/edu/um/mateo/general/reportes/almacenes.jrxml"));
        JasperReport jasperReport = JasperCompileManager.compileReport(jd);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(almacenes));
        byte[] archivo = JasperExportManager.exportReportToPdf(jasperPrint);
        
        return archivo;
    }
    
    private byte[] generaCsv(List almacenes) throws JRException {
        Map<String, Object> params = new HashMap<>();
        JRCsvExporter exporter = new JRCsvExporter();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        JasperDesign jd = JRXmlLoader.load(this.getClass().getResourceAsStream("/mx/edu/um/mateo/general/reportes/almacenes.jrxml"));
        JasperReport jasperReport = JasperCompileManager.compileReport(jd);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(almacenes));
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, byteArrayOutputStream);
        exporter.exportReport();
        byte[] archivo = byteArrayOutputStream.toByteArray();
        
        return archivo;
    }
    
    private byte[] generaXls(List almacenes) throws JRException {
        Map<String, Object> params = new HashMap<>();
        JRXlsExporter exporter = new JRXlsExporter();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        JasperDesign jd = JRXmlLoader.load(this.getClass().getResourceAsStream("/mx/edu/um/mateo/general/reportes/almacenes.jrxml"));
        JasperReport jasperReport = JasperCompileManager.compileReport(jd);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(almacenes));
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, byteArrayOutputStream);
        exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
        exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
        exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS, Boolean.TRUE);
        exporter.setParameter(JRXlsExporterParameter.IS_COLLAPSE_ROW_SPAN, Boolean.TRUE);
        exporter.setParameter(JRXlsExporterParameter.IGNORE_PAGE_MARGINS, Boolean.TRUE);
        exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
        exporter.exportReport();
        byte[] archivo = byteArrayOutputStream.toByteArray();
        
        return archivo;
    }
}
