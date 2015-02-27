package test;

import com.mycompany.arquillianwarp.IndexBean;
import java.io.File;
import java.net.URL;
import javax.ws.rs.core.Response;
import static org.hamcrest.CoreMatchers.is;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.extension.rest.warp.api.RestContext;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.warp.Activity;
import org.jboss.arquillian.warp.Inspection;
import org.jboss.arquillian.warp.Warp;
import org.jboss.arquillian.warp.WarpTest;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import static org.jboss.arquillian.warp.client.filter.http.HttpFilters.request;
import org.jboss.arquillian.warp.client.filter.http.HttpMethod;
import org.jboss.arquillian.warp.client.result.WarpResult;
import org.jboss.arquillian.warp.servlet.AfterServlet;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 *
 * @author kikuta
 */
@WarpTest
@RunAsClient
@RunWith(Arquillian.class)
public class RedirectTest {
    
    @Drone
    WebDriver browser;

    @ArquillianResource
    URL contextPath;
    
    @Deployment
    public static WebArchive createDeployment() {

        return ShrinkWrap.create(WebArchive.class, "jsf-test.war").addClasses(IndexBean.class)
                .addAsWebResource(new File("src/main/webapp/index.xhtml"))
                .addAsWebResource(new File("src/main/webapp/second.xhtml"))
                .addAsWebInfResource(new File("src/main/webapp/WEB-INF/web.xml"));
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    //@Ignore
    @Test
    //@RunAsClient
    public void hello() {
        //https://github.com/lfryc/arquillian.github.com/blob/warp-docs/docs/warp.adoc
        browser.navigate().to(contextPath + "faces/index.xhtml");
        Warp.initiate(() -> browser.findElement(By.name("frm:btn")).click())
                .group("faces/index.xhtml")
                .observe(request().uri().contains("index.xhtml"))
                .inspect()
                .group("faces/second.xhtml")
                .observe(request().index(2)).inspect()
                .execute();
        assertThat(browser.getTitle(), is("second"));
    }

    //@Ignore
    @Test
    //@RunAsClient
    public void hello2() {
        //https://github.com/lfryc/arquillian.github.com/blob/warp-docs/docs/warp.adoc
        browser.navigate().to(contextPath + "faces/index.xhtml");
        WarpResult result = Warp.initiate(() -> browser.findElement(By.name("frm:btn")).click())
                .group("faces/index.xhtml")
                    .observe(request().uri().contains("index.xhtml"))
                    .inspect()
                .group("faces/second.xhtml")
                    .observe(request().uri().contains("second.xhtml"))//.observe(request().index(2))
                    .inspect()
                .execute();
        assertThat(browser.getTitle(), is("second"));
    }

    @Test
    public void hello3() {
        //画面遷移なら下記でも可能
        browser.get(contextPath.toExternalForm() + "faces/index.xhtml");
        browser.findElement(By.name("frm:btn")).click();
        assertThat(browser.getTitle(), is("second"));
        
        //ページに指定文字列が含まれるか
        assertTrue(browser.getPageSource().contains("Hello from Facelets"));
    }

    @Test
    public void shouldMakeProperRequest() {
        //http://slidedeck.io/mmatloka/arq-extensions-presentation
        //Arquillian Warp
        Warp.initiate(new Activity() {
          @Override
          public void perform() {
             browser.navigate().to(contextPath + "faces/index.xhtml");
          }
       }).group().observe(request().uri().endsWith("/index.xhtml"))
       .inspect(new Inspection() {
            private static final long serialVersionUID = 1L;

            @ArquillianResource
            private RestContext restContext;

            @AfterServlet
            public void testGetStocks() {
                assertTrue(restContext.getHttpResponse().getStatusCode() == Response.Status.OK.getStatusCode());

//             assertThat(restContext.getHttpRequest().getMethod()).isEqualTo(HttpMethod.GET);
//             assertThat(restContext.getHttpResponse().getStatusCode()).isEqualTo(Response.Status.OK.getStatusCode());
//             assertThat(restContext.getHttpResponse().getContentType()).isEqualTo("application/json");
//             List list = (List) restContext.getHttpResponse().getEntity();
//             assertThat(list.size()).isEqualTo(1);
             }
          });
    }
}
