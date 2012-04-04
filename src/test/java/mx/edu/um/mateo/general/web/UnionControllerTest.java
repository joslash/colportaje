/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.general.web;

import mx.um.edu.mateo.Constantes;
import mx.edu.um.mateo.general.dao.UnionDao;
import mx.edu.um.mateo.general.model.Union;
import mx.edu.um.mateo.general.test.BaseTest;
import mx.edu.um.mateo.general.test.GenericWebXmlContextLoader;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
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
import static org.junit.Assert.*;

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
public class UnionControllerTest extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(UnionControllerTest.class);
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;
    @Autowired
    private UnionDao unionDao;

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
    public void debieraMostrarListaDeUnion() throws Exception {
        log.debug("Debiera monstrar lista de uniones");
        
        for (int i = 0; i < 20; i++) {
            Union union = new Union(Constantes.NOMBRE+i, Constantes.STATUS_ACTIVO);
            unionDao.crea(union);
            assertNotNull(union);
        }

        this.mockMvc.perform(get(Constantes.PATH_UNION))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_UNION_LISTA + ".jsp"))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_UNIONES))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINACION))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINAS))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINA));
    }

   @Test
    public void debieraMostrarUnion() throws Exception {
        log.debug("Debiera mostrar union");
        Union union = new Union(Constantes.NOMBRE, Constantes.STATUS_ACTIVO);
        union = unionDao.crea(union);
        assertNotNull(union);

        this.mockMvc.perform(get(Constantes.PATH_UNION_VER +"/"+ union.getId()))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_UNION_VER + ".jsp"))
                .andExpect(model()
                .attributeExists(Constantes.ADDATTRIBUTE_UNION));
    }

    @Test
    public void debieraCrearUnion() throws Exception {
        log.debug("Debiera crear union");

        this.mockMvc.perform(post(Constantes.PATH_UNION_CREA)
                .param("nombre", "test")
                .param("status", Constantes.STATUS_ACTIVO))
                .andExpect(status().isOk())
                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "union.creada.message"));
    }

    @Test
    public void debieraActualizarUnion() throws Exception {
        log.debug("Debiera actualizar union");
        Union union = new Union(Constantes.NOMBRE, Constantes.STATUS_ACTIVO);
        union = unionDao.crea(union);
        assertNotNull(union);

        this.mockMvc.perform(post(Constantes.PATH_UNION_ACTUALIZA)
                .param("id",union.getId().toString())
                .param("version", union.getVersion().toString())
                .param("nombre", "test1")
                .param("status", union.getStatus()))
                .andExpect(status().isOk())
                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "union.actualizada.message"));
    }

    @Test
    public void debieraEliminarUnion() throws Exception {
        log.debug("Debiera eliminar union");
        Union union = new Union(Constantes.NOMBRE, Constantes.STATUS_ACTIVO);
        unionDao.crea(union);
        assertNotNull(union);

        this.mockMvc.perform(post(Constantes.PATH_UNION_ELIMINA)
                .param("id", union.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                    .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "union.eliminada.message"));
    }
}