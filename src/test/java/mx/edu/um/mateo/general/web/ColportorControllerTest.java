/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.general.web;
import mx.edu.um.mateo.Constantes;
import mx.edu.um.mateo.general.dao.ColportorDao;
import mx.edu.um.mateo.general.model.Colportor;
import mx.edu.um.mateo.general.test.BaseTest;
import mx.edu.um.mateo.general.test.GenericWebXmlContextLoader;
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
public class ColportorControllerTest {

    private static final Logger log = LoggerFactory.getLogger(ColportorControllerTest.class);
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;
    @Autowired
    private ColportorDao colportorDao;

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
        
        for (int i = 0; i < 20; i++) {
            Colportor colportor = new Colportor( Constantes.STATUS_ACTIVO,Constantes.CLAVE+i,Constantes.TELEFONO,Constantes.CALLE,Constantes.COLONIA,Constantes.MUNICIPIO);
            colportorDao.crea(colportor);
            assertNotNull(colportor);
        }

        this.mockMvc.perform(get(Constantes.PATH_COLPORTOR))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_COLPORTOR_LISTA + ".jsp"))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_COLPORTORES))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINACION))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINAS))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINA));
    }

   @Test
    public void debieraMostrarColportor() throws Exception {
        log.debug("Debiera mostrar colportor");
        Colportor colportor = new Colportor( Constantes.STATUS_ACTIVO,Constantes.CLAVE,Constantes.TELEFONO,Constantes.CALLE,Constantes.COLONIA,Constantes.MUNICIPIO);
        colportor = colportorDao.crea(colportor);
        assertNotNull(colportor);

        this.mockMvc.perform(get(Constantes.PATH_COLPORTOR_VER +"/"+ colportor.getId()))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_COLPORTOR_VER + ".jsp"))
                .andExpect(model()
                .attributeExists(Constantes.ADDATTRIBUTE_COLPORTOR));
    }

    @Test
    public void debieraCrearColportor() throws Exception {
        log.debug("Debiera crear colportor");

        this.mockMvc.perform(post(Constantes.PATH_COLPORTOR_CREA)
                .param("status", Constantes.STATUS_ACTIVO)
                .param("clave",Constantes.CLAVE)
                .param("correo",Constantes.CORREO)
                .param("telefono",Constantes.TELEFONO)
                .param("calle",Constantes.CALLE)
                .param("colonia",Constantes.COLONIA)
                .param("municipio",Constantes.MUNICIPIO))
                .andExpect(status().isOk())
                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "colportor.creado.message"));
  
   
    }

    @Test
    public void debieraActualizarColportor() throws Exception {
        log.debug("Debiera actualizar colportor");
        Colportor colportor = new Colportor(Constantes.STATUS_ACTIVO,Constantes.CLAVE,Constantes.TELEFONO,Constantes.CALLE,Constantes.COLONIA,Constantes.MUNICIPIO);
        colportor = colportorDao.crea(colportor);
        assertNotNull(colportor);

        this.mockMvc.perform(post(Constantes.PATH_COLPORTOR_ACTUALIZA)
                .param("id",colportor.getId().toString())
                .param("version", colportor.getVersion().toString())
                .param("status", colportor.getStatus())
                .param("clave", colportor.getClave())
                .param("telefono", colportor.getTelefono())
                .param("calle",colportor.getCalle())
                .param("colonia",colportor.getColonia())
                .param("municipio",colportor.getMunicipio()))
                .andExpect(status().isOk())
                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "colportor.actualizado.message"));
    
 
     }

    @Test
    public void debieraEliminarColportor() throws Exception {
        log.debug("Debiera eliminar colportor");
        Colportor colportor = new Colportor(Constantes.STATUS_ACTIVO,Constantes.CLAVE,Constantes.TELEFONO,Constantes.CALLE,Constantes.COLONIA,Constantes.MUNICIPIO);
        colportorDao.crea(colportor);
        assertNotNull(colportor);

        this.mockMvc.perform(post(Constantes.PATH_COLPORTOR_ELIMINA)
                .param("id", colportor.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                    .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "colportor.eliminado.message"));
    }
}