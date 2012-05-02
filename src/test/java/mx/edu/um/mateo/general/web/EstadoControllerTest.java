/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.general.web;
import mx.edu.um.mateo.Constantes;
import mx.edu.um.mateo.general.dao.EstadoDao;
import mx.edu.um.mateo.general.model.Estado;
import mx.edu.um.mateo.general.test.GenericWebXmlContextLoader;
import org.junit.*;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.server.MockMvc;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.*;
import org.springframework.test.web.server.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
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
public class EstadoControllerTest {
   private static final Logger log = LoggerFactory.getLogger(EstadoControllerTest.class);
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;
    @Autowired
    private EstadoDao estadoDao;
    
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
    public void debieraMostrarListaDeEstado() throws Exception {
        log.debug("Debiera monstrar lista Estado");
        for (int i = 0; i < 20; i++) {
            Estado estado = new Estado("test" + i);
            estadoDao.crea(estado);
            assertNotNull(estado);
        }
        this.mockMvc.perform(get(Constantes.PATH_ESTADO))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_ESTADO_LISTA+ ".jsp"))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_ESTADOS))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINACION))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINAS))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINA));
    }
    @Test
    public void debieraMostrarEstado() throws Exception {
        log.debug("Debiera mostrar  Estado");
        Estado estado = new Estado("test");
        estado = estadoDao.crea(estado);
        assertNotNull(estado);

        this.mockMvc.perform(get(Constantes.PATH_ESTADO_VER +"/"+ estado.getId()))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_ESTADO_VER + ".jsp"))
                .andExpect(model()
                .attributeExists(Constantes.ADDATTRIBUTE_ESTADO));
    }
    @Test
    public void debieraCrearEstado() throws Exception {
        log.debug("Debiera crear Estado");
        this.mockMvc.perform(post(Constantes.PATH_ESTADO_CREA)
                .param("nombre", "test"))
                .andExpect(status().isOk())
                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "estado.creada.message"));
    }
    @Test
    public void debieraActualizarPais() throws Exception {
        log.debug("Debiera actualizar  pais");
        Estado estado = new Estado("test");
        estado = estadoDao.crea(estado);
        assertNotNull(estado);

        this.mockMvc.perform(post(Constantes.PATH_ESTADO_ACTUALIZA)
                .param("id",estado.getId().toString())
                .param("version", estado.getVersion().toString())
                .param("nombre", "test1"))
                .andExpect(status().isOk())
                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "estado.actualizada.message"));
    }
    @Test
    public void debieraEliminarEstado() throws Exception {
        log.debug("Debiera eliminar Estado");
        Estado estado = new Estado("test");
        estadoDao.crea(estado);
        assertNotNull(estado);

        this.mockMvc.perform(post(Constantes.PATH_ESTADO_ELIMINA)
                .param("id", estado.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "estado.eliminada.message"));
    }
}
