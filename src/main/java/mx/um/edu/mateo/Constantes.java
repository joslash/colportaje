/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.um.edu.mateo;
/**
 *
 * @author gibrandemetrioo
 */
public class Constantes {


    public static final String NOMBRE= "test";
    public static final String STATUS_ACTIVO= "A";
    public static final String CLAVE= "test90";
    public static final String DIRECCION= "testd";
    public static final String CORREO= "test@tes.tst";
    public static final String TELEFONO= "0000000000000";
    
    
    
    
    /**
     * Valores para el constructor de Locale
     */
    public static final String LOCALE_LANGUAGE = "es";
    public static final String LOCALE_COUNTRY = "MX";
    public static final String LOCALE_VARIANT = "Traditional_WIN";
 
    
    /**
     * Formato (yyyy-MM-dd) de la fecha en el cual el mes se representa
     * numericamente.
     */
    public static final String DATE_SHORT_SYSTEM_PATTERN = "yyyy-MM-dd";
    /**
     * Formato (dd/MM/yyyy) de la fecha en el cual el mes se representa
     * numericamente
     */
    public static final String DATE_SHORT_HUMAN_PATTERN = "dd/MM/yyyy";
    /**
     * Formato (dd/MM/yyyy hh:mm) de la fecha en el cual el mes se representa
     * numericamente incluyendo la hora:minutos:segundos am/pm
     */
    public static final String DATE_SHORT_HHMM_HUMAN_PATTERN = "dd/MM/yyyy HH:mm";
    /**
     * Se utiliza para informar al usuario el formato de fecha esperado
     */
    public static final String DATE_SHORT_HUMAN_PATTERN_MSG = "dd/mm/yyyy";
    /**
     * Formato (dd/MMM/yyyy) de la fecha en el cual el mes se representa en
     * palabra
     */
    public static final String DATE_LONG_HUMAN_PATTERN = "dd/MMM/yyyy";
    /**
     * Formato (dd de MMMMM de yyyy) de la fecha en la cual el mes es completo
     */
    public static final String DATE_XLONG_HUMAN_PATTERN = "dd 'de' MMMM 'de' yyyy";
    //public static final String STATUS_ACTIVO = "A";
    public static final String STATUS_INACTIVO = "I";
    public static final String DECIMAL_PATTERN = "###,###,###,##0.00";
    public static final String CURRENCY_PATTERN = "$###,###,##0.00";
    public static final String PERCENTAGE_PATTERN = "#.00%";
    /**
     * Valores para el los containsKey
     */
    public static final String CONTAINSKEY_MAX = "max";
    public static final String CONTAINSKEY_PAGINA = "pagina";
    public static final String CONTAINSKEY_PAGINAS = "paginas";
    public static final String CONTAINSKEY_PAGINACION = "paginacion";
    public static final String CONTAINSKEY_OFFSET = "offset";
    public static final String CONTAINSKEY_FILTRO = "filtro";
    public static final String CONTAINSKEY_ORDER = "order";
    public static final String CONTAINSKEY_SORT = "sort";
    public static final String CONTAINSKEY_DESC = "desc";
    public static final String CONTAINSKEY_REPORTE = "reporte";
    public static final String CONTAINSKEY_CANTIDAD = "cantidad";
    public static final String CONTAINSKEY_MESSAGE = "message";
    public static final String CONTAINSKEY_MESSAGE_ATTRS = "messageAttrs";
    /**
     * Valores para el los containsKey para las clases
     */
    public static final String CONTAINSKEY_UNIONES = "uniones";
    public static final String CONTAINSKEY_COLPORTORES = "colportores";
    /**
     * Valores para el los addAttribute para las clases
     */
    public static final String ADDATTRIBUTE_UNION = "union";
     public static final String ADDATTRIBUTE_COLPORTOR = "colportor";
    /**
     * Valores para el los path's para las clases
     */
    public static final String PATH_UNION = "/web/union";
    public static final String PATH_UNION_LISTA = "web/union/lista";
    public static final String PATH_UNION_VER = "/web/union/ver";
    public static final String PATH_UNION_NUEVA = "web/union/nueva";
    public static final String PATH_UNION_EDITA = "web/union/edita";
    public static final String PATH_UNION_CREA = "/web/union/crea";
    public static final String PATH_UNION_ACTUALIZA = "/web/union/actualiza";
    public static final String PATH_UNION_ELIMINA = "/web/union/elimina";


    public static final String PATH_COLPORTOR = "/web/colportor";
    public static final String PATH_COLPORTOR_LISTA = "web/colportor/lista";
    public static final String PATH_COLPORTOR_VER = "/web/colportor/ver";
    public static final String PATH_COLPORTOR_NUEVO = "web/colportor/nuevo";
    public static final String PATH_COLPORTOR_EDITA = "web/colportor/edita";
    public static final String PATH_COLPORTOR_CREA = "/web/colportor/crea";
    public static final String PATH_COLPORTOR_ACTUALIZA = "/web/colportor/actualiza";
    public static final String PATH_COLPORTOR_ELIMINA = "/web/colportor/elimina";


    /**
     * Valores para el los containsKey para las clases
     */
    public static final String CONTAINSKEY_ASOCIACIONES = "asociaciones";
    public static final String CONTAINSKEY_ASOCIADOS = "asociados";
    public static final String CONTAINSKEY_TEMPORADAS = "temporadas";
    /**
     * Valores para el los addAttribute para las clases
     */
    public static final String ADDATTRIBUTE_ASOCIACION = "asociacion";
    public static final String ADDATTRIBUTE_ASOCIADO = "asociado";
    public static final String ADDATTRIBUTE_TEMPORADA = "temporada";
    /**
     * Valores para el los path's para las clases
     */
    public static final String PATH_ASOCIACION = "/web/asociacion";
    public static final String PATH_ASOCIACION_LISTA = "/web/asociacion/lista";
    public static final String PATH_ASOCIACION_VER = "/web/asociacion/ver";
    public static final String PATH_ASOCIACION_NUEVA = "/web/asociacion/nueva";
    public static final String PATH_ASOCIACION_EDITA = "/web/asociacion/edita";
    public static final String PATH_ASOCIACION_CREA = "/web/asociacion/crea";
    public static final String PATH_ASOCIACION_ACTUALIZA = "/web/asociacion/actualiza";
    public static final String PATH_ASOCIACION_ELIMINA = "/web/asociacion/elimina";  
    
    public static final String PATH_ASOCIADO = "/web/asociado";
    public static final String PATH_ASOCIADO_LISTA = "/web/asociado/lista";
    public static final String PATH_ASOCIADO_VER = "/web/asociado/ver";
    public static final String PATH_ASOCIADO_NUEVA = "/web/asociado/nueva";
    public static final String PATH_ASOCIADO_EDITA = "/web/asociado/edita";
    public static final String PATH_ASOCIADO_CREA = "/web/asociado/crea";
    public static final String PATH_ASOCIADO_ACTUALIZA = "/web/asociado/actualiza";
    public static final String PATH_ASOCIADO_ELIMINA = "/web/asociado/elimina";        

    public static final String PATH_TEMPORADA = "/web/temporada";
    public static final String PATH_TEMPORADA_LISTA = "/web/temporada/lista";
    public static final String PATH_TEMPORADA_VER = "/web/temporada/ver";
    public static final String PATH_TEMPORADA_NUEVA = "/web/temporada/nueva";
    public static final String PATH_TEMPORADA_EDITA = "/web/temporada/edita";
    public static final String PATH_TEMPORADA_CREA = "/web/temporada/crea";
    public static final String PATH_TEMPORADA_ACTUALIZA = "/web/temporada/actualiza";
    public static final String PATH_TEMPORADA_ELIMINA = "/web/temporada/elimina";  
}


