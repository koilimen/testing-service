package ru.testservice.serviceapp.interceptors;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import ru.testservice.serviceapp.model.Course;
import ru.testservice.serviceapp.service.CourseService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MoreCoursesInterceptor extends HandlerInterceptorAdapter {

    private final String[] urls = new String[]{"/course/", "/section/", "/tests/", "/ticket/"};
    private CourseService cs;

    public MoreCoursesInterceptor(CourseService cs) {
        this.cs = cs;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        if (!"XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
            String requestURI = request.getRequestURI();
            for (String url : urls) {
                if (requestURI.startsWith(url)) {
                    Course course = (Course) modelAndView.getModel().get("course");
                    if (course != null) {
                        modelAndView.addObject("allCourses", cs.getAllExcept(course));
                    } else {
                        modelAndView.addObject("allCourses", cs.getAll());
                    }
                    break;
                }
            }
        }

        super.postHandle(request, response, handler, modelAndView);
    }
}
