/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.general.web;
import mx.edu.um.mateo.general.dao.ColegioDao;
import mx.edu.um.mateo.general.model.Colegio;
import mx.edu.um.mateo.general.test.BaseTest;
import mx.edu.um.mateo.general.test.GenericWebXmlContextLoader;
import mx.um.edu.mateo.Constantes;
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
public class ColegioControllerTest extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(ColegioControllerTest.class);
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;
    @Autowired
    private ColegioDao colegioDao;

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
    public void debieraMostrarListaDeColegio() throws Exception {
        log.debug("Debiera monstrar lista de colegioes");
        
        for (int i = 0; i < 20; i++) {
            Colegio colegio = new Colegio(Constantes.NOMBRE+i, Constantes.STATUS_ACTIVO);
            colegioDao.crea(colegio);
            assertNotNull(colegio);
        }

        this.mockMvc.perform(get(Constantes.PATH_COLEGIO))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_COLEGIO_LISTA + ".jsp"))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_COLEGIOS))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINACION))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINAS))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINA));
    }

   @Test
    public void debieraMostrarColegio() throws Exception {
        log.debug("Debiera mostrar colegio");
        Colegio colegio = new Colegio(Constantes.NOMBRE, Constantes.STATUS_ACTIVO);
        colegio = colegioDao.crea(colegio);
        assertNotNull(colegio);

        this.mockMvc.perform(get(Constantes.PATH_COLEGIO_VER +"/"+ colegio.getId()))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_COLEGIO_VER + ".jsp"))
                .andExpect(model()
                .attributeExists(Constantes.ADDATTRIBUTE_COLEGIO));
    }

    @Test
    public void debieraCrearColegio() throws Exception {
        log.debug("Debiera crear colegio");

        this.mockMvc.perform(post(Constantes.PATH_COLEGIO_CREA)
                .param("nombre", "test")
                .param("status", Constantes.STATUS_ACTIVO))
                .andExpect(status().isOk())
                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "colegio.creado.message"));
    }

    @Test
    public void debieraActualizarColegio() throws Exception {
        log.debug("Debiera actualizar colegio");
        Colegio colegio = new Colegio(Constantes.NOMBRE, Constantes.STATUS_ACTIVO);
        colegio = colegioDao.crea(colegio);
        assertNotNull(colegio);

        this.mockMvc.perform(post(Constantes.PATH_COLEGIO_ACTUALIZA)
                .param("id",colegio.getId().toString())
                .param("version", colegio.getVersion().toString())
                .param("nombre", "test1")
                .param("status", colegio.getStatus()))
                .andExpect(status().isOk())
                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "colegio.actualizado.message"));
    }

    @Test
    public void debieraEliminarColegio() throws Exception {
        log.debug("Debiera eliminar colegio");
        Colegio colegio = new Colegio(Constantes.NOMBRE, Constantes.STATUS_ACTIVO);
        colegioDao.crea(colegio);
        assertNotNull(colegio);

        this.mockMvc.perform(post(Constantes.PATH_COLEGIO_ELIMINA)
                .param("id", colegio.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                    .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "colegio.eliminado.message"));
    }
}