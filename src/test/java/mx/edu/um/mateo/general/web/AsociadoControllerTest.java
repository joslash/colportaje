/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.general.web;
import mx.edu.um.mateo.general.dao.AsociadoDao;
import mx.edu.um.mateo.general.model.Asociado;
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
public class AsociadoControllerTest extends BaseTest{
    private static final Logger log = LoggerFactory.getLogger(AsociadoControllerTest.class);
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;
    @Autowired
    private AsociadoDao asociadoDao;

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
    public void debieraMostrarListaDeAsociado() throws Exception {
        log.debug("Debiera monstrar lista asociado");
        
        for (int i = 0; i < 20; i++) {
            Asociado asociado = new Asociado("test" + i,"test"+i,"test"+i,"test"+i,Constantes.STATUS_ACTIVO);
            asociadoDao.crea(asociado);
            assertNotNull(asociado);
        }

        this.mockMvc.perform(get(Constantes.PATH_ASOCIADO))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_ASOCIADO_LISTA+ ".jsp"))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_ASOCIADOS))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINACION))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINAS))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINA));
    }
    @Test
    public void debieraMostrarAsociado() throws Exception {
        log.debug("Debiera mostrar  asociado");
        Asociado asociado = new Asociado("test","test","test","test", Constantes.STATUS_ACTIVO);
        asociado = asociadoDao.crea(asociado);
        assertNotNull(asociado);

        this.mockMvc.perform(get(Constantes.PATH_ASOCIADO_VER +"/"+ asociado.getId()))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_ASOCIADO_VER + ".jsp"))
                .andExpect(model()
                .attributeExists(Constantes.ADDATTRIBUTE_ASOCIADO));
    }
    @Test
    public void debieraCrearAsociado() throws Exception {
        log.debug("Debiera crear asociado");

        this.mockMvc.perform(post(Constantes.PATH_ASOCIADO_CREA)
                .param("nombre", "test")
                .param("clave", "test")
                .param("direccion", "test")
                .param("telefono", "test")
                .param("correo", "test@tes.tes")
                .param("status", Constantes.STATUS_ACTIVO))
                .andExpect(status().isOk())
                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "asociado.creada.message"));
    }
    @Test
    public void debieraActualizarAsociado() throws Exception {
        log.debug("Debiera actualizar  asociado");
        Asociado asociado = new Asociado("test","test","test","test", Constantes.STATUS_ACTIVO);
        asociado = asociadoDao.crea(asociado);
        assertNotNull(asociado);

        this.mockMvc.perform(post(Constantes.PATH_ASOCIADO_ACTUALIZA)
                .param("id",asociado.getId().toString())
                .param("version", asociado.getVersion().toString())
                .param("nombre", "test1")
                .param("clave", "test2")
                .param("direccion", "test3")
                .param("telefono", "test4")
                .param("correo", "test0@tes.tst")
                .param("status", asociado.getStatus()))
                .andExpect(status().isOk())
                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "asociado.actualizado.message"));
    }
    @Test
    public void debieraEliminarAsociacion() throws Exception {
        log.debug("Debiera eliminar  asociado");
        Asociado asociado = new Asociado("test","test","test","test",Constantes.STATUS_ACTIVO);
        asociadoDao.crea(asociado);
        assertNotNull(asociado);

        this.mockMvc.perform(post(Constantes.PATH_ASOCIADO_ELIMINA)
                .param("id", asociado.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "asociado.eliminado.message"));
    }
}
