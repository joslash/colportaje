/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.general.web;


import mx.edu.um.mateo.general.dao.AsociacionDao;
import mx.edu.um.mateo.general.model.Asociacion;
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
 * @author gibrandemetrioo
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = GenericWebXmlContextLoader.class, locations = {
    "classpath:mateo.xml",
    "classpath:security.xml",
    "classpath:dispatcher-servlet.xml"
})
@Transactional
public class AsociacionControllerTest extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(AsociacionControllerTest.class);
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;
    @Autowired
    private AsociacionDao asociacionDao;

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
    public void debieraMostrarListaDeAsociacion() throws Exception {
        log.debug("Debiera monstrar lista de cuentas de asociacion");
        
        for (int i = 0; i < 20; i++) {
            Asociacion asociacion = new Asociacion("test" + i,Constantes.STATUS_ACTIVO);
            asociacionDao.crea(asociacion);
            assertNotNull(asociacion);
        }

        this.mockMvc.perform(get(Constantes.PATH_ASOCIACION))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_ASOCIACION_LISTA+ ".jsp"))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_ASOCIACIONES))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINACION))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINAS))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINA));
    }

    @Test
    public void debieraMostrarAsociacion() throws Exception {
        log.debug("Debiera mostrar  asociacion");
        Asociacion asociacion = new Asociacion("test", Constantes.STATUS_ACTIVO);
        asociacion = asociacionDao.crea(asociacion);
        assertNotNull(asociacion);

        this.mockMvc.perform(get(Constantes.PATH_ASOCIACION_VER +"/"+ asociacion.getId()))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_ASOCIACION_VER + ".jsp"))
                .andExpect(model()
                .attributeExists(Constantes.ADDATTRIBUTE_ASOCIACION));
    }

    @Test
    public void debieraCrearASociacion() throws Exception {
        log.debug("Debiera crear asociacion");

        this.mockMvc.perform(post(Constantes.PATH_ASOCIACION_CREA)
                .param("nombre", "test")
                .param("status", Constantes.STATUS_ACTIVO))
                .andExpect(status().isOk())
                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "asociacion.creada.message"));
    }

    @Test
    public void debieraActualizarAsociacion() throws Exception {
        log.debug("Debiera actualizar  asociacion");
        Asociacion asociacion = new Asociacion("test", Constantes.STATUS_ACTIVO);
        asociacion = asociacionDao.crea(asociacion);
        assertNotNull(asociacion);

        this.mockMvc.perform(post(Constantes.PATH_ASOCIACION_ACTUALIZA)
                .param("id",asociacion.getId().toString())
                .param("version", asociacion.getVersion().toString())
                .param("nombre", "test1")
                .param("status", asociacion.getStatus()))
                .andExpect(status().isOk())
                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "asociacion.actualizado.message"));
    }

    @Test
    public void debieraEliminarAsociacion() throws Exception {
        log.debug("Debiera eliminar  asociacion");
        Asociacion asociacion = new Asociacion("test",Constantes.STATUS_ACTIVO);
        asociacionDao.crea(asociacion);
        assertNotNull(asociacion);

        this.mockMvc.perform(post(Constantes.PATH_ASOCIACION_ELIMINA)
                .param("id", asociacion.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "asociacion.eliminado.message"));
    }
}