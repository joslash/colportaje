/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.general.web;

/**
 * TODO 
 */

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import mx.edu.um.mateo.Constantes;
import mx.edu.um.mateo.general.dao.ColportorDao;
import mx.edu.um.mateo.general.model.*;
import mx.edu.um.mateo.general.test.GenericWebXmlContextLoader;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import static org.junit.Assert.assertNotNull;
import org.junit.*;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.server.MockMvc;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.*;
import org.springframework.test.web.server.result.ModelResultMatchers;
import org.springframework.test.web.server.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

/**
 *
 * @author wilbert
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = GenericWebXmlContextLoader.class, locations = {
    "classpath:mateo.xml",
    "classpath:security.xml",
    "classpath:dispatcher-servlet.xml"
})
@Transactional
public class ColportorControllerTest {

    private static final Logger log = LoggerFactory.getLogger(ColportorControllerTest.class);
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;
    @Autowired
    private ColportorDao colportorDao;
    @Autowired
    private SessionFactory sessionFactory;

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webApplicationContextSetup(wac).build();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void debieraMostrarListaDeColportor() throws Exception {
        log.debug("Debiera monstrar lista de colportores");
        Union union = new Union("test");
        union.setStatus(Constantes.STATUS_ACTIVO);
        currentSession().save(union);
        Rol rol = new Rol("ROLE_COL");
        currentSession().save(rol);
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        Asociacion asociacion = new Asociacion("TEST01", Constantes.STATUS_ACTIVO, union);
        currentSession().save(asociacion);
        for (int i = 0; i < 20; i++) {
            Colportor colportor = new Colportor("test" + i + "@test.com", "test", "test", "test", "test", "test", Constantes.STATUS_ACTIVO,
                    "8262652626", "test", "test", "10706" + i, "test", "test001", new Date());
            colportor.setAsociacion(asociacion);
            currentSession().save(colportor);
            assertNotNull(colportor.getId());
        }

        this.mockMvc.perform(get(Constantes.PATH_COLPORTOR)
                .param("asociacion", asociacion.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_COLPORTOR_LISTA + ".jsp"))
                .andExpect(model()
                .attributeExists(Constantes.CONTAINSKEY_COLPORTORES))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINACION))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINAS))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINA));
               
    }

    @Test
    public void debieraMostrarColportor() throws Exception {
        log.debug("Debiera mostrar colportor");
        Union union = new Union("test");
        union.setStatus(Constantes.STATUS_ACTIVO);
        currentSession().save(union);
        Asociacion asociacion = new Asociacion("TEST01", Constantes.STATUS_ACTIVO, union);
        currentSession().save(asociacion);
        Colportor colportor = new Colportor("test@test.com", "test", "test", "test", "test", "test", Constantes.STATUS_ACTIVO,
                "8262652626", "test", "test", "10706", "test", "test001", new Date());
        colportor.setAsociacion(asociacion);
        currentSession().save(colportor);
        
        
        this.mockMvc.perform(get(Constantes.PATH_COLPORTOR_VER + "/" + colportor.getId()))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_COLPORTOR_VER + ".jsp"))
                .andExpect(model().attributeExists(Constantes.ADDATTRIBUTE_COLPORTOR));
       
    }

    @Test
    public void debieraCrearColportor() throws Exception {
        log.debug("Debiera crear colportor");
        Union union = new Union("test");
        union.setStatus(Constantes.STATUS_ACTIVO);
        currentSession().save(union);
        Rol rol = new Rol("ROLE_COL");
        currentSession().save(rol);
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        Asociacion asociacion = new Asociacion("TEST01", Constantes.STATUS_ACTIVO, union);
        currentSession().save(asociacion);
        
        this.mockMvc.perform(post(Constantes.PATH_COLPORTOR_CREA)
                .sessionAttr("asociacionId", asociacion.getId())
                .param("roles", "ROLE_COL")
                .param("username", "test@test.com")
                .param("password", "test")
                .param("nombre", "test")
                .param("apellidoP", "test")
                .param("apellidoM", "test")
                .param("clave", Constantes.CLAVE)
                .param("status", Constantes.STATUS_ACTIVO)
                .param("telefono", Constantes.TELEFONO)
                .param("calle", Constantes.CALLE)
                .param("colonia", Constantes.COLONIA)
                .param("municipio", Constantes.MUNICIPIO)
                .param("tipoDeColportor", Constantes.TIPO_COLPORTOR)
                .param("matricula", Constantes.MATRICULA)
                .param("fechaDeNacimiento", "05/05/2010"))
                .andExpect(status().isOk())
                //.andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                //.andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "colportor.creado.message"))
                
                .andExpect(model().attributeExists("colportor"));
    }       
////    super(username, password, nombre, apellidoP, apellidoM); 
////       this.clave=clave;
////       this.status=status;
////       this.telefono=telefono;
////       this.calle=calle;
////       this.colonia=colonia;
////       this.municipio=municipio;
////       this.tipoDeColportor=tipoColportor;
////       this.matricula=matricula;
////       this.fechaDeNacimiento=fechaNac;
//    }

    @Test
    public void debieraActualizarColportor() throws Exception {
        log.debug("Debiera actualizar colportor");
       Union union = new Union("test");
        union.setStatus(Constantes.STATUS_ACTIVO);
        currentSession().save(union);
        Asociacion asociacion = new Asociacion("TEST01", Constantes.STATUS_ACTIVO, union);
        currentSession().save(asociacion);
        Colportor colportor = new Colportor("test@test.com", "test", "test", "test", "test", "test", Constantes.STATUS_ACTIVO,
                "8262652626", "test", "test", "10706", "test", "test001", new Date());
          colportor.setAsociacion(asociacion);
        currentSession().save(colportor);
        assertNotNull(colportor);
// apellidoM, apellidoP, asociacion_id,  nombre,  password, username, version,
//        calle, clave, colonia, fecha_nac, matricula, municipio, status, telefono,
//        tipoDeColportor, entity_type
          this.mockMvc.perform(post(Constantes.PATH_COLPORTOR_ACTUALIZA)
                .param("id", colportor.getId().toString()))
                //.andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                //.andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "colportor.actualizado.message"))
                .andExpect(status().isOk());
    }

    @Test
    public void debieraEliminarColportor() throws Exception {
        log.debug("Debiera eliminar colportor");
      Union union = new Union("test");
        union.setStatus(Constantes.STATUS_ACTIVO);
        currentSession().save(union);
        Asociacion asociacion = new Asociacion("TEST01", Constantes.STATUS_ACTIVO, union);
        currentSession().save(asociacion);
        Colportor colportor = new Colportor("test@test.com", "test", "test", "test", "test", "test", Constantes.STATUS_ACTIVO,
                "8262652626", "test", "test", "10706", "test", "test001", new Date());
        colportor.setAsociacion(asociacion);
        currentSession().save(colportor);
        assertNotNull(colportor);

        this.mockMvc.perform(post(Constantes.PATH_COLPORTOR_ELIMINA)
                .param("id", colportor.getId().toString()))
                .andExpect(status().isOk()).andExpect(flash()
                .attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash()
                .attribute(Constantes.CONTAINSKEY_MESSAGE, "colportor.eliminado.message"));
        
    }
}