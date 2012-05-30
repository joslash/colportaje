/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.general.web;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import mx.edu.um.mateo.general.dao.AsociacionDao;
import mx.edu.um.mateo.general.dao.ColportorDao;
import mx.edu.um.mateo.general.model.Asociacion;
import mx.edu.um.mateo.general.model.Colportor;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.utils.Ambiente;
import mx.edu.um.mateo.general.utils.ReporteException;
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
import org.springframework.security.crypto.keygen.KeyGenerators;
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
@RequestMapping(Constantes.PATH_COLPORTOR)
public class ColportorController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(ColportorController.class);
    @Autowired
    private ColportorDao colportorDao;
    @Autowired
    private AsociacionDao asociacionDao;
    /*
     * DE AQUI @InitBinder public void initBinder(WebDataBinder binder) {
     *
     * binder.registerCustomEditor(TipoColportor.class, new
     * EnumEditor(TipoColportor.class)); }
     */

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
            params = colportorDao.lista(params);
            try {
                generaReporte(tipo, (List<Colportor>) params.get(Constantes.CONTAINSKEY_COLPORTORES), response, Constantes.CONTAINSKEY_COLPORTORES, Constantes.ASO, null);
                return null;
            } catch (ReporteException e) {
                log.error("No se pudo generar el reporte", e);
                params.remove(Constantes.CONTAINSKEY_REPORTE);
                //errors.reject("error.generar.reporte");
            }
        }
        if (StringUtils.isNotBlank(correo)) {
            params.put(Constantes.CONTAINSKEY_REPORTE, true);
            params = colportorDao.lista(params);
            params.remove(Constantes.CONTAINSKEY_REPORTE);
            try {
                enviaCorreo(correo, (List<Colportor>) params.get(Constantes.CONTAINSKEY_COLPORTORES), request, Constantes.CONTAINSKEY_COLPORTORES, Constantes.ASO, null);
                modelo.addAttribute(Constantes.CONTAINSKEY_MESSAGE, "lista.enviada.message");
                modelo.addAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{messageSource.getMessage("asociado.lista.label", null, request.getLocale()), ambiente.obtieneUsuario().getUsername()});
            } catch (ReporteException e) {
                log.error("No se pudo enviar el reporte por correo", e);
            }
        }

        params = colportorDao.lista(params);
        modelo.addAttribute(Constantes.CONTAINSKEY_COLPORTORES, params.get(Constantes.CONTAINSKEY_COLPORTORES));
        this.pagina(params, modelo, Constantes.CONTAINSKEY_COLPORTORES, pagina);

        return Constantes.PATH_COLPORTOR_LISTA;
    }

    @RequestMapping("/ver/{id}")
    public String ver(@PathVariable Long id, Model modelo) {
        log.debug("Mostrando colportor {}", id);
        Colportor colportores = colportorDao.obtiene(id);

        modelo.addAttribute(Constantes.ADDATTRIBUTE_COLPORTOR, colportores);

        return Constantes.PATH_COLPORTOR_VER;
    }

    @RequestMapping("/nuevo")
    public String nuevo(Model modelo) {
        log.debug("Nuevo colportor");
        Colportor colportores = new Colportor();
        modelo.addAttribute(Constantes.ADDATTRIBUTE_COLPORTOR, colportores);
        return Constantes.PATH_COLPORTOR_NUEVO;
    }
/**
 * TODO 
 * @param request
 * @param response
 * @param colportores
 * @param bindingResult
 * @param errors
 * @param modelo
 * @param redirectAttributes
 * @return
 * @throws ParseException 
 */
    @Transactional
    @RequestMapping(value = "/crea", method = RequestMethod.POST)
    public String crea(HttpServletRequest request, HttpServletResponse response, @Valid Colportor colportores, BindingResult bindingResult, Errors errors, Model modelo, RedirectAttributes redirectAttributes) throws ParseException {
        for (String nombre : request.getParameterMap().keySet()) {
            log.debug("Param: {} : {}", nombre, request.getParameterMap().get(nombre));
        }
        if (bindingResult.hasErrors()) {
            log.debug("Hubo algun error en la forma, regresando "+bindingResult.getFieldErrors());
            return Constantes.PATH_COLPORTOR_NUEVO;
        }
        try{
            switch (colportores.getTipoDeColportor()) {
                case "0":
                    colportores.setTipoDeColportor(Constantes.TIEMPO_COMPLETO);
                    break;
                case "1":
                    colportores.setTipoDeColportor(Constantes.TIEMPO_PARCIAL);
                    break;
                case "2":
                    colportores.setTipoDeColportor(Constantes.ESTUDIANTE);
                    break;

            }
        }catch(Exception e){
            e.printStackTrace();
            return Constantes.PATH_COLPORTOR_NUEVO;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(Constantes.DATE_SHORT_HUMAN_PATTERN);
            colportores.setFechaDeNacimiento(sdf.parse(request.getParameter("fechaDeNacimiento")));
        } catch (ParseException e) {
            log.error("FechaDeNacimiento", e);
            return Constantes.PATH_COLPORTOR_NUEVO;
        }
String password = null;
 password = KeyGenerators.string().generateKey();
        
        try {
            colportores.setAsociacion(asociacionDao.obtiene((Long) request.getSession().getAttribute("asociacionId")));
            colportores.setPassword(password);
            colportores = colportorDao.crea(colportores);
            
        
//        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE, "colportor.creado.message");
//        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{colportores.getColonia()});
        modelo.addAttribute("colportor", colportores);
        return "redirect:" + Constantes.PATH_COLPORTOR_VER + "/" + colportores.getId();
        } catch (Exception e) {
            log.error("No se pudo crear la colportor", e);
            return Constantes.PATH_COLPORTOR_NUEVO;
        }
        
    }

    @RequestMapping("/edita/{id}")
    public String edita(@PathVariable Long id, Model modelo) {
        log.debug("Editar colportor {}", id);
        Colportor colportores = colportorDao.obtiene(id);
        modelo.addAttribute(Constantes.ADDATTRIBUTE_COLPORTOR, colportores);
        return Constantes.PATH_COLPORTOR_EDITA;
    }

    @Transactional
    @RequestMapping(value = "/actualiza", method = RequestMethod.POST)
    public String actualiza(HttpServletRequest request, @Valid Colportor colportores, BindingResult bindingResult, Errors errors, Model modelo, RedirectAttributes redirectAttributes) throws ParseException {
        if (bindingResult.hasErrors()) {
            log.error("Hubo algun error en la forma, regresando");
            return Constantes.PATH_COLPORTOR_EDITA;
        }
        switch (colportores.getTipoDeColportor()) {
            case "0":
                colportores.setTipoDeColportor(Constantes.TIEMPO_COMPLETO);
                break;
            case "1":
                colportores.setTipoDeColportor(Constantes.TIEMPO_PARCIAL);
                break;
            case "2":
                colportores.setTipoDeColportor(Constantes.ESTUDIANTE);
                break;

        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(Constantes.DATE_SHORT_HUMAN_PATTERN);
            colportores.setFechaDeNacimiento(sdf.parse(request.getParameter("fechaDeNacimiento")));
        } catch (ConstraintViolationException e) {
            log.error("FechaDeNacimiento", e);
            return Constantes.PATH_COLPORTOR_NUEVO;
        }

        try {
            log.debug("Colportor FechaDeNacimiento" + colportores.getFechaDeNacimiento());
            colportores = colportorDao.actualiza(colportores);
        } catch (ConstraintViolationException e) {
            log.error("No se pudo crear la colportor", e);
            return Constantes.PATH_COLPORTOR_NUEVO;
        }

        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE, "colportor.actualizado.message");
        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{colportores.getColonia()});

        return "redirect:" + Constantes.PATH_COLPORTOR_VER + "/" + colportores.getId();
    }

    @Transactional
    @RequestMapping(value = "/elimina", method = RequestMethod.POST)
    public String elimina(HttpServletRequest request, @RequestParam Long id, Model modelo, @ModelAttribute Colportor colportores, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        log.debug("Elimina colportor");
        try {
            String nombre = colportorDao.elimina(id);

            redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE, "colportor.eliminado.message");
            redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{nombre});
        } catch (Exception e) {
            log.error("No se pudo eliminar el colportor " + id, e);
            bindingResult.addError(new ObjectError(Constantes.ADDATTRIBUTE_COLPORTOR, new String[]{"colportor.no.eliminado.message"}, null, null));
            return Constantes.PATH_COLPORTOR_VER;
        }

        return "redirect:" + Constantes.PATH_COLPORTOR;
    }

    private void generaReporte(String tipo, List<Colportor> colportores, HttpServletResponse response) throws JRException, IOException {
        log.debug("Generando reporte {}", tipo);
        byte[] archivo = null;
        switch (tipo) {
            case "PDF":
                archivo = generaPdf(colportores);
                response.setContentType("application/pdf");
                response.addHeader("Content-Disposition", "attachment; filename=Colportores.pdf");
                break;
            case "CSV":
                archivo = generaCsv(colportores);
                response.setContentType("text/csv");
                response.addHeader("Content-Disposition", "attachment; filename=Colportores.csv");
                break;
            case "XLS":
                archivo = generaXls(colportores);
                response.setContentType("application/vnd.ms-excel");
                response.addHeader("Content-Disposition", "attachment; filename=Colportores.xls");
        }
        if (archivo != null) {
            response.setContentLength(archivo.length);
            try (BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream())) {
                bos.write(archivo);
                bos.flush();
            }
        }

    }

    private void enviaCorreo(String tipo, List<Colportor> colportores, HttpServletRequest request) throws JRException, MessagingException {
        log.debug("Enviando correo {}", tipo);
        byte[] archivo = null;
        String tipoContenido = null;
        switch (tipo) {
            case "PDF":
                archivo = generaPdf(colportores);
                tipoContenido = "application/pdf";
                break;
            case "CSV":
                archivo = generaCsv(colportores);
                tipoContenido = "text/csv";
                break;
            case "XLS":
                archivo = generaXls(colportores);
                tipoContenido = "application/vnd.ms-excel";
        }

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(ambiente.obtieneUsuario().getUsername());
        String titulo = messageSource.getMessage("colportor.lista.label", null, request.getLocale());
        helper.setSubject(messageSource.getMessage("envia.correo.titulo.message", new String[]{titulo}, request.getLocale()));
        helper.setText(messageSource.getMessage("envia.correo.contenido.message", new String[]{titulo}, request.getLocale()), true);
        helper.addAttachment(titulo + "." + tipo, new ByteArrayDataSource(archivo, tipoContenido));
        mailSender.send(message);
    }

    private byte[] generaPdf(List colportores) throws JRException {
        Map<String, Object> params = new HashMap<>();
        JasperDesign jd = JRXmlLoader.load(this.getClass().getResourceAsStream("/mx/edu/um/mateo/general/reportes/colportores.jrxml"));
        JasperReport jasperReport = JasperCompileManager.compileReport(jd);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(colportores));
        byte[] archivo = JasperExportManager.exportReportToPdf(jasperPrint);

        return archivo;
    }

    private byte[] generaCsv(List colportores) throws JRException {
        Map<String, Object> params = new HashMap<>();
        JRCsvExporter exporter = new JRCsvExporter();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        JasperDesign jd = JRXmlLoader.load(this.getClass().getResourceAsStream("/mx/edu/um/mateo/general/reportes/colportores.jrxml"));
        JasperReport jasperReport = JasperCompileManager.compileReport(jd);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(colportores));
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, byteArrayOutputStream);
        exporter.exportReport();
        byte[] archivo = byteArrayOutputStream.toByteArray();

        return archivo;
    }

    private byte[] generaXls(List colportores) throws JRException {
        Map<String, Object> params = new HashMap<>();
        JRXlsExporter exporter = new JRXlsExporter();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        JasperDesign jd = JRXmlLoader.load(this.getClass().getResourceAsStream("/mx/edu/um/mateo/general/reportes/colportores.jrxml"));
        JasperReport jasperReport = JasperCompileManager.compileReport(jd);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(colportores));
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
