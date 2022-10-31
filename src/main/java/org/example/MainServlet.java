package org.example;

import org.apache.commons.io.FileUtils;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.filechooser.FileSystemView;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@WebServlet(urlPatterns = "")
public class MainServlet extends HttpServlet {
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.service(req, resp);
    }

    @Override
    public void destroy() {

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserService user = UserRepository.USER_REPOSITORY.getUserByCookies(req.getCookies());
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String pathTomcat = FileSystemView.getFileSystemView().getHomeDirectory().getPath();
        String path = req.getParameter("path");
        String defaultPath = System.getProperty("os.name").toLowerCase().contains("win") ? ("H:\\" + user.getLogin() + "\\") : (pathTomcat + "/usr/" + user.getLogin() + "/");

        if (path == null || !path.startsWith(defaultPath)) {
            path = defaultPath;
        }

        path = path.replaceAll("%20", " ");
        File file = new File(path);
        Path currentPath = file.toPath();

        if (!Files.exists(currentPath)) {
            Files.createDirectories(currentPath);
        }

        if (Files.isDirectory(currentPath)) {
            req.setAttribute("date", new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date()));
            req.setAttribute("currentPath", currentPath.toString());
            showFiles(req, currentPath);
            RequestDispatcher requestDispatcher = req.getRequestDispatcher("index.jsp");
            requestDispatcher.forward(req, resp);
        } else {
            downloadFile(resp, currentPath);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (req.getParameter("exit") != null) {
            UserRepository.USER_REPOSITORY.removeUserBySession(CookieUtil.getValue(req.getCookies(), "JSESSIONID"));
            CookieUtil.addCookie(resp, "JSESSIONID", null);
            resp.sendRedirect(req.getContextPath() + "/login");
        }
    }

    private void showFiles(HttpServletRequest req, Path currentPath) throws IOException{
        File[] allFiles = currentPath.toFile().listFiles();
        if (allFiles == null) {
            return;
        }
        List<FileService> directories = new ArrayList<>();
        List<FileService> files = new ArrayList<>();
        for (File file : allFiles) {
            BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
            (file.isDirectory() ? directories : files).add(new FileService(file, FileUtils.byteCountToDisplaySize(FileUtils.sizeOf(file)), attr.lastModifiedTime()));
        }
        req.setAttribute("files", files);
        req.setAttribute("directories", directories);
    }

    private void downloadFile(HttpServletResponse resp, Path file) throws IOException {
        resp.setContentType("text/plain");
        resp.setHeader("Content-disposition", "attachment; filename=" + file.getFileName());

        try (InputStream in = new FileInputStream(file.toFile()); OutputStream out = resp.getOutputStream()) {
            byte[] buffer = new byte[1048];

            int numBytesRead;
            while ((numBytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, numBytesRead);
            }
        }
    }
}
