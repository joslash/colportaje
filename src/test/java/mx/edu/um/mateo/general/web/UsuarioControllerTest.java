    /*
 * The MIT License
 *
 * Copyright 2012 jdmr.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package mx.edu.um.mateo.general.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import mx.edu.um.mateo.Constantes;
import mx.edu.um.mateo.general.dao.RolDao;
import mx.edu.um.mateo.general.dao.UnionDao;
import mx.edu.um.mateo.general.dao.UsuarioDao;
import mx.edu.um.mateo.general.model.*;
import mx.edu.um.mateo.general.test.BaseTest;
import mx.edu.um.mateo.general.test.GenericWebXmlContextLoader;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.*;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.junit.Assert.assertNotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.server.MockMvc;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
import org.springframework.test.web.server.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

/**
 *
 * @author jdmr
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = GenericWebXmlContextLoader.class, locations = {
    "classpath:mateo.xml",
    "classpath:security.xml",
    "classpath:dispatcher-servlet.xml"
})
@Transactional
public class UsuarioControllerTest extends BaseTest{
  private static final Logger log = LoggerFactory.getLogger(UsuarioControllerTest.class);
    @Autowired
    private UsuarioDao usuarioDao;
    @Autowired
    private UnionDao unionDao;
    @Autowired
    private RolDao rolDao;
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;
        @Autowired
    private SessionFactory sessionFactory;

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    public UsuarioControllerTest() {
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
    public void debieraMostrarListaDeUsuarios() throws Exception {
        Union union = new Union("test");
        union.setStatus(Constantes.STATUS_ACTIVO);
        currentSession().save(union);
        Rol rol = new Rol("ROLE_TEST");
        currentSession().save(rol);
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        Asociacion asociacion = new Asociacion("TEST01", Constantes.STATUS_ACTIVO, union);
        currentSession().save(asociacion);
        for(int i=0; i<10; i++){
        Usuario usuario = new Usuario("test"+i+"@test.com", "test", "test", "test", "test");
        usuario.setRoles(roles);
        usuario.setAsociacion(asociacion);
        currentSession().save(usuario);
        }
        this.mockMvc.perform(get("/admin/usuario")
                .sessionAttr(Constantes.SESSION_ASOCIACION, asociacion))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/jsp/admin/usuario/lista.jsp"))
                .andExpect(model().attributeExists("usuarios"))
                .andExpect(model().attributeExists("paginacion"))
                .andExpect(model().attributeExists("paginas"))
                .andExpect(model().attributeExists("pagina"));
    }
    
      @Test 
      public void debieraMostrarUsuario() throws Exception { 
        Union union = new Union("test");
        union.setStatus(Constantes.STATUS_ACTIVO);
        currentSession().save(union);
        Rol rol = new Rol("ROLE_TEST");
        currentSession().save(rol);
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        //for(int i=0; i<=10; i++){
        Asociacion asociacion = new Asociacion("TEST01", Constantes.STATUS_ACTIVO, union);
        currentSession().save(asociacion);
        Usuario usuario = new Usuario("test@test.com", "test", "test", "test", "test");
        usuario.setRoles(roles);
        usuario.setAsociacion(asociacion);
        currentSession().save(usuario);
        
         Long id= usuario.getId();
         this.mockMvc.perform(get("/admin/usuario/ver/"+id))
              .andExpect(status().isOk())
              .andExpect(forwardedUrl("/WEB-INF/jsp/admin/usuario/ver.jsp"))
              .andExpect(model().attributeExists("usuario"))
              .andExpect(model().attributeExists("roles"));
     }
    

      /**
       * 
       * TODO problemas con el URL y con el id  @throws Exception 
       */
      /*
      @Test
      public void deberiaCrearUsuario()throws Exception { 
        log.debug("Deberia crear Usuario");
        Union union = new Union("test");
        union.setStatus(Constantes.STATUS_ACTIVO);
        currentSession().save(union);
        Rol rol = new Rol("ROLE_USER");
        currentSession().save(rol);
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        Asociacion asociacion = new Asociacion("TEST01", Constantes.STATUS_ACTIVO, union);
        currentSession().save(asociacion);

        rol = rolDao.crea(rol);
        Usuario usuario = new Usuario("test@test.com", "test", "test", "test","test");
        usuario = usuarioDao.crea(usuario,asociacion.getId(), new String[]{rol.getAuthority()});
        Long id = usuario.getId();
        assertNotNull(id);
        
       this.authenticate(usuario, usuario.getPassword(), new ArrayList(usuario.getAuthorities()));
      

      this.mockMvc.perform(post("/admin/usuario/crea")
              .sessionAttr("asociacionId", asociacion.getId())
              .param("roles",  "ROLE_USER")
              .param("username","test--01@test.com") 
              .param("nombre", "TEST--01")
              .param("apellidoP","TEST--01") 
              .param("apellidoM","TEST--01")) 
              .andExpect(status().isOk())
              //.andExpect(redirectedUrl("/admin/usuario/ver/"))
              .andExpect(flash().attributeExists("message"))
              .andExpect(flash().attribute("message","usuario.creado.message")) ;
    }*/


   @Test
    public void debieraActualizarUsuario() throws Exception {
        log.debug("Debiera actualizar usuario");
         Union union = new Union("test");
        union.setStatus(Constantes.STATUS_ACTIVO);
        currentSession().save(union);
        Rol rol = new Rol("ROLE_TEST");
        currentSession().save(rol);
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
         Asociacion asociacion = new Asociacion("TEST01", Constantes.STATUS_ACTIVO, union);
        currentSession().save(asociacion);
         Usuario usuario = new Usuario("test@test.com", "test", "test", "test", "test");
         usuario.setRoles(roles);
         usuario.setAsociacion(asociacion);
         currentSession().save(usuario);
         
          this.mockMvc.perform(post("/admin/usuario/actualiza")
                .param("id", usuario.getId().toString()))
                .andExpect(status().isOk());
    }    
   
    @Test
    public void debieraEliminarUsuario() throws Exception {
        log.debug("Debiera eliminar usuario");
         Union union = new Union("test");
        union.setStatus(Constantes.STATUS_ACTIVO);
        currentSession().save(union);
        Rol rol = new Rol("ROLE_TEST");
        currentSession().save(rol);
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
         Asociacion asociacion = new Asociacion("TEST01", Constantes.STATUS_ACTIVO, union);
        currentSession().save(asociacion);
         Usuario usuario = new Usuario("test@test.com", "test", "test", "test", "test");
         usuario.setRoles(roles);
         usuario.setAsociacion(asociacion);
         currentSession().save(usuario);
         

        this.mockMvc.perform(post("/admin/usuario/elimina")
                .param("id", usuario.getId().toString()))
                .andExpect(status().isOk());
               // .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                //.andExpect(flash().attribute("message", "usuario.eliminado.message"));
        
    }

}
