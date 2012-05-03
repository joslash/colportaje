/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.general.web;

import mx.edu.um.mateo.Constantes;
import mx.edu.um.mateo.general.dao.CiudadDao;
import mx.edu.um.mateo.general.model.Ciudad;
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
public class CiudadControllerTest {
    private static final Logger log = LoggerFactory.getLogger(CiudadControllerTest.class);
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;
    @Autowired
    private CiudadDao ciudadDao;
    
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
    public void debieraMostrarListaDeCiudad() throws Exception {
        log.debug("Debiera monstrar lista Ciudad");
        for (int i = 0; i < 20; i++) {
            Ciudad ciudad = new Ciudad("test" + i);
            ciudadDao.crea(ciudad);
            assertNotNull(ciudad);
        }
        this.mockMvc.perform(get(Constantes.PATH_CIUDAD))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_CIUDAD_LISTA+ ".jsp"))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_CIUDADES))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINACION))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINAS))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINA));
    }
    @Test
    public void debieraMostrarCiudad() throws Exception {
        log.debug("Debiera mostrar  Ciudad");
        Ciudad ciudad = new Ciudad("test");
        ciudad = ciudadDao.crea(ciudad);
        assertNotNull(ciudad);

        this.mockMvc.perform(get(Constantes.PATH_CIUDAD_VER +"/"+ ciudad.getId()))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_CIUDAD_VER + ".jsp"))
                .andExpect(model()
                .attributeExists(Constantes.ADDATTRIBUTE_CIUDAD));
    }
    @Test
    public void debieraCrearCiudad() throws Exception {
        log.debug("Debiera crear Ciudad");
        this.mockMvc.perform(post(Constantes.PATH_CIUDAD_CREA)
                .param("nombre", "test"))
                .andExpect(status().isOk())
                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "ciudad.creada.message"));
    }
    @Test
    public void debieraActualizarPais() throws Exception {
        log.debug("Debiera actualizar  pais");
        Ciudad ciudad = new Ciudad("test");
        ciudad = ciudadDao.crea(ciudad);
        assertNotNull(ciudad);

        this.mockMvc.perform(post(Constantes.PATH_CIUDAD_ACTUALIZA)
                .param("id",ciudad.getId().toString())
                .param("version", ciudad.getVersion().toString())
                .param("nombre", "test1"))
                .andExpect(status().isOk())
                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "ciudad.actualizada.message"));
    }
    @Test
    public void debieraEliminarCiudad() throws Exception {
        log.debug("Debiera eliminar Ciudad");
        Ciudad ciudad = new Ciudad("test");
        ciudadDao.crea(ciudad);
        assertNotNull(ciudad);

        this.mockMvc.perform(post(Constantes.PATH_CIUDAD_ELIMINA)
                .param("id", ciudad.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "ciudad.eliminada.message"));
    }
}
