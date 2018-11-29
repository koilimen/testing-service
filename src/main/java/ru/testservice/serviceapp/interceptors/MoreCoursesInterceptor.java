package ru.testservice.serviceapp.interceptors;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import ru.testservice.serviceapp.model.Course;
import ru.testservice.serviceapp.model.Folder;
import ru.testservice.serviceapp.service.CourseService;
import ru.testservice.serviceapp.service.StorageService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

public class MoreCoursesInterceptor extends HandlerInterceptorAdapter {

    private final String[] urls = new String[]{"/course/", "/section/"};
    private final CourseService cs;
    private final StorageService storageService;

    public MoreCoursesInterceptor(CourseService cs, StorageService storageService) {
        this.cs = cs;
        this.storageService = storageService;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        if (!"XMLHttpRequest".equals(request.getHeader("X-Requested-With")) && modelAndView != null) {
            String viewName = modelAndView.getViewName();
            modelAndView.addObject("viewName", viewName == null ? "null": viewName);
//            String requestURI = request.getRequestURI();
//            for (String url : urls) {
//                if (requestURI.startsWith(url)) {
//                    Course course = (Course) modelAndView.getModel().get("course");
//                    if (course != null) {
//                        modelAndView.addObject("allCourses", cs.getAllExcept(course));
//                    } else {
//                        modelAndView.addObject("allCourses", cs.getAll());
//                    }
//                    break;
//                }
//            }
        }

        super.postHandle(request, response, handler, modelAndView);
    }
}
