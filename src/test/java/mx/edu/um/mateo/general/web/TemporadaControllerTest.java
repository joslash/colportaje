/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.general.web;
import java.text.SimpleDateFormat;
import java.util.Date;
import mx.edu.um.mateo.general.test.BaseTest;
import mx.edu.um.mateo.general.test.GenericWebXmlContextLoader;
import mx.edu.um.mateo.general.dao.AsociadoDao;
import mx.edu.um.mateo.general.dao.TemporadaDao;
import mx.edu.um.mateo.general.model.Asociado;
import mx.edu.um.mateo.general.model.Temporada;
import mx.um.edu.mateo.Constantes;
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
import org.springframework.web.context.WebApplicationContext;
import static org.junit.Assert.*;
import org.springframework.transaction.annotation.Transactional;
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
public class TemporadaControllerTest {
    private static final Logger log = LoggerFactory.getLogger(TemporadaControllerTest.class);
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;
    @Autowired
    private TemporadaDao temporadaDao;

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
    public void debieraMostrarListaDeTemporada() throws Exception {
        log.debug("Debiera monstrar lista TEmporada");
        
        for (int i = 0; i < 20; i++) {
            Temporada temporada = new Temporada("test" + i);
            temporadaDao.crea(temporada);
            assertNotNull(temporada);
        }

        this.mockMvc.perform(get(Constantes.PATH_TEMPORADA))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_TEMPORADA_LISTA+ ".jsp"))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_TEMPORADAS))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINACION))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINAS))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINA));
    }
    @Test
    public void debieraMostrarTemporada() throws Exception {
        log.debug("Debiera mostrar  temporada");
        Temporada temporada = new Temporada("test");
        temporada = temporadaDao.crea(temporada);
        assertNotNull(temporada);

        this.mockMvc.perform(get(Constantes.PATH_TEMPORADA_VER +"/"+ temporada.getId()))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_TEMPORADA_VER + ".jsp"))
                .andExpect(model()
                .attributeExists(Constantes.ADDATTRIBUTE_TEMPORADA));
    }
    @Test
    public void debieraCrearTemporada() throws Exception {
        log.debug("Debiera crear temporada");
        SimpleDateFormat sdf = new SimpleDateFormat(Constantes.DATE_SHORT_HUMAN_PATTERN);
        this.mockMvc.perform(post(Constantes.PATH_TEMPORADA_CREA)
                .param("nombre", "test")
                .param("fechaInicio", sdf.format(new Date()))
                .param("fechaFinal", sdf.format(new Date())))
                .andExpect(status().isOk())
                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "temporada.creada.message"));
    }
    @Test
    public void debieraActualizarTemporada() throws Exception {
        log.debug("Debiera actualizar  temporada");
        SimpleDateFormat sdf = new SimpleDateFormat(Constantes.DATE_SHORT_HUMAN_PATTERN);
        Temporada temporada = new Temporada("test");
        temporada = temporadaDao.crea(temporada);
        assertNotNull(temporada);

        this.mockMvc.perform(post(Constantes.PATH_TEMPORADA_ACTUALIZA)
                .param("id",temporada.getId().toString())
                .param("version", temporada.getVersion().toString())
                .param("nombre", "test1")
                .param("fechaInicio", sdf.format(new Date()))
                .param("fechaFinal", sdf.format(new Date())))
                .andExpect(status().isOk())
                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "temporada.actualizado.message"));
    }
    @Test
    public void debieraEliminarTemporada() throws Exception {
        log.debug("Debiera eliminar  temporada");
        Temporada temporada = new Temporada("test");
        temporadaDao.crea(temporada);
        assertNotNull(temporada);

        this.mockMvc.perform(post(Constantes.PATH_TEMPORADA_ELIMINA)
                .param("id", temporada.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "temporada.eliminado.message"));
    }
}
