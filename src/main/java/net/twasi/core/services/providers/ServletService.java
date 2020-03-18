package net.twasi.core.services.providers;

import net.twasi.core.services.IService;

import javax.servlet.Servlet;
import java.util.ArrayList;
import java.util.List;

public class ServletService implements IService {

    private List<String> registeredPaths = new ArrayList<>();
    private IServletPasser passer;

    public ServletService(IServletPasser passer) {
        this.passer = passer;
    }

    /**
     * @param handler The handling servlet class
     * @param path    The path where the servlet should be reachable at
     * @throws RuntimeException if another handler with the same path already is present
     */
    public void addServlet(Class<? extends Servlet> handler, String path) {
        path = path.toLowerCase();
        if (registeredPaths.contains(path))
            throw new RuntimeException("A servlet with that path alreay is registered.");
        registeredPaths.add(path);
        passer.pass(handler, path);
    }

    public interface IServletPasser {

        void pass(Class<? extends Servlet> handler, String path);
    }
}
