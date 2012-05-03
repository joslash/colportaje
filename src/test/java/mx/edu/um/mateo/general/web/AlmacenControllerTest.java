/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.general.web;
import mx.edu.um.mateo.Constantes;
import mx.edu.um.mateo.general.dao.AlmacenDao;
import mx.edu.um.mateo.general.model.Almacen;
import mx.edu.um.mateo.general.model.Asociacion;
import mx.edu.um.mateo.general.model.Asociado;
import mx.edu.um.mateo.general.model.Union;
import mx.edu.um.mateo.general.test.BaseTest;
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
public class AlmacenControllerTest extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(AlmacenControllerTest.class);
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;
    @Autowired
    private AlmacenDao almacenDao;
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
    public void debieraMostrarListaDeAlmacen() throws Exception {
        log.debug("Debiera monstrar lista de almacenes");
        
        for (int i = 0; i < 20; i++) {
            Almacen almacen = new Almacen(Constantes.CLAVE+i, Constantes.NOMBRE);
            almacenDao.crea(almacen);
            assertNotNull(almacen);
        }

        this.mockMvc.perform(get(Constantes.PATH_ALMACEN))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_ALMACEN_LISTA + ".jsp"))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_ALMACENES))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINACION))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINAS))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINA));
    }

   @Test
    public void debieraMostrarAlmacen() throws Exception {
        log.debug("Debiera mostrar almacen");
        Almacen almacen = new Almacen(Constantes.CLAVE, Constantes.NOMBRE);
        almacen = almacenDao.crea(almacen);
        assertNotNull(almacen);

        this.mockMvc.perform(get(Constantes.PATH_ALMACEN_VER +"/"+ almacen.getId()))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_ALMACEN_VER + ".jsp"))
                .andExpect(model()
                .attributeExists(Constantes.ADDATTRIBUTE_ALMACEN));
    }

    @Test
    public void debieraCrearAlmacen() throws Exception {
        log.debug("Debiera crear almacen");
            Union union = new Union("test");
        union.setStatus(Constantes.STATUS_ACTIVO);
        currentSession().save(union);
           Asociacion test2 = new Asociacion("test", Constantes.STATUS_ACTIVO, union);
        currentSession().save(test2);

        Asociacion asociacion = new Asociacion("test", Constantes.STATUS_ACTIVO, union);
        currentSession().save(asociacion);
        
        this.mockMvc.perform(post(Constantes.PATH_ALMACEN_CREA)
                .param("clave", Constantes.CLAVE)
                .param("nombre", Constantes.NOMBRE)
                .param("asociacion", asociacion.getId().toString()))
                .andExpect(status().isOk());
             //   .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
               // .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "almacen.creado.message"));
    }

    @Test
    public void debieraActualizarAlmacen() throws Exception {
        log.debug("Debiera actualizar almacen");
        Almacen almacen = new Almacen(Constantes.NOMBRE, Constantes.STATUS_ACTIVO);
         Union union = new Union("test");
        union.setStatus(Constantes.STATUS_ACTIVO);
        currentSession().save(union);
           Asociacion test2 = new Asociacion("test", Constantes.STATUS_ACTIVO, union);
        currentSession().save(test2);

        Asociacion asociacion = new Asociacion("test", Constantes.STATUS_ACTIVO, union);
        currentSession().save(asociacion);
        almacen = almacenDao.crea(almacen);
        assertNotNull(almacen);

        this.mockMvc.perform(post(Constantes.PATH_ALMACEN_ACTUALIZA)
                .param("id",almacen.getId().toString())
                .param("version", almacen.getVersion().toString())
                .param("clave", "test1")
                .param("nombre", almacen.getNombre())
                 .param("asociacion", asociacion.getId().toString()))
                .andExpect(status().isOk());
                //.andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                //.andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "almacen.actualizado.message"));
    }

    @Test
    public void debieraEliminarAlmacen() throws Exception {
        log.debug("Debiera eliminar almacen");
        Almacen almacen = new Almacen(Constantes.NOMBRE, Constantes.STATUS_ACTIVO);
        almacenDao.crea(almacen);
        assertNotNull(almacen);

        this.mockMvc.perform(post(Constantes.PATH_ALMACEN_ELIMINA)
                .param("id", almacen.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                    .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "almacen.eliminado.message"));
    }
}