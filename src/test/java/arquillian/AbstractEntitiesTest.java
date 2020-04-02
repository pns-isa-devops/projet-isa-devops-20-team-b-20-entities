package arquillian;

import javax.ejb.EJB;

import org.hsqldb.Database;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ClassLoaderAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;

import fr.polytech.entities.Delivery;

/**
 * AbstractEntitiesTest
 */
public class AbstractEntitiesTest {

    @EJB
    protected Database memory;

    @Deployment
    public static WebArchive createDeployement() {
        return ShrinkWrap.create(WebArchive.class, "persistence-test.war").addPackage(Delivery.class.getPackage())
                .addAsManifestResource(new ClassLoaderAsset("META-INF/persistence.xml"), "persistence.xml");
    }
}
